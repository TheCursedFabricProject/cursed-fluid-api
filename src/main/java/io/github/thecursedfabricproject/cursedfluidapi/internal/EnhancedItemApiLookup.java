package io.github.thecursedfabricproject.cursedfluidapi.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mojang.datafixers.util.Pair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.provider.v1.ApiProviderMap;
import net.fabricmc.fabric.api.provider.v1.ContextKey;
import net.fabricmc.fabric.api.provider.v1.item.ItemApiLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnhancedItemApiLookup<T, C> implements ItemApiLookup<T, C> {
	private static final Logger LOGGER = LogManager.getLogger();
    private final ApiProviderMap<Item, ItemApiProvider<?, ?>> providerMap = ApiProviderMap.create();
    private final List<Pair<FallbackProviderPredicate, ItemApiProvider<?, ?>>> fallbackProviders = new ArrayList<>();
	private final Identifier id;
	private final ContextKey<C> contextKey;

	public EnhancedItemApiLookup(Identifier apiId, ContextKey<C> contextKey) {
		this.id = apiId;
		this.contextKey = contextKey;
	}

	@Nullable
	@Override
	public T get(ItemStack stack, C context) {
		Objects.requireNonNull(stack, "World cannot be null");
		// Providers have the final say whether a null context is allowed.

		@SuppressWarnings("unchecked")
		@Nullable
        ItemApiProvider<T, C> provider = (ItemApiProvider<T, C>) providerMap.get(stack.getItem());
        
        if (provider == null) {
            Pair<FallbackProviderPredicate, ItemApiProvider<?, ?>> entry;
            for (int i = fallbackProviders.size() - 1; i >= 0; i--) {
                entry = fallbackProviders.get(i);
                if (entry.getFirst().hasApiProvider(stack)) {
                    provider = (ItemApiProvider<T, C>) entry.getSecond();
                    if (provider != null) break;
                }
            }
        }

		if (provider != null) {
			return provider.get(stack, context);
		}

		return null;
	}

	@Override
	public void register(ItemApiProvider<T, C> provider, ItemConvertible... items) {
		Objects.requireNonNull(provider);

		for (ItemConvertible item : items) {
			Objects.requireNonNull(item, "Passed item convertible cannot be null");
			Objects.requireNonNull(item.asItem(), "Item convertible in item form cannot be null: " + item.toString());

			if (providerMap.putIfAbsent(item.asItem(), provider) != null) {
				LOGGER.warn("Encountered duplicate API provider registration for item: " + Registry.ITEM.getId(item.asItem()));
			}
		}
    }
    
    public void registerFallback(ItemApiProvider<T, C> provider, FallbackProviderPredicate predicate) {
		Objects.requireNonNull(provider);
        Objects.requireNonNull(predicate, "Passed predicate cannot be null");

        fallbackProviders.add(new Pair<>(predicate, provider));
	}

	@Override
	public Identifier getApiId() {
		return this.id;
	}

	@Override
	public ContextKey<C> getContextKey() {
		return this.contextKey;
    }
    
    @FunctionalInterface
    public static interface FallbackProviderPredicate {
        public boolean hasApiProvider(ItemStack stack);
    }
}
