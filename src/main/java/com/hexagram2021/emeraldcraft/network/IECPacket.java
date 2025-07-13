package com.hexagram2021.emeraldcraft.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;

public interface IECPacket extends FabricPacket {
    void handle();
}
