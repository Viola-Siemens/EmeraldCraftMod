package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ClientEventHandler implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		EmeraldCraft.proxy.clearRenderCaches();
	}
}
