package io.github.thecursedfabricproject.cursedfluidapi;

import org.jetbrains.annotations.NotNull;

import io.github.thecursedfabricproject.cursedfluidapi.internal.EnhancedItemApiLookup;
import net.fabricmc.fabric.api.provider.v1.ContextKey;
import net.fabricmc.fabric.api.provider.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.provider.v1.block.BlockApiLookupRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class FluidApiKeys {
	public static final Identifier FLUID_INSERTABLE_ID = new Identifier("cursed-fluid-api", "fluid_insertable");
	public static final Identifier FLUID_EXTRACTABLE_ID = new Identifier("cursed-fluid-api", "fluid_extractable");
	public static final Identifier FLUID_VIEW_ID = new Identifier("cursed-fluid-api", "fluid_view");
	public static final ContextKey<Direction> SIDED = ContextKey.of(Direction.class, new Identifier("c", "sided"));
	public static final BlockApiLookup<FluidInsertable, @NotNull Direction> SIDED_FLUID_INSERTABLE = BlockApiLookupRegistry.getLookup(FLUID_INSERTABLE_ID, SIDED);
	public static final BlockApiLookup<FluidExtractable, @NotNull Direction> SIDED_FLUID_EXTRACTABLE = BlockApiLookupRegistry.getLookup(FLUID_EXTRACTABLE_ID, SIDED);
	public static final BlockApiLookup<FluidView, Void> BLOCK_FLUID_VIEW = BlockApiLookupRegistry.getLookup(FLUID_VIEW_ID, ContextKey.NO_CONTEXT);
	public static final ContextKey<FluidInteractionContext> FLUID_INTERACTON_CONTEXT_KEY = ContextKey.of(FluidInteractionContext.class, new Identifier("cursed-fluid-api", "fluid_interaction_context"));
	public static final EnhancedItemApiLookup<FluidInsertable, FluidInteractionContext> ITEM_FLUID_INSERTABLE = new EnhancedItemApiLookup<>(FLUID_INSERTABLE_ID, FLUID_INTERACTON_CONTEXT_KEY);
	public static final EnhancedItemApiLookup<FluidExtractable, FluidInteractionContext> ITEM_FLUID_EXTRACTABLE = new EnhancedItemApiLookup<>(FLUID_EXTRACTABLE_ID, FLUID_INTERACTON_CONTEXT_KEY);
	public static final EnhancedItemApiLookup<FluidView, Void> ITEM_FLUID_VIEW = new EnhancedItemApiLookup<>(FLUID_VIEW_ID, ContextKey.NO_CONTEXT);

	private FluidApiKeys(){}
}
