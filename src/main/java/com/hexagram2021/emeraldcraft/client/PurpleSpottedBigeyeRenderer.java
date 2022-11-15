package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.PurpleSpottedBigeyeModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PurpleSpottedBigeyeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class PurpleSpottedBigeyeRenderer extends MobRenderer<PurpleSpottedBigeyeEntity, PurpleSpottedBigeyeModel<PurpleSpottedBigeyeEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/purple_spotted_bigeye.png");

	public PurpleSpottedBigeyeRenderer(EntityRendererProvider.Context manager) {
		super(manager, new PurpleSpottedBigeyeModel<>(manager.bakeLayer(PurpleSpottedBigeyeModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull PurpleSpottedBigeyeEntity entity) { return TEXTURE; }
}
