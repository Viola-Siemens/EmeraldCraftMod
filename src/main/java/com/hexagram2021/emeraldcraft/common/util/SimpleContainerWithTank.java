package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ISynchronizableContainer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class SimpleContainerWithTank extends SimpleContainer implements Tank, ISynchronizableContainer {
	private final FluidTank[] fluidTanks;

	public SimpleContainerWithTank(int containerSize, int... tankCapacities) {
		super(containerSize);
		this.fluidTanks = new FluidTank[tankCapacities.length];
		for(int i = 0; i < tankCapacities.length; ++i) {
			this.fluidTanks[i] = new FluidTank(tankCapacities[i]);
		}
	}

	@Override
	public FluidStack getFluidStack(int tank) {
		return this.fluidTanks[tank].getFluid();
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
		this.fluidTanks[tank].setFluid(stack);
	}

	@Override
	public ClientboundFluidSyncPacket getSyncPacket() {
		throw new UnsupportedOperationException("Trying to synchronize a client-side container.");
	}

	@Override
	public int getTankSize() {
		return this.fluidTanks.length;
	}
}
