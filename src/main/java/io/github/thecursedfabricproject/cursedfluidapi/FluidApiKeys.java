package io.github.thecursedfabricproject.cursedfluidapi;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.provider.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.provider.v1.block.BlockApiLookupRegistry;
import net.fabricmc.fabric.api.provider.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.provider.v1.item.ItemApiLookupRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class FluidApiKeys {
	public static final Identifier FLUID_INSERTABLE_ID = new Identifier("cursed-fluid-api", "fluid_insertable");
	public static final Identifier FLUID_EXTRACTABLE_ID = new Identifier("cursed-fluid-api", "fluid_extractable");
	public static final Identifier FLUID_VIEW_ID = new Identifier("cursed-fluid-api", "fluid_view");
	public static final BlockApiLookup<FluidView, @NotNull Direction> SIDED_FLUID_IO = BlockApiLookupRegistry.getLookup(FLUID_INSERTABLE_ID, FluidView.class, Direction.class);
	public static final ItemApiLookup<FluidInsertable, FluidInteractionContext> ITEM_FLUID_INSERTABLE = ItemApiLookupRegistry.getLookup(FLUID_INSERTABLE_ID, FluidInsertable.class, FluidInteractionContext.class);
	public static final ItemApiLookup<FluidExtractable, FluidInteractionContext> ITEM_FLUID_EXTRACTABLE = ItemApiLookupRegistry.getLookup(FLUID_EXTRACTABLE_ID, FluidExtractable.class, FluidInteractionContext.class);
	public static final ItemApiLookup<FluidView, Void> ITEM_FLUID_VIEW = ItemApiLookupRegistry.getLookup(FLUID_VIEW_ID, FluidView.class, Void.class);

	private FluidApiKeys(){}
}
