package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.fluid.Fluid;

public interface FluidExtractable {
    public Fluid getFluid();
    public long extractFluidAmount(long maxamount, boolean simulation);
}
