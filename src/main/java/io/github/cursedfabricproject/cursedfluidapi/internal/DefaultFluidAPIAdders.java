package io.github.cursedfabricproject.cursedfluidapi.internal;

import io.github.cursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.cursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.cursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.cursedfabricproject.cursedfluidapi.FluidView;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class DefaultFluidAPIAdders implements ModInitializer {

    @Override
    public void onInitialize() {
        //Cauldron
        FluidApiKeys.SIDED_FLUID_INSERTABLE.registerForBlocks(
            (world, pos, side) -> {
                return new FluidInsertable(){
                        @Override
                        public long insertFluid(long ammount, Identifier fluidkey, boolean simulation) {
                            BlockState state = world.getBlockState(pos);
                            int level = state.get(CauldronBlock.LEVEL);
                            if (level == 3) return ammount;
                            if (level <= 2) {
                                if (ammount <= level * 27000) {
                                    ammount -= 27000;
                                    level += 1;
                                } else {
                                    return ammount;
                                }
                            }
                            if (level <= 1) {
                                if (ammount <= level * 27000) {
                                    ammount -= 27000;
                                    level += 1;
                                } else {
                                    if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                                    return ammount;
                                }
                            }
                            if (level == 0) {
                                if (ammount <= level * 27000) {
                                    ammount -= 27000;
                                    level += 1;
                                } else {
                                    if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                                    return ammount;
                                }
                            }
                            if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                            return ammount;
                        }
                };
            },
            Blocks.CAULDRON
        );
        FluidApiKeys.BLOCK_FLUID_VIEW.registerForBlocks((world, pos, side) -> new FluidView(){

            @Override
            public Identifier getFluidKey() {
                return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidAmmount() {
                return ((long)world.getBlockState(pos).get(CauldronBlock.LEVEL)) * 27000l;
            }
            
        }, Blocks.CAULDRON);

        FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerForBlocks((world, pos, side) -> new FluidExtractable(){

            @Override
            public Identifier getFluidKey() {
                return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidAmmount(long maxammount, Identifier fluidKey, boolean simulation) {
                BlockState state = world.getBlockState(pos);
                int level = state.get(CauldronBlock.LEVEL);
                int maxextractlevel = (int) MathHelper.clamp((long)Math.floor((double)maxammount / 27000.0), 0l, 3l);
                int extract = MathHelper.clamp(level, 0, maxextractlevel);
                if (!simulation) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, extract);
                return extract;
            }
            
        }, Blocks.CAULDRON);
    }
    
}
