package com.hexagram2021.emeraldcraft.network;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.client.screens.ScreenUtils;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public abstract class ClientboundFluidSyncPacket implements IECPacket {
    private final String containerType;
    private final List<FluidStack> fluidStacks;

    public ClientboundFluidSyncPacket(String containerType, List<FluidStack> fluidStacks) {
        this.containerType = containerType;
        this.fluidStacks = fluidStacks;
    }

    public ClientboundFluidSyncPacket(FriendlyByteBuf buf) {
        this.containerType = buf.readUtf();
        this.fluidStacks = buf.readCollection(Lists::newArrayListWithCapacity, FluidStack::readFromPacket);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.containerType);
        buf.writeCollection(this.fluidStacks, (buf1, fluidStack) -> fluidStack.writeToPacket(buf1));
    }

    @Override
    public void handle() {
        ScreenUtils.handleFluidSyncPacket(containerType, this.fluidStacks);
    }
}
