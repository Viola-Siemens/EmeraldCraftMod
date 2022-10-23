package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.HerringModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.HerringEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class HerringRenderer extends MobRenderer<HerringEntity, HerringModel<HerringEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/herring.png");

	public HerringRenderer(EntityRendererManager manager) {
		super(manager, new HerringModel<>(), 0.7F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull HerringEntity entity) { return TEXTURE; }
}
