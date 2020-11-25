package io.github.thecursedfabricproject.cursedfluidapi;

import java.util.Collections;
import java.util.List;
import com.mojang.datafixers.util.Pair;

import net.minecraft.fluid.Fluid;

public interface FluidView {
    public Fluid getFluid();
    public long getFluidAmount();
    public default List<Pair<Fluid, Long>> getAllFluids() {
        return Collections.singletonList(new Pair<>(getFluid(), getFluidAmount()));
    }
}
