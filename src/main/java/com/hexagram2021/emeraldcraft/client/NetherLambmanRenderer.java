package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import com.hexagram2021.emeraldcraft.client.models.NetherLambmanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherLambmanRenderer extends MobRenderer<NetherLambmanEntity, NetherLambmanModel<NetherLambmanEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/nether_lambman/nether_lambman.png");

	public NetherLambmanRenderer(EntityRendererProvider.Context manager) {
		super(manager, new NetherLambmanModel<>(manager.bakeLayer(NetherLambmanModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull NetherLambmanEntity entity) { return TEXTURE; }
}
