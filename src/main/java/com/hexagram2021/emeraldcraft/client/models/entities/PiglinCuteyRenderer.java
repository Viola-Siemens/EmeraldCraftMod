package com.hexagram2021.emeraldcraft.client.models.entities;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PiglinCuteyRenderer extends MobRenderer<PiglinCuteyEntity, PiglinCuteyModel<PiglinCuteyEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin_cutey/piglin_cutey.png");

	public PiglinCuteyRenderer(EntityRendererProvider.Context manager) {
		super(manager, new PiglinCuteyModel<>(manager.bakeLayer(PiglinCuteyModel.LAYER_LOCATION)), 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(PiglinCuteyEntity entity) { return TEXTURE; }
}
