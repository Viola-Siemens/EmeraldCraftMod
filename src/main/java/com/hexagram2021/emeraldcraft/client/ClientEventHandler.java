package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ClientEventHandler implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EmeraldCraft.proxy.clearRenderCaches();
    }

    @Override
    public ResourceLocation getFabricId() {
        return EmeraldCraft.id("client_event_handler");
    }
}
