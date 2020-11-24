package io.github.thecursedfabricproject.cursedfluidapi;

import net.minecraft.util.Identifier;

public interface ItemFluidExtractable {
    public Identifier getFluidKey();
    public long extractFluidAmount(long maxamount, Identifier fluidKey);
}
