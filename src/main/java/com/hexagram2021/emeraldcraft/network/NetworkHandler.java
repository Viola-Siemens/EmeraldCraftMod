package com.hexagram2021.emeraldcraft.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHandler {
    public static void sendMessageToPlayer(IECPacket packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }

    public static void sendMessageToAll(IECPacket packet, MinecraftServer server) {
        PlayerLookup.all(server).forEach(player -> sendMessageToPlayer(packet, player));
    }
}
