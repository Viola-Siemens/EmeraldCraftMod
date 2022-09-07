package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import java.util.function.Predicate;


public class ClientEventHandler implements ISelectiveResourceReloadListener {
	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, @Nonnull Predicate<IResourceType> resourcePredicate) {
		if(resourcePredicate.test(VanillaResourceType.TEXTURES)) {
			EmeraldCraft.proxy.clearRenderCaches();
		}
	}
}
