package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.*;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.client.model.BoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
		event.registerLayerDefinition(HerringModel.LAYER_LOCATION, HerringModel::createBodyLayer);
		event.registerLayerDefinition(PurpleSpottedBigeyeModel.LAYER_LOCATION, PurpleSpottedBigeyeModel::createBodyLayer);
		event.registerLayerDefinition(MantaModel.LAYER_LOCATION, MantaModel::createBodyLayer);
		event.registerLayerDefinition(WraithModel.LAYER_LOCATION, WraithModel::createBodyLayer);

		for(ECBoat.ECBoatType type: ECBoat.ECBoatType.values()) {
			ForgeHooksClient.registerLayerDefinition(ECBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
		}
	}

	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_PIGMAN.get(), NetherPigmanRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_LAMBMAN.get(), NetherLambmanRenderer::new);
		event.registerEntityRenderer(ECEntities.HERRING.get(), HerringRenderer::new);
		event.registerEntityRenderer(ECEntities.PURPLE_SPOTTED_BIGEYE.get(), PurpleSpottedBigeyeRenderer::new);
		event.registerEntityRenderer(ECEntities.MANTA.get(), MantaRenderer::new);
		event.registerEntityRenderer(ECEntities.WRAITH.get(), WraithRenderer::new);
		event.registerEntityRenderer(ECEntities.BOAT.get(), ECBoatRenderer::new);
	}
}
