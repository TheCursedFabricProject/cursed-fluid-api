package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.Simulation;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class BottleCompat {
    private BottleCompat() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> {
            if (!(stack.getItem() instanceof PotionItem)) return null;
            return new FluidView() {
                @Override
                public int getVersion() {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? 1 : 0;
                }

                @Override
                public int getFluidSlotCount() {
                    return 1;
                }

                @Override
                public Fluid getFluid(int slot) {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? Fluids.WATER : Fluids.EMPTY;
                }

                @Override
                public long getFluidAmount(int slot) {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? FluidConstants.BOTTLE : 0;
                }

            };
        });

        FluidApiKeys.ITEM_FLUID_EXTRACTABLE.registerFallback((stack, context) -> {
            if (!(stack.getItem() instanceof PotionItem)) return null;
            return new FluidExtractable() {
                @Override
                public int getVersion() {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? 1 : 0;
                }

                @Override
                public int getFluidSlotCount() {
                    return 1;
                }

                @Override
                public long getFluidAmount(int slot) {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? FluidConstants.BOTTLE : 0;
                }

                @Override
                public Fluid getFluid(int slot) {
                    return PotionUtil.getPotion(stack) == Potions.WATER ? Fluids.WATER : Fluids.EMPTY;
                }

                @Override
                public long extractFluidAmount(long maxamount, Fluid fluid, Simulation simulation) {
                    if (PotionUtil.getPotion(stack) != Potions.WATER)
                        return 0;
                    if (maxamount < FluidConstants.BOTTLE)
                        return 0;
                    if (simulation.isAct()) context.getMainStack().decrement(1);
                    if (simulation.isAct()) context.addExtraStack(new ItemStack(Items.GLASS_BOTTLE));
                    return FluidConstants.BOTTLE;
                }
            };
        });

        FluidApiKeys.ITEM_FLUID_INSERTABLE.register((stack, context) -> new FluidInsertable() {

            @Override
            public long insertFluid(long amount, Fluid fluid, Simulation simulation) {
                if (amount < FluidConstants.BOTTLE) return 0;
                if (fluid != Fluids.WATER) return 0;
                if (simulation.isAct()) context.getMainStack().decrement(1);
                if (simulation.isAct()) context.addExtraStack(new ItemStack(Items.POTION));
                return amount - FluidConstants.BOTTLE;
            }
            
        }, Items.GLASS_BOTTLE);
    }
}
