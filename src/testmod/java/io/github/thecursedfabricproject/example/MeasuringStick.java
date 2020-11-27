package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidIO;
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
        FluidIO fluidIO = FluidApiKeys.SIDED_FLUID_IO.get(context.getWorld(), context.getBlockPos(), context.getSide());
        if (fluidIO instanceof FluidView && !context.getWorld().isClient) {
            FluidView view = (FluidView) fluidIO;
            for (int i = 0; i < view.getSlotCount(); i++) {
                System.out.println(view.getFluid(i));
                System.out.println(view.getFluidAmount(i));
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
