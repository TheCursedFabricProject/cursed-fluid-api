package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import io.github.thecursedfabricproject.cursedfluidapi.Simulation;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

/**
 * Yeets up to 2/3s of a bucket from a block on right click
 */
public class YeetStick extends Item {

    public YeetStick(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        FluidView fluidIO = FluidApiKeys.SIDED_FLUID_IO.get(context.getWorld(), context.getBlockPos(), context.getSide());
        if (fluidIO instanceof FluidExtractable && !context.getWorld().isClient) {
            FluidExtractable extractable = (FluidExtractable) fluidIO;
            if (extractable.getFluidSlotCount() > 0) {
                Fluid fluid = extractable.getFluid(0);
                long extractedAmmount = extractable.extractFluidAmount(FluidConstants.BOTTLE * 2, fluid, Simulation.ACT);
                if (extractedAmmount > 0) {
                    System.out.printf("Extracted %d %s%n", extractedAmmount, Registry.FLUID.getId(fluid).toString());
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
    
}
