package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class MeasuringStick extends Item {

    public MeasuringStick(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        FluidView view = FluidApiKeys.BLOCK_FLUID_VIEW.get(context.getWorld(), context.getBlockPos(), null);
        if (view != null && !context.getWorld().isClient) {
            System.out.println(view.getFluidKey());
            System.out.println(view.getFluidAmount());
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
