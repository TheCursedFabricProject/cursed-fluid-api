package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.internal.lba.LBACompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class DefaultFluidAPIAdders implements ModInitializer {

    @Override
    public void onInitialize() {
        //*LBA
        if (FabricLoader.getInstance().isModLoaded("libblockattributes")) {
            LBACompat.addLBACompat();
        }
        //*Cauldron
        FluidApiKeys.SIDED_FLUID_INSERTABLE.registerForBlocks(
            (world, pos, side) -> new FluidInsertable(){
                    @Override
                    public long insertFluid(long amount, Identifier fluidkey, boolean simulation) {
                        if (!fluidkey.equals(Registry.FLUID.getId(Fluids.WATER))) return amount;
                        BlockState state = world.getBlockState(pos);
                        int originallevel = state.get(CauldronBlock.LEVEL);
                        int level = originallevel;
                        if (originallevel == 3) return amount;
                        if (originallevel <= 2) {
                            if (amount - FluidConstants.BOTTLE >= 0 && level < 3) {
                                amount -= FluidConstants.BOTTLE;
                                level += 1;
                            } else {
                                return amount;
                            }
                        }
                        if (originallevel <= 1) {
                            if (amount - FluidConstants.BOTTLE >= 0 && level < 3) {
                                amount -= FluidConstants.BOTTLE;
                                level += 1;
                            } else {
                                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                                return amount;
                            }
                        }
                        if (originallevel == 0) {
                            if (amount - FluidConstants.BOTTLE >= 0 && level < 3) {
                                amount -= FluidConstants.BOTTLE;
                                level += 1;
                            } else {
                                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                                return amount;
                            }
                        }
                        if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                        return amount;
                    }
                },
            Blocks.CAULDRON
        );
        FluidApiKeys.BLOCK_FLUID_VIEW.registerForBlocks((world, pos, side) -> new FluidView(){

            @Override
            public Identifier getFluidKey() {
                return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidamount() {
                return ((long)world.getBlockState(pos).get(CauldronBlock.LEVEL)) * FluidConstants.BOTTLE;
            }
            
        }, Blocks.CAULDRON);

        FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerForBlocks((world, pos, side) -> new FluidExtractable(){

            @Override
            public Identifier getFluidKey() {
                return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long extractFluidAmount(long maxamount, Identifier fluidKey, boolean simulation) {
                BlockState state = world.getBlockState(pos);
                int level = state.get(CauldronBlock.LEVEL);
                int maxExtractLevel = (int) MathHelper.clamp((long)Math.floor((double)maxamount / (double)FluidConstants.BOTTLE), 0l, 3l);
                int extractLevel = MathHelper.clamp(level, 0, maxExtractLevel);
                level -= extractLevel;
                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                return extractLevel * FluidConstants.BOTTLE;
            }
            
        }, Blocks.CAULDRON);
    }
    
}
