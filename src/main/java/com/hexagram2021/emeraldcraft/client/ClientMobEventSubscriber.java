package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientMobEventSubscriber {
	public static void onRegisterRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.NETHER_PIGMAN.get(), NetherPigmanRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.NETHER_LAMBMAN.get(), NetherLambmanRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.BOAT.get(), ECBoatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.HERRING.get(), HerringRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.PURPLE_SPOTTED_BIGEYE.get(), PurpleSpottedBigeyeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ECEntities.MANTA.get(), MantaRenderer::new);
	}
}
