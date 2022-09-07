package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import com.hexagram2021.emeraldcraft.client.models.NetherPigmanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherPigmanRenderer extends MobRenderer<NetherPigmanEntity, NetherPigmanModel<NetherPigmanEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/nether_pigman/nether_pigman.png");

	public NetherPigmanRenderer(EntityRendererManager manager) {
		super(manager, new NetherPigmanModel<>(0.0F), 0.7F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull NetherPigmanEntity entity) { return TEXTURE; }
}
