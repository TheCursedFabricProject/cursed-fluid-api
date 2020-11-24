package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidApiKeys;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Example implements ModInitializer {

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
	}
    
}
