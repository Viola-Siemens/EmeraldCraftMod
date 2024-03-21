package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraftforge.fluids.FluidStack;

public interface ISynchronizableContainer {
	void markDirty();
	void clearDirty();
	boolean isDirty();
	void setFluidStack(int tank, FluidStack stack);

	ClientboundFluidSyncPacket getSyncPacket();
}
