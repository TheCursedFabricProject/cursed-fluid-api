package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidIO;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.Simulation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

class CauldronCompat {
    private CauldronCompat() {}
    public static void init() {
        FluidApiKeys.SIDED_FLUID_IO.registerForBlocks((world, pos, side) -> new CauldronFluidIO(world, pos), Blocks.CAULDRON);
    }

    private static class CauldronFluidIO implements FluidIO, FluidExtractable, FluidInsertable {
        private final World world;
        private final BlockPos pos;
        public CauldronFluidIO(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        @Override
        public long insertFluid(long amount, Fluid fluid, Simulation simulation) {
            if (!fluid.equals(Fluids.WATER)) return amount;
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
                    if (simulation.isAct()) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                    return amount;
                }
            }
            if (originallevel == 0) {
                if (amount - FluidConstants.BOTTLE >= 0 && level < 3) {
                    amount -= FluidConstants.BOTTLE;
                    level += 1;
                } else {
                    if (simulation.isAct()) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
                    return amount;
                }
            }
            if (simulation.isAct()) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
            return amount;
        }

        @Override
        public int getFluidSlotCount() {
            return 1;
        }

        @Override
        public Fluid getFluid(int slot) {
            return world.getBlockState(pos).get(CauldronBlock.LEVEL) > 0 ? Fluids.WATER : Fluids.EMPTY;
        }

        @Override
        public long getFluidAmount(int slot) {
            return ((long)world.getBlockState(pos).get(CauldronBlock.LEVEL)) * FluidConstants.BOTTLE;
        }

        @Override
        public long extractFluidAmount(long maxamount, Fluid fluid, Simulation simulation) {
            BlockState state = world.getBlockState(pos);
            int level = state.get(CauldronBlock.LEVEL);
            int maxExtractLevel = (int) MathHelper.clamp((long)Math.floor((double)maxamount / (double)FluidConstants.BOTTLE), 0l, 3l);
            int extractLevel = MathHelper.clamp(level, 0, maxExtractLevel);
            level -= extractLevel;
            if (simulation.isAct()) ((CauldronBlock)state.getBlock()).setLevel(world, pos, state, level);
            return extractLevel * FluidConstants.BOTTLE;
        }

        @Override
        public int getVersion() {
            return world.getBlockState(pos).get(CauldronBlock.LEVEL);
        }
    }
}
