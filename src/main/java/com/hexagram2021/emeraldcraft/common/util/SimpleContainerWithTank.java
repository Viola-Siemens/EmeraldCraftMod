package com.hexagram2021.emeraldcraft.common.util;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.blocks.entity.BaseBlockEntityItemStorage;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ISynchronizableContainer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class SimpleContainerWithTank extends SimpleContainer implements Tank, ISynchronizableContainer {
    private final SingleFluidStorage[] fluidTanks;
    private final FluidStack[] fluidStacks;
    private final SingleItemStorage itemStorage = new BaseBlockEntityItemStorage();

    public SimpleContainerWithTank(int containerSize, long... tankCapacities) {
        super(containerSize);
        this.fluidTanks = new SingleFluidStorage[tankCapacities.length];
        this.fluidStacks = new FluidStack[tankCapacities.length];
        for (int i = 0; i < tankCapacities.length; ++i) {
            this.fluidTanks[i] = SingleFluidStorage.withFixedCapacity(tankCapacities[i], () -> {
            });
            this.fluidStacks[i] = FluidStack.EMPTY;
        }
    }

    @Override
    public FluidStack getFluidStack(int tank) {
        this.fluidStacks[tank].setFluidVariant(this.fluidTanks[tank].variant);
        this.fluidStacks[tank].setAmount(this.fluidTanks[tank].amount);
        return this.fluidStacks[tank].copy();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public void clearDirty() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void setFluidStack(int tank, FluidStack stack) {
        this.fluidTanks[tank].variant = stack.getFluidVariant();
        this.fluidTanks[tank].amount = stack.getAmount();
        this.fluidStacks[tank] = stack;
    }

    @Override
    public ClientboundFluidSyncPacket getSyncPacket() {
        throw new UnsupportedOperationException("Trying to synchronize a client-side container.");
    }

    @Override
    public int getTankSize() {
        return this.fluidTanks.length;
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage(int tank) {
        return this.fluidTanks[tank];
    }

    @Override
    public SingleVariantStorage<ItemVariant> getItemStorage(@NotNull Direction direction) {
        return this.itemStorage;
    }

    @Override
    public int getTanks() {
        return getTankSize();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluidStack(tank);
    }
}
