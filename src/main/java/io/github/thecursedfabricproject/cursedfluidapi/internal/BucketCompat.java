package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.mixin.BucketItemAccess;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BucketCompat {
    private BucketCompat() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> {
            if (!(stack.getItem() instanceof BucketItem)) return null;
            return new FluidView() {
                @Override
                public int getSlotCount() {
                    return 1;
                }

                @Override
                public Fluid getFluid(int slot) {
                    return ((BucketItemAccess) stack.getItem()).getFluid();
                }
    
                @Override
                public long getFluidAmount(int slot) {
                    return getFluid(0) == Fluids.EMPTY ? 0 : FluidConstants.BUCKET;
                }
    
            };
        });

        FluidApiKeys.ITEM_FLUID_INSERTABLE.register((stack, context) -> new FluidInsertable() {

            @Override
            public long insertFluid(long amount, Fluid fluid, boolean simulation) {
                if (amount < FluidConstants.BUCKET) return amount;
                if (!simulation) context.getMainStack().decrement(1);
                if (!simulation) context.addExtraStack(new ItemStack(fluid.getBucketItem()));
                return amount - FluidConstants.BUCKET;
            }
            
        }, Items.BUCKET);
    }
}
