package io.github.thecursedfabricproject.example;

import io.github.cursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.cursedfabricproject.cursedfluidapi.FluidView;
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
            System.out.println(view.getFluidAmmount());
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
