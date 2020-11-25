package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
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
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> new FluidView() {

            @Override
            public Fluid getFluid() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? Fluids.WATER : Fluids.EMPTY;
            }

            @Override
            public long getFluidAmount() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? FluidConstants.BOTTLE : 0;
            }

        }, stack -> stack.getItem() instanceof PotionItem);

        FluidApiKeys.ITEM_FLUID_EXTRACTABLE.registerFallback((stack, context) -> new FluidExtractable() {

            @Override
            public Fluid getFluid() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? Fluids.WATER : Fluids.EMPTY;
            }

            @Override
            public long extractFluidAmount(long maxamount, boolean simulation) {
                if (PotionUtil.getPotion(stack) != Potions.WATER)
                    return 0;
                if (maxamount < FluidConstants.BOTTLE)
                    return 0;
                if (!simulation) context.getMainStack().decrement(1);
                if (!simulation) context.addExtraStack(new ItemStack(Items.GLASS_BOTTLE));
                return FluidConstants.BOTTLE;
            }

        }, stack -> stack.getItem() instanceof PotionItem);

        FluidApiKeys.ITEM_FLUID_INSERTABLE.register((stack, context) -> new FluidInsertable() {

            @Override
            public long insertFluid(long amount, Fluid fluid, boolean simulation) {
                if (amount < FluidConstants.BOTTLE) return 0;
                if (fluid != Fluids.WATER) return 0;
                if (!simulation) context.getMainStack().decrement(1);
                if (!simulation) context.addExtraStack(new ItemStack(Items.POTION));
                return amount - FluidConstants.BOTTLE;
            }
            
        }, Items.GLASS_BOTTLE);
    }
}
