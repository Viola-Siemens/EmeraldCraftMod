package com.hexagram2021.emeraldcraft.client;


import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyEntity;
import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyModel;
import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyRenderer;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientMobEventSubscriber {
	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PiglinCuteyModel.LAYER_LOCATION, PiglinCuteyModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyRenderer::new);
	}

	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyEntity.createAttributes().build());
	}
}
