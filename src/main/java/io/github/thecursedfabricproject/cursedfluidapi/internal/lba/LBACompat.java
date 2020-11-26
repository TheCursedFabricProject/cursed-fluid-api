package io.github.thecursedfabricproject.cursedfluidapi.internal.lba;

import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;

public class LBACompat {
    private static Logger screamAtModDeveloper = LogManager.getLogger("Cursed Fluid API LBACompat");

    private static final FluidAmount U_AMOUNT = FluidAmount.of(1, FluidConstants.BUCKET);
    private static final FluidAmount LITER_AMOUNT = FluidAmount.of(1, 1000);

    private LBACompat() {
    }

    public static void init() {
        FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerBlockFallback(
            (world, pos, side) -> new LBAFluidExtractable(FluidAttributes.EXTRACTABLE.get(world, pos, SearchOptions.inDirection(side.getOpposite())))
        );

        FluidApiKeys.SIDED_FLUID_INSERTABLE.registerBlockFallback(
            (world, pos, side) -> new LBAFluidInsertable(FluidAttributes.INSERTABLE.get(world, pos, SearchOptions.inDirection(side.getOpposite())))
        );
    }

    private static class LBAFluidExtractable implements FluidExtractable {
        private final alexiil.mc.lib.attributes.fluid.FluidExtractable extractable;

        public LBAFluidExtractable(alexiil.mc.lib.attributes.fluid.FluidExtractable extractable) {
            this.extractable = extractable;
        }

        @Override
        public int getSlotCount() {
            return 1;
        }

        @Override
        public Fluid getFluid(int slot) {
            Fluid fluid = extractable.attemptAnyExtraction(FluidAmount.ABSOLUTE_MAXIMUM, Simulation.SIMULATE).getFluidKey().getRawFluid();
            return fluid == null ? Fluids.EMPTY : fluid;
        }

        @Override
        public long getFluidAmount(int slot) {
            return extractFluidAmount(Integer.MAX_VALUE, getFluid(0), true);
        }

        @Override
        public long extractFluidAmount(long maxamount, Fluid fluid, boolean simulation) {
            if (fluid != getFluid(1)) return 0l;
            long total = extractable.attemptAnyExtraction(FluidAmount.ABSOLUTE_MAXIMUM, Simulation.SIMULATE).amount().getCountOf(U_AMOUNT);
            long uExtractTarget = Math.min(total, maxamount);
            FluidAmount extractFluidAmount = U_AMOUNT.mul(uExtractTarget);
            if (extractFluidAmount.equals(extractable.attemptAnyExtraction(extractFluidAmount, Simulation.SIMULATE).amount())) {
                if (extractFluidAmount.equals(extractable.attemptAnyExtraction(extractFluidAmount, simulation ? Simulation.SIMULATE : Simulation.ACTION).amount())) {
                    return uExtractTarget;
                } else {
                    screamAtModDeveloper.warn("{} Did something very strange (Error 0)", extractable.getClass());
                }
            }
            long literExtractTarget = Math.min(total, maxamount/81);
            extractFluidAmount = LITER_AMOUNT.mul(literExtractTarget);
            if (extractFluidAmount.equals(extractable.attemptAnyExtraction(extractFluidAmount, Simulation.SIMULATE).amount())) {
                if (extractFluidAmount.equals(extractable.attemptAnyExtraction(extractFluidAmount, simulation ? Simulation.SIMULATE : Simulation.ACTION).amount())) {
                    return literExtractTarget * 81;
                } else {
                    screamAtModDeveloper.warn("{} Did something very strange (Error 0)", extractable.getClass());
                }
            }
            return 0;
        }

    }

    private static class LBAFluidInsertable implements FluidInsertable {
        private final alexiil.mc.lib.attributes.fluid.FluidInsertable insertable;

        public LBAFluidInsertable(alexiil.mc.lib.attributes.fluid.FluidInsertable insertable) {
            this.insertable = insertable;
        }

        @Override
        public long insertFluid(long amount, Fluid fluid, boolean simulation) {
            FluidAmount a = U_AMOUNT.mul(amount);
            FluidKey lbaKey = FluidKeys.get(fluid);
            if (lbaKey == null) return amount;
            FluidVolume b = insertable.attemptInsertion(lbaKey.withAmount(a), Simulation.SIMULATE);
            long c = amount - b.amount().asLong(FluidConstants.BUCKET, RoundingMode.DOWN);
            FluidAmount d = insertable.attemptInsertion(lbaKey.withAmount(U_AMOUNT.mul(c)), Simulation.SIMULATE).amount();
            if (0 == d.asLong(FluidConstants.BUCKET, RoundingMode.DOWN)) {
                if (d.equals(insertable.attemptInsertion(lbaKey.withAmount(U_AMOUNT.mul(c)), simulation ? Simulation.SIMULATE : Simulation.ACTION).amount())) {
                    return amount - c;
                } else {
                    screamAtModDeveloper.warn("{} Did something very strange (Error 1)", insertable.getClass());
                }
            }
            return amount;
        }

        
    }
}
