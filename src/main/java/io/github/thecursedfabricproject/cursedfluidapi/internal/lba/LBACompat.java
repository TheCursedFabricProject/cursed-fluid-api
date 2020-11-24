package io.github.thecursedfabricproject.cursedfluidapi.internal.lba;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alexiil.mc.lib.attributes.SearchOptionDirectional;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class LBACompat {
    private static Logger screamAtModDeveloper = LogManager.getLogger("Cursed Fluid API LBACompat");

    private static List<SearchOptionDirectional<Object>> allDirections = Arrays.asList(
            SearchOptions.inDirection(Direction.UP), SearchOptions.inDirection(Direction.DOWN),
            SearchOptions.inDirection(Direction.EAST), SearchOptions.inDirection(Direction.WEST),
            SearchOptions.inDirection(Direction.NORTH), SearchOptions.inDirection(Direction.SOUTH));

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
        public Identifier getFluidKey() {
            return extractable.attemptAnyExtraction(FluidAmount.ABSOLUTE_MAXIMUM, Simulation.SIMULATE).getFluidKey().entry.getId();
        }

        @Override
        public long extractFluidAmount(long maxamount, boolean simulation) {
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

    private static HashMap<Identifier, FluidKey> fluidKeyLookup = new HashMap<>();

    public static FluidKey getLBAFluidKey(Identifier id) {
        return fluidKeyLookup.computeIfAbsent(id, yes -> {
            Optional<Fluid> a = Registry.FLUID.getOrEmpty(id);
            if (a.isPresent()) return FluidKeys.get(a.get());
            return null;
        });
    }

    private static class LBAFluidInsertable implements FluidInsertable {
        private final alexiil.mc.lib.attributes.fluid.FluidInsertable insertable;

        public LBAFluidInsertable(alexiil.mc.lib.attributes.fluid.FluidInsertable insertable) {
            this.insertable = insertable;
        }

        @Override
        public long insertFluid(long amount, Identifier fluidkey, boolean simulation) {
            FluidAmount a = U_AMOUNT.mul(amount);
            FluidKey lbaKey = getLBAFluidKey(fluidkey);
            if (lbaKey == null) return amount;
            FluidVolume b = insertable.attemptInsertion(lbaKey.withAmount(a), Simulation.SIMULATE);
            long c = amount - b.amount().asLong(FluidConstants.BUCKET, RoundingMode.DOWN);
            FluidAmount d = insertable.attemptInsertion(lbaKey.withAmount(U_AMOUNT.mul(c)), Simulation.SIMULATE).amount();
            System.out.println(d.asLong(FluidConstants.BUCKET, RoundingMode.DOWN));
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
