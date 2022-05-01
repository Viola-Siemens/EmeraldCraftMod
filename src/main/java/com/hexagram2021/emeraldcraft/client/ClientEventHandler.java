package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyEntity;
import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyModel;
import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyRenderer;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;


public class ClientEventHandler implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		EmeraldCraft.proxy.clearRenderCaches();
	}
}
