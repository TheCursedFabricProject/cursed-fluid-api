package io.github.thecursedfabricproject.cursedfluidapi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public final class FluidInteractionContext {
    private ArrayList<ItemStack> extraStacks = new ArrayList<>();
    private ItemStack mainStack;
    private boolean mainStackModified = false;

    public FluidInteractionContext(ItemStack stack) {
        mainStack = stack;
    }

    public ItemStack getMainStack() {
        return mainStack;
    }

    public void setMainStack(ItemStack stack) {
        this.mainStackModified = true;
        this.mainStack = stack;
    }

    public boolean mainStackModified() {
        return mainStackModified;
    }

    public void addExtraStack(ItemStack stack) {
        extraStacks.add(stack);
    }

    public List<ItemStack> getExtraStacks() {
        return extraStacks;
    }
}
