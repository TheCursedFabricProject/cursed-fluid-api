package io.github.thecursedfabricproject.cursedfluidapi;

import java.util.Collections;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Identifier;

public interface FluidView {
    public Identifier getFluidKey();
    public long getFluidamount();
    public default List<Pair<Identifier, Long>> getAllFluids() {
        return Collections.singletonList(new Pair<>(getFluidKey(), getFluidamount()));
    }
}
