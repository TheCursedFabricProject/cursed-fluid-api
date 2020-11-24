package io.github.thecursedfabricproject.cursedfluidapi.internal;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.ItemFluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.mixin.BucketItemAccess;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BucketCompat {
    private BucketCompat() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> new FluidView() {

            @Override
            public Identifier getFluidKey() {
                return Registry.FLUID.getId(((BucketItemAccess) stack.getItem()).getFluid());
            }

            @Override
            public long getFluidAmount() {
                return getFluidKey() == Registry.FLUID.getId(Fluids.EMPTY) ? 0 : FluidConstants.BUCKET;
            }

        }, stack -> stack.getItem() instanceof BucketItem);

        FluidApiKeys.ITEM_FLUID_INSERTABLE.register((stack, context) -> new ItemFluidInsertable() {

            @Override
            public @Nullable Pair<ItemStack, Long> insertFluid(long amount, Identifier fluidkey) {
                if (amount < FluidConstants.BUCKET) return null;
                return new Pair<>(new ItemStack(Registry.FLUID.get(fluidkey).getBucketItem()), amount - FluidConstants.BUCKET);
            }
            
        }, Items.BUCKET);
    }
}
