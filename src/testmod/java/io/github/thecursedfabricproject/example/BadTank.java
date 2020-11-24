package io.github.thecursedfabricproject.example;

import com.mojang.datafixers.util.Pair;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.ItemFluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.ItemFluidInsertable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
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
        ItemStack simulation_stack = handItemStack.copy();
        simulation_stack.setCount(1);
        ItemFluidExtractable extractable = FluidApiKeys.ITEM_FLUID_EXTRACTABLE.get(simulation_stack, null);
        if (extractable != null) {
            Identifier fluidKey = extractable.getFluidKey();
            if (e1.getFluidKey().equals(Registry.FLUID.getId(Fluids.EMPTY)) || fluidKey.equals(e1.getFluidKey())) {
                Pair<ItemStack, Long> extracted = extractable.extractFluidAmount(FluidConstants.BUCKET - e1.getFluidAmount());
                if (extracted != null) {
                    long extracted_fluid = extracted.getSecond();
                    ItemStack post_extract_stack = extracted.getFirst();
                    if (extracted_fluid != 0l) {
                        if (e1.insertFluid(extracted_fluid, fluidKey, true) ==  0l) {
                            e1.insertFluid(extracted_fluid, fluidKey, false);
                            handItemStack.decrement(1);
                            player.inventory.offerOrDrop(world, post_extract_stack);
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }
    
}
