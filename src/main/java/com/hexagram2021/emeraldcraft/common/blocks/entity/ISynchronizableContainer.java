package com.hexagram2021.emeraldcraft.common.blocks.entity;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;

public interface ISynchronizableContainer {
    void markDirty();

    void clearDirty();

    boolean isDirty();

    void setFluidStack(int tank, FluidStack stack);

    ClientboundFluidSyncPacket getSyncPacket();
}
