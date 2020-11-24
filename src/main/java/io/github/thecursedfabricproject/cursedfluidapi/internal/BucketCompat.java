package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.mixin.BucketItemAccess;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BucketCompat {
    private BucketCompat() {
    }

    public static void init() {
        FluidApiKeys.ITEM_FLUID_VIEW.registerFallback((stack, context) -> new FluidView() {

            @Override
            public Identifier getFluidKey() {
                return Registry.FLUID.getId(((BucketItemAccess)stack.getItem()).getFluid());
            }

            @Override
            public long getFluidAmount() {
                return getFluidKey() == Registry.FLUID.getId(Fluids.EMPTY) ? 0 : FluidConstants.BUCKET;
            }
                
            },
            stack -> stack.getItem() instanceof BucketItem
        );
    }
}
