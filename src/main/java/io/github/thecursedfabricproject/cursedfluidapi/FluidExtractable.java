package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.fluid.Fluid;

public interface FluidExtractable extends FluidView {
    public long extractFluidAmount(long maxamount, Fluid fluid, Simulation simulation);
}
