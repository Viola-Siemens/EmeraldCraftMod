package com.hexagram2021.emeraldcraft.common.crafting;

import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import javax.annotation.Nonnull;

public class RecipeCachingReloadListener implements ResourceManagerReloadListener {
	private final ServerResources dataPackRegistries;

	public RecipeCachingReloadListener(ServerResources dataPackRegistries) {
		this.dataPackRegistries = dataPackRegistries;
	}

	@Override
	public void onResourceManagerReload(@Nonnull ResourceManager resourceManager) {
		RecipeReloadListener.buildRecipeLists(dataPackRegistries.getRecipeManager());
	}
}
