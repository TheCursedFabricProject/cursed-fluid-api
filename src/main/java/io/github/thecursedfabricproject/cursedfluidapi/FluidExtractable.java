package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface FluidExtractable {
    public Identifier getFluidKey();
    public long extractFluidAmount(long maxamount, Identifier fluidKey, boolean simulation);
}
