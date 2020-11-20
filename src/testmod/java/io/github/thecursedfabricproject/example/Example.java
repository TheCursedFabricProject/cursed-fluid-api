package io.github.thecursedfabricproject.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Example implements ModInitializer {

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("e", "measuring_stick"), new MeasuringStick(new Item.Settings()));
		Registry.register(Registry.ITEM, new Identifier("e", "yeet_stick"), new YeetStick(new Item.Settings()));
		Registry.register(Registry.ITEM, new Identifier("e", "water_shover"), new WaterShover(new Item.Settings()));
	}
    
}
