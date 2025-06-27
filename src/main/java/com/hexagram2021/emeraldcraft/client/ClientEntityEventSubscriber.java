package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.*;
import com.hexagram2021.emeraldcraft.client.renderers.*;
import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.client.renderers.block.MeatGrinderRenderer;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEntityEventSubscriber {
	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PiglinCuteyModel.LAYER_LOCATION, PiglinCuteyModel::createBodyLayer);
		event.registerLayerDefinition(NetherPigmanModel.LAYER_LOCATION, NetherPigmanModel::createBodyLayer);
		event.registerLayerDefinition(NetherLambmanModel.LAYER_LOCATION, NetherLambmanModel::createBodyLayer);
		event.registerLayerDefinition(HerringModel.LAYER_LOCATION, HerringModel::createBodyLayer);
		event.registerLayerDefinition(PurpleSpottedBigeyeModel.LAYER_LOCATION, PurpleSpottedBigeyeModel::createBodyLayer);
		event.registerLayerDefinition(SnakeheadModel.LAYER_LOCATION, SnakeheadModel::createBodyLayer);
		event.registerLayerDefinition(WraithModel.LAYER_LOCATION, WraithModel::createBodyLayer);
		event.registerLayerDefinition(MantaModel.LAYER_LOCATION, MantaModel::createBodyLayer);
		event.registerLayerDefinition(LumineModel.LAYER_LOCATION, LumineModel::createBodyLayer);
		event.registerLayerDefinition(WombatModel.LAYER_LOCATION, WombatModel::createBodyLayer);
		event.registerLayerDefinition(CookstoveDisplayModel.LAYER_LOCATION, CookstoveDisplayModel::createBodyLayer);

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
		event.registerEntityRenderer(ECEntities.SNAKEHEAD, SnakeheadRenderer::new);
		event.registerEntityRenderer(ECEntities.WRAITH, WraithRenderer::new);
		event.registerEntityRenderer(ECEntities.MANTA, MantaRenderer::new);
		event.registerEntityRenderer(ECEntities.LUMINE, LumineRenderer::new);
		event.registerEntityRenderer(ECEntities.WOMBAT, WombatRenderer::new);
		event.registerEntityRenderer(ECEntities.BOAT, (context) -> new ECBoatRenderer(context, false));
		event.registerEntityRenderer(ECEntities.CHEST_BOAT, (context) -> new ECBoatRenderer(context, true));

		event.registerBlockEntityRenderer(ECBlockEntity.MEAT_GRINDER.get(), MeatGrinderRenderer::new);
		event.registerBlockEntityRenderer(ECBlockEntity.COOKSTOVE.get(), CookstoveRenderer::new);
	}
}
