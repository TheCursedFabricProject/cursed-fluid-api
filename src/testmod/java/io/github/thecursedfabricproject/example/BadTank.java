package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInteractionContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BadTank extends Block implements BlockEntityProvider {

    public BadTank(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BadTankBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;
        BlockEntity e = world.getBlockEntity(pos);
        if (!(e instanceof BadTankBlockEntity)) return ActionResult.PASS;
        ItemStack handItemStack = player.getStackInHand(hand);
        BadTankBlockEntity e1 = (BadTankBlockEntity) e;
        FluidInteractionContext context = new FluidInteractionContext(handItemStack);
        FluidExtractable extractable = FluidApiKeys.ITEM_FLUID_EXTRACTABLE.get(handItemStack, context);
        if (extractable != null) {
            if (extractable.getSlotCount() > 0) {
                Fluid fluid = extractable.getFluid(0);
                if (e1.getFluid(0).equals(Fluids.EMPTY) || fluid.equals(e1.getFluid(0))) {
                    long extracted = extractable.extractFluidAmount(FluidConstants.BUCKET - e1.getFluidAmount(0), fluid, true);
                    if (e1.insertFluid(extracted, fluid, true) == 0l) {
                        extractable.extractFluidAmount(FluidConstants.BUCKET - e1.getFluidAmount(0), fluid, false);
                        e1.insertFluid(extracted, fluid, false);
                    }
                    if (context.mainStackModified()) {
                        player.setStackInHand(hand, context.getMainStack());
                        for (ItemStack stack : context.getExtraStacks()) {
                            player.inventory.offerOrDrop(world, stack);
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }
    
}
