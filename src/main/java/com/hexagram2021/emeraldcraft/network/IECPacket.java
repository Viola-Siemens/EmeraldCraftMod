package com.hexagram2021.emeraldcraft.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IECPacket {
	void write(FriendlyByteBuf buf);
	void handle();
}
