package io.github.cursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface FluidInsertable {
    public long insertFluid(long ammount, Identifier fluidkey, boolean simulation);
}
