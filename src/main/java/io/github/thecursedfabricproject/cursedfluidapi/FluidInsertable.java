package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface FluidInsertable {
    public long insertFluid(long amount, Identifier fluidkey, boolean simulation);
}
