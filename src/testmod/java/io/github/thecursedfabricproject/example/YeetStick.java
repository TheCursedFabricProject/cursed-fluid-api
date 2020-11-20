package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

/**
 * Yeets up to 2/3s of a bucket from a block on right click
 */
public class YeetStick extends Item {

    public YeetStick(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        FluidExtractable extractable = FluidApiKeys.SIDED_FLUID_EXTRACTABLE.get(context.getWorld(), context.getBlockPos(), context.getSide());
        if (extractable != null && !context.getWorld().isClient) {
            Identifier fluidKey = extractable.getFluidKey();
            long extractedAmmount = extractable.extractFluidAmount(FluidConstants.BOTTLE * 2, fluidKey, false);
            if (extractedAmmount > 0) {
                System.out.printf("Extracted %d %s%n", extractedAmmount, fluidKey.toString());
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
    
}
