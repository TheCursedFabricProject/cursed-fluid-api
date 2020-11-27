package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.fluid.Fluid;

public interface FluidInsertable {
    public long insertFluid(long amount, Fluid fluid, Simulation simulation);
}
