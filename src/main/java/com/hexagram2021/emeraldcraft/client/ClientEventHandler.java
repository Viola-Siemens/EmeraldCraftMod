package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

public class ClientEventHandler implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
		EmeraldCraft.proxy.clearRenderCaches();
	}
}
