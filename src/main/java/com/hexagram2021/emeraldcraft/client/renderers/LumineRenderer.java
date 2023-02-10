package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.LumineModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.LumineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class LumineRenderer extends MobRenderer<LumineEntity, LumineModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/lumine/lumine.png");

	public LumineRenderer(EntityRendererProvider.Context context) {
		super(context, new LumineModel(context.bakeLayer(LumineModel.LAYER_LOCATION)), 0.4F);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull LumineEntity lumine) {
		return TEXTURE;
	}

	@Override
	protected int getBlockLightLevel(@NotNull LumineEntity lumine, @NotNull BlockPos blockPos) {
		return 15;
	}
}
