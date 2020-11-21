package io.github.thecursedfabricproject.cursedfluidapi.internal.lba;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alexiil.mc.lib.attributes.SearchOptionDirectional;
import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class LBACompat {
    private static Logger screamAtModDeveloper = LogManager.getLogger("Cursed Fluid API LBACompat");

    private static List<SearchOptionDirectional<Object>> allDirections = Arrays.asList(
            SearchOptions.inDirection(Direction.UP), SearchOptions.inDirection(Direction.DOWN),
            SearchOptions.inDirection(Direction.EAST), SearchOptions.inDirection(Direction.WEST),
            SearchOptions.inDirection(Direction.NORTH), SearchOptions.inDirection(Direction.SOUTH));

    private static final FluidAmount U_AMOUNT = FluidAmount.of(1, 81000);
    private static final FluidAmount LITER_AMOUNT = FluidAmount.of(1, 1000);

    private LBACompat() {
    }

    public static void addLBACompat() {
        FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerFallback(
            (world, pos, side) -> new LBAFluidExtractable(FluidAttributes.EXTRACTABLE.get(world, pos, SearchOptions.inDirection(side.getOpposite()))),
            (world, pos) -> {
                for (SearchOptionDirectional<Object> directional : allDirections) {
                    alexiil.mc.lib.attributes.fluid.FluidExtractable lbaextractable = FluidAttributes.EXTRACTABLE.get(world, pos, directional);
                    if (lbaextractable != FluidAttributes.EXTRACTABLE.defaultValue) return true;
                }
                return false;
            }
        );
    }

    private static class LBAFluidExtractable implements FluidExtractable {
        private final alexiil.mc.lib.attributes.fluid.FluidExtractable extractable;

        public LBAFluidExtractable(alexiil.mc.lib.attributes.fluid.FluidExtractable extractable) {
            this.extractable = extractable;
        }

        @Override
        public Identifier getFluidKey() {
            return extractable.attemptAnyExtraction(FluidAmount.ABSOLUTE_MAXIMUM, Simulation.SIMULATE).getFluidKey().entry.getId();
        }

        @Override
        public long extractFluidAmount(long maxamount, Identifier fluidKey, boolean simulation) {
            long total = extractable.attemptAnyExtraction(FluidAmount.ABSOLUTE_MAXIMUM, Simulation.SIMULATE).amount().getCountOf(U_AMOUNT);
            long uExtractTarget = Math.min(total, maxamount);
            FluidAmount extratractFluidAmount = U_AMOUNT.mul(uExtractTarget);
            if (extratractFluidAmount.equals(extractable.attemptAnyExtraction(extratractFluidAmount, Simulation.SIMULATE).amount())) {
                if (extratractFluidAmount.equals(extractable.attemptAnyExtraction(extratractFluidAmount, Simulation.ACTION).amount())) {
                    return uExtractTarget;
                } else {
                    screamAtModDeveloper.warn("{} Did something very strange (Error 0)", extractable.getClass());
                }
            }
            long literExtractTarget = Math.min(total, maxamount/81);
            extratractFluidAmount = LITER_AMOUNT.mul(literExtractTarget);
            if (extratractFluidAmount.equals(extractable.attemptAnyExtraction(extratractFluidAmount, Simulation.SIMULATE).amount())) {
                if (extratractFluidAmount.equals(extractable.attemptAnyExtraction(extratractFluidAmount, Simulation.ACTION).amount())) {
                    return literExtractTarget * 81;
                } else {
                    screamAtModDeveloper.warn("{} Did something very strange (Error 0)", extractable.getClass());
                }
            }
            return 0;
        }

    }
}
