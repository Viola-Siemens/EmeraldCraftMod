package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.NetherPigmanModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherPigmanRenderer extends MobRenderer<NetherPigmanEntity, NetherPigmanModel<NetherPigmanEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/nether_pigman/nether_pigman.png");

	public NetherPigmanRenderer(EntityRendererProvider.Context manager) {
		super(manager, new NetherPigmanModel<>(manager.bakeLayer(NetherPigmanModel.LAYER_LOCATION)), 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(NetherPigmanEntity entity) { return TEXTURE; }
}
