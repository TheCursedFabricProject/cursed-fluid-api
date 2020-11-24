package io.github.thecursedfabricproject.cursedfluidapi;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface ItemFluidInsertable {
    /**
     * @return The new itemstack and fluid not inserted; Null is valid to return if no fluid was inserted
     */
    public @Nullable Pair<ItemStack, Long> insertFluid(long amount, Identifier fluidkey);
}
