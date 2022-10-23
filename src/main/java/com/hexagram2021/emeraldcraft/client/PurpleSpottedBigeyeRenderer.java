package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.PurpleSpottedBigeyeModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PurpleSpottedBigeyeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class PurpleSpottedBigeyeRenderer extends MobRenderer<PurpleSpottedBigeyeEntity, PurpleSpottedBigeyeModel<PurpleSpottedBigeyeEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/purple_spotted_bigeye.png");

	public PurpleSpottedBigeyeRenderer(EntityRendererManager manager) {
		super(manager, new PurpleSpottedBigeyeModel<>(), 0.7F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull PurpleSpottedBigeyeEntity entity) { return TEXTURE; }
}
