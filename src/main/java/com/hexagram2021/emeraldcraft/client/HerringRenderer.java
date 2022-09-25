package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.HerringModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.HerringEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class HerringRenderer extends MobRenderer<HerringEntity, HerringModel<HerringEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/herring.png");

	public HerringRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HerringModel<>(manager.bakeLayer(HerringModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull HerringEntity entity) { return TEXTURE; }
}
