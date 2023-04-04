package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.*;
import com.hexagram2021.emeraldcraft.client.renderers.*;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientEntityEventSubscriber {
	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PiglinCuteyModel.LAYER_LOCATION, PiglinCuteyModel::createBodyLayer);
		event.registerLayerDefinition(NetherPigmanModel.LAYER_LOCATION, NetherPigmanModel::createBodyLayer);
		event.registerLayerDefinition(NetherLambmanModel.LAYER_LOCATION, NetherLambmanModel::createBodyLayer);
		event.registerLayerDefinition(HerringModel.LAYER_LOCATION, HerringModel::createBodyLayer);
		event.registerLayerDefinition(PurpleSpottedBigeyeModel.LAYER_LOCATION, PurpleSpottedBigeyeModel::createBodyLayer);
		event.registerLayerDefinition(WraithModel.LAYER_LOCATION, WraithModel::createBodyLayer);
		event.registerLayerDefinition(MantaModel.LAYER_LOCATION, MantaModel::createBodyLayer);
		event.registerLayerDefinition(LumineModel.LAYER_LOCATION, LumineModel::createBodyLayer);

		for(ECBoat.ECBoatType type: ECBoat.ECBoatType.values()) {
			event.registerLayerDefinition(ECBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
			event.registerLayerDefinition(ECBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
		}
	}

	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ECEntities.PIGLIN_CUTEY, PiglinCuteyRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_PIGMAN, NetherPigmanRenderer::new);
		event.registerEntityRenderer(ECEntities.NETHER_LAMBMAN, NetherLambmanRenderer::new);
		event.registerEntityRenderer(ECEntities.HERRING, HerringRenderer::new);
		event.registerEntityRenderer(ECEntities.PURPLE_SPOTTED_BIGEYE, PurpleSpottedBigeyeRenderer::new);
		event.registerEntityRenderer(ECEntities.WRAITH, WraithRenderer::new);
		event.registerEntityRenderer(ECEntities.MANTA, MantaRenderer::new);
		event.registerEntityRenderer(ECEntities.LUMINE, LumineRenderer::new);
		event.registerEntityRenderer(ECEntities.BOAT, (context) -> new ECBoatRenderer(context, false));
		event.registerEntityRenderer(ECEntities.CHEST_BOAT, (context) -> new ECBoatRenderer(context, true));
	}
}
