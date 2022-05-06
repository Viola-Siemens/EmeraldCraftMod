package com.hexagram2021.emeraldcraft.client;


import com.hexagram2021.emeraldcraft.client.models.entities.*;
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
		event.registerLayerDefinition(NetherPigmanModel.LAYER_LOCATION, NetherPigmanModel::createBodyLayer);
		event.registerLayerDefinition(NetherLambmanModel.LAYER_LOCATION, NetherLambmanModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_PIGMAN.get(), NetherPigmanRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_LAMBMAN.get(), NetherLambmanRenderer::new);
	}

	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyEntity.createAttributes().build());
		event.put(ECEntities.NETHER_PIGMAN.get(), NetherPigmanEntity.createAttributes().build());
		event.put(ECEntities.NETHER_LAMBMAN.get(), NetherLambmanEntity.createAttributes().build());
	}
}
