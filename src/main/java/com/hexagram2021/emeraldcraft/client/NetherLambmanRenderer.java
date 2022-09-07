package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import com.hexagram2021.emeraldcraft.client.models.NetherLambmanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherLambmanRenderer extends MobRenderer<NetherLambmanEntity, NetherLambmanModel<NetherLambmanEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/nether_lambman/nether_lambman.png");

	public NetherLambmanRenderer(EntityRendererManager manager) {
		super(manager, new NetherLambmanModel<>(0.0F), 0.7F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull NetherLambmanEntity entity) { return TEXTURE; }
}
