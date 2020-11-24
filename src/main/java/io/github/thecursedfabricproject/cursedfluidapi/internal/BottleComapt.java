package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BottleComapt {
    private BottleComapt() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> new FluidView() {

            @Override
            public Identifier getFluidKey() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? Registry.FLUID.getId(Fluids.WATER) : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidAmount() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? FluidConstants.BOTTLE : 0;
            }
            
        }, stack -> stack.getItem() instanceof PotionItem);
    }
}
