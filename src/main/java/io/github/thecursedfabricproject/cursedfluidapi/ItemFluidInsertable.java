package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface ItemFluidInsertable {
    public long insertFluid(long amount, Identifier fluidkey);
}
