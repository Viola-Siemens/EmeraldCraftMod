package com.hexagram2021.emeraldcraft.common.blocks.entity;

import net.minecraftforge.fluids.FluidStack;

public interface Tank {
	FluidStack getFluidStack(int tank);
	int getTankSize();
}
