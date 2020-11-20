package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public class WaterShover extends Item {

    public WaterShover(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        FluidInsertable insertable = FluidApiKeys.SIDED_FLUID_INSERTABLE.get(context.getWorld(), context.getBlockPos(), context.getSide());
        if (insertable != null && !context.getWorld().isClient) {
            long leftover = insertable.insertFluid(FluidConstants.BOTTLE + 3000, Registry.FLUID.getId(Fluids.WATER), false);
            System.out.printf("Tried to insert 30,000 U of Water, %d could not be inserted.%n", leftover);
        }
        return ActionResult.PASS;
    }
    
}
