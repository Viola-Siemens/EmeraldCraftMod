package com.hexagram2021.emeraldcraft.network;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.client.screens.ScreenUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ClientboundFluidSyncPacket implements IECPacket {
	private final List<FluidStack> fluidStacks;

	public ClientboundFluidSyncPacket(List<FluidStack> fluidStacks) {
		this.fluidStacks = fluidStacks;
	}

	public ClientboundFluidSyncPacket(FriendlyByteBuf buf) {
		this.fluidStacks = buf.readCollection(Lists::newArrayListWithCapacity, FluidStack::readFromPacket);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeCollection(this.fluidStacks, (buf1, fluidStack) -> fluidStack.writeToPacket(buf1));
	}

	@Override
	public void handle() {
		ScreenUtils.handleFluidSyncPacket(this.fluidStacks);
	}
}
