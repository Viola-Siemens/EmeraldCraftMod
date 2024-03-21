package com.hexagram2021.emeraldcraft.common.crafting.menu;

import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public interface IFluidSyncMenu {
	void addUsingPlayer(ServerPlayer serverPlayer);
	void removeUsingPlayer(ServerPlayer serverPlayer);
	ClientboundFluidSyncPacket getSyncPacket();
	Container getContainer();
}
