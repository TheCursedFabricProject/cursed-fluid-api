package io.github.thecursedfabricproject.cursedfluidapi;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface ItemFluidExtractable {
    public Identifier getFluidKey();
    public @Nullable Pair<ItemStack, Long> extractFluidAmount(long maxamount, Identifier fluidKey);
}
