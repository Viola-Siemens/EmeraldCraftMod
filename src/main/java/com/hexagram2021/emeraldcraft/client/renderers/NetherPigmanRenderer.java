package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import com.hexagram2021.emeraldcraft.client.models.NetherPigmanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherPigmanRenderer extends MobRenderer<NetherPigmanEntity, NetherPigmanModel<NetherPigmanEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/nether_pigman/nether_pigman.png");

	public NetherPigmanRenderer(EntityRendererProvider.Context manager) {
		super(manager, new NetherPigmanModel<>(manager.bakeLayer(NetherPigmanModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull NetherPigmanEntity entity) { return TEXTURE; }
}
