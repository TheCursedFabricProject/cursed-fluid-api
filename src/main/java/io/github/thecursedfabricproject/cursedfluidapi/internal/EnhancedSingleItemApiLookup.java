package io.github.thecursedfabricproject.cursedfluidapi.internal;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.provider.v1.ContextKey;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class EnhancedSingleItemApiLookup<T, C> extends EnhancedItemApiLookup<T, C> {

    public EnhancedSingleItemApiLookup(Identifier apiId, ContextKey<C> contextKey) {
        super(apiId, contextKey);
    }

    @Override
    public @Nullable T get(ItemStack stack, C context) {
        Objects.requireNonNull(stack, "Stack cannot be null");
        if (stack.getCount() != 1) throw new IllegalArgumentException("Stack: " + stack.toString() + " did not have a count of 1 :(");
        return super.get(stack, context);
    }
    
}
