package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidIO;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class WaterShover extends Item {

    public WaterShover(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        FluidIO fluidIO = FluidApiKeys.SIDED_FLUID_IO.get(context.getWorld(), context.getBlockPos(), context.getSide());
        if (fluidIO instanceof FluidInsertable && !context.getWorld().isClient) {
            FluidInsertable insertable = (FluidInsertable) fluidIO;
            long leftover = insertable.insertFluid(FluidConstants.BOTTLE + 3000, Fluids.WATER, false);
            System.out.printf("Tried to insert 30,000 U of Water, %d could not be inserted.%n", leftover);
        }
        return ActionResult.PASS;
    }
    
}
