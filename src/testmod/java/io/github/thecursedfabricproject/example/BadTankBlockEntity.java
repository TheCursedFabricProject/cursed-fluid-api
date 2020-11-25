package io.github.thecursedfabricproject.example;

import io.github.thecursedfabricproject.cursedfluidapi.FluidConstants;
import io.github.thecursedfabricproject.cursedfluidapi.FluidExtractable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidInsertable;
import io.github.thecursedfabricproject.cursedfluidapi.FluidView;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BadTankBlockEntity extends BlockEntity implements FluidInsertable, FluidExtractable, FluidView {
    public BadTankBlockEntity() {
        super(Example.BAD_BLOCK_ENTITY);
    }

    private Fluid fluid = Fluids.EMPTY;
    private long stored_amount = 0;

    @Override
    public long getFluidAmount() {
        return stored_amount;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public long extractFluidAmount(long maxamount, boolean simulation) {
        long result = Math.min(stored_amount, maxamount);
        if (!simulation) {
            stored_amount -= result;
            if (stored_amount == 0) fluid = Fluids.EMPTY;
        }
        return result;
    }

    @Override
    public long insertFluid(long amount, Fluid fluid2, boolean simulation) {
        if (!(fluid.equals(Fluids.EMPTY) || fluid.equals(fluid2))) return amount;
        if (!simulation) fluid = fluid2;
        long result = Math.max(amount - (FluidConstants.BUCKET - stored_amount), 0);
        long inserted = amount - result;
        if (!simulation) stored_amount += inserted;
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
}
