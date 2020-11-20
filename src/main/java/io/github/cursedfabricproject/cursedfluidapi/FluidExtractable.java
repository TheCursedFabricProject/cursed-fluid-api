package io.github.cursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface FluidExtractable {
    public Identifier getFluidKey();
    public long getFluidAmmount(long maxammount, Identifier fluidKey, boolean simulation);
}
