package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.fluid.Fluid;

public interface FluidView {
    int getSlotCount();

    public Fluid getFluid(int slot);
    
    long getFluidAmount(int slot);
}
