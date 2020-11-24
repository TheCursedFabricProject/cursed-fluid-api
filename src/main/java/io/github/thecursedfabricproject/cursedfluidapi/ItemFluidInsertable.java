package io.github.thecursedfabricproject.cursedfluidapi;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface ItemFluidInsertable {
    public @Nullable Pair<ItemStack, Long> insertFluid(long amount, Identifier fluidkey);
}
