package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.Simulation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BadTankBlockEntity extends BlockEntity implements FluidInsertable, FluidExtractable {
    public BadTankBlockEntity() {
        super(Example.BAD_BLOCK_ENTITY);
    }

    private Fluid fluid = Fluids.EMPTY;
    private long stored_amount = 0;
    private int version = Integer.MIN_VALUE;

    @Override
    public long getFluidAmount(int slot) {
        return stored_amount;
    }

    @Override
    public Fluid getFluid(int slot) {
        return fluid;
    }

    @Override
    public long extractFluidAmount(long maxamount, Fluid fluid, Simulation simulation) {
        if (fluid != this.fluid) return 0;
        long result = Math.min(stored_amount, maxamount);
        if (simulation.isAct()) {
            version++;
            stored_amount -= result;
            if (stored_amount == 0) this.fluid = Fluids.EMPTY;
        }
        return result;
    }

    @Override
    public long insertFluid(long amount, Fluid fluid2, Simulation simulation) {
        if (!(fluid.equals(Fluids.EMPTY) || fluid.equals(fluid2))) return amount;
        if (simulation.isAct()) fluid = fluid2;
        long result = Math.max(amount - (FluidConstants.BUCKET - stored_amount), 0);
        long inserted = amount - result;
        if (simulation.isAct()) stored_amount += inserted;
        if (simulation.isAct()) version++;
        return result;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putString("f", Registry.FLUID.getId(fluid).toString());
        tag.putLong("a", stored_amount);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        fluid = Registry.FLUID.get(new Identifier(tag.getString("f")));
        stored_amount = tag.getLong("a");
    }

    @Override
    public int getFluidSlotCount() {
        return 1;
    }

    @Override
    public int getVersion() {
        return version;
    }
}
