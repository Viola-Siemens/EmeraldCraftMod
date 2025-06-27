package com.hexagram2021.emeraldcraft.common.blocks.entity;

import net.neoforged.neoforge.fluids.FluidStack;

public interface Tank {
	FluidStack getFluidStack(int tank);
	void setFluidStack(int tank, FluidStack stack);
	int getTankSize();
}
