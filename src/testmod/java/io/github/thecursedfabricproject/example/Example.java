package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Example implements ModInitializer {

	public static BlockEntityType<BadTankBlockEntity> BAD_BLOCK_ENTITY;
	public static BadTank BAD_TANK = new BadTank(Settings.copy(Blocks.SAND));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("e", "measuring_stick"), new MeasuringStick(new Item.Settings()));
		Registry.register(Registry.ITEM, new Identifier("e", "yeet_stick"), new YeetStick(new Item.Settings()));
		Registry.register(Registry.ITEM, new Identifier("e", "water_shover"), new WaterShover(new Item.Settings()));
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			FluidView fluidView = FluidApiKeys.ITEM_FLUID_VIEW.get(stack, null);
			if (fluidView == null) return;
			lines.add(new LiteralText(fluidView.getFluidKey().toString()));
			lines.add(new LiteralText(String.valueOf(fluidView.getFluidAmount())));
		});

		Registry.register(Registry.BLOCK, new Identifier("e", "bad"), BAD_TANK);
		BAD_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "e:very_bad", BlockEntityType.Builder.create(BadTankBlockEntity::new, BAD_TANK).build(null));

		FluidApiKeys.BLOCK_FLUID_VIEW.registerForBlockEntities((blockEntity, context) -> (BadTankBlockEntity) blockEntity, BAD_BLOCK_ENTITY);
		FluidApiKeys.SIDED_FLUID_EXTRACTABLE.registerForBlockEntities((blockEntity, context) -> (BadTankBlockEntity) blockEntity, BAD_BLOCK_ENTITY);
		FluidApiKeys.SIDED_FLUID_INSERTABLE.registerForBlockEntities((blockEntity, context) -> (BadTankBlockEntity) blockEntity, BAD_BLOCK_ENTITY);
	}
    
}
