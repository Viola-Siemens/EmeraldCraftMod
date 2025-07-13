package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.*;
import com.hexagram2021.emeraldcraft.client.renderers.*;
import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.client.renderers.block.MeatGrinderRenderer;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;

public class ClientEntityEventSubscriber {
    public static void onRegisterLayers() {
        EntityModelLayerRegistry.registerModelLayer(PiglinCuteyModel.LAYER_LOCATION, PiglinCuteyModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NetherPigmanModel.LAYER_LOCATION, NetherPigmanModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NetherLambmanModel.LAYER_LOCATION, NetherLambmanModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(HerringModel.LAYER_LOCATION, HerringModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PurpleSpottedBigeyeModel.LAYER_LOCATION, PurpleSpottedBigeyeModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SnakeheadModel.LAYER_LOCATION, SnakeheadModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WraithModel.LAYER_LOCATION, WraithModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(MantaModel.LAYER_LOCATION, MantaModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(LumineModel.LAYER_LOCATION, LumineModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WombatModel.LAYER_LOCATION, WombatModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CookstoveDisplayModel.LAYER_LOCATION, CookstoveDisplayModel::createBodyLayer);

        for (ECBoat.ECBoatType type : ECBoat.ECBoatType.values()) {
            EntityModelLayerRegistry.registerModelLayer(ECBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
            EntityModelLayerRegistry.registerModelLayer(ECBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
        }
    }

    @SuppressWarnings("deprecation")
    public static void onRegisterRenderer() {
        EntityRendererRegistry.register(ECEntities.PIGLIN_CUTEY, PiglinCuteyRenderer::new);
        EntityRendererRegistry.register(ECEntities.NETHER_PIGMAN, NetherPigmanRenderer::new);
        EntityRendererRegistry.register(ECEntities.NETHER_LAMBMAN, NetherLambmanRenderer::new);
        EntityRendererRegistry.register(ECEntities.HERRING, HerringRenderer::new);
        EntityRendererRegistry.register(ECEntities.PURPLE_SPOTTED_BIGEYE, PurpleSpottedBigeyeRenderer::new);
        EntityRendererRegistry.register(ECEntities.SNAKEHEAD, SnakeheadRenderer::new);
        EntityRendererRegistry.register(ECEntities.WRAITH, WraithRenderer::new);
        EntityRendererRegistry.register(ECEntities.MANTA, MantaRenderer::new);
        EntityRendererRegistry.register(ECEntities.LUMINE, LumineRenderer::new);
        EntityRendererRegistry.register(ECEntities.WOMBAT, WombatRenderer::new);
        EntityRendererRegistry.register(ECEntities.BOAT, (context) -> new ECBoatRenderer(context, false));
        EntityRendererRegistry.register(ECEntities.CHEST_BOAT, (context) -> new ECBoatRenderer(context, true));


        BlockEntityRendererRegistry.register(ECBlockEntity.MEAT_GRINDER, MeatGrinderRenderer::new);
        BlockEntityRendererRegistry.register(ECBlockEntity.COOKSTOVE, CookstoveRenderer::new);
    }
}
