package io.github.thecursedfabricproject.cursedfluidapi.internal;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.ItemFluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.ItemFluidInsertable;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BottleCompat {
    private BottleCompat() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> new FluidView() {

            @Override
            public Identifier getFluidKey() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? Registry.FLUID.getId(Fluids.WATER)
                        : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public long getFluidAmount() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? FluidConstants.BOTTLE : 0;
            }

        }, stack -> stack.getItem() instanceof PotionItem);

        FluidApiKeys.ITEM_FLUID_EXTRACTABLE.registerFallback((stack, context) -> new ItemFluidExtractable() {

            @Override
            public Identifier getFluidKey() {
                return PotionUtil.getPotion(stack) == Potions.WATER ? Registry.FLUID.getId(Fluids.WATER)
                        : Registry.FLUID.getId(Fluids.EMPTY);
            }

            @Override
            public Pair<ItemStack, Long> extractFluidAmount(long maxamount, Identifier fluidKey) {
                if (PotionUtil.getPotion(stack) != Potions.WATER)
                    return null;
                if (maxamount < FluidConstants.BOTTLE)
                    return null;
                return new Pair<>(new ItemStack(Items.GLASS_BOTTLE), FluidConstants.BOTTLE);
            }

        }, stack -> stack.getItem() instanceof PotionItem);

        FluidApiKeys.ITEM_FLUID_INSERTABLE.register((stack, context) -> new ItemFluidInsertable() {

            @Override
            public @Nullable Pair<ItemStack, Long> insertFluid(long amount, Identifier fluidkey) {
                if (amount < FluidConstants.BOTTLE) return null;
                if (fluidkey != Registry.FLUID.getId(Fluids.WATER)) return null;
                return new Pair<>(new ItemStack(Items.POTION), amount - FluidConstants.BOTTLE);
            }
            
        }, Items.GLASS_BOTTLE);
    }
}
