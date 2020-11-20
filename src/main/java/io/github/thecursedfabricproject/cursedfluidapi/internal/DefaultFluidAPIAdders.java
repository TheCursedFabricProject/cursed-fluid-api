package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.fabricmc.api.ModInitializer;
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
        //Cauldron
        FluidApiKeys.SIDED_FLUID_INSERTABLE.registerForBlocks(
            (world, pos, side) -> new FluidInsertable(){
                    @Override
                    public long insertFluid(long amount, Identifier fluidkey, boolean simulation) {
                        BlockState state = world.getBlockState(pos);
                        int level = state.get(CauldronBlock.LEVEL);
                        if (level == 3) return amount;
                        if (level <= 2) {
                            if (amount <= level * 27000) {
                                amount -= 27000;
                                level += 1;
                            } else {
                                return amount;
                            }
                        }
                        if (level <= 1) {
                            if (amount <= level * 27000) {
                                amount -= 27000;
                                level += 1;
                            } else {
                                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                                return amount;
                            }
                        }
                        if (level == 0) {
                            if (amount <= level * 27000) {
                                amount -= 27000;
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
                return ((long)world.getBlockState(pos).get(CauldronBlock.LEVEL)) * 27000l;
            }
            
        }, Blocks.CAULDRON);

        FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerForBlocks((world, pos, side) -> new FluidExtractable(){

            @Override
            public Identifier getFluidKey() {
                return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidamount(long maxamount, Identifier fluidKey, boolean simulation) {
                BlockState state = world.getBlockState(pos);
                int level = state.get(CauldronBlock.LEVEL);
                int maxextractlevel = (int) MathHelper.clamp((long)Math.floor((double)maxamount / 27000.0), 0l, 3l);
                int extract = MathHelper.clamp(level, 0, maxextractlevel);
                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, extract);
                return extract;
            }
            
        }, Blocks.CAULDRON);
    }
    
}
