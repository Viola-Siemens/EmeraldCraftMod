package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import com.hexagram2021.emeraldcraft.client.models.PiglinCuteyModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class PiglinCuteyRenderer extends MobRenderer<PiglinCuteyEntity, PiglinCuteyModel<PiglinCuteyEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin_cutey/piglin_cutey.png");

	public PiglinCuteyRenderer(EntityRendererManager manager) {
		super(manager, new PiglinCuteyModel<>(0.0F), 0.7F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull PiglinCuteyEntity entity) { return TEXTURE; }
}
