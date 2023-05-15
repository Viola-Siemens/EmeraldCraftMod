package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.SnakeheadModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.SnakeheadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class SnakeheadRenderer extends MobRenderer<SnakeheadEntity, SnakeheadModel<SnakeheadEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/snakehead.png");

	public SnakeheadRenderer(EntityRendererProvider.Context manager) {
		super(manager, new SnakeheadModel<>(manager.bakeLayer(SnakeheadModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull SnakeheadEntity entity) { return TEXTURE; }

	@Override
	protected void setupRotations(@NotNull SnakeheadEntity entity, @NotNull PoseStack transform, float bob, float yRot, float xRot) {
		super.setupRotations(entity, transform, bob, yRot, xRot);
		float f = 1.0F;
		float f1 = 1.0F;
		if (!entity.isInWater()) {
			f = 1.3F;
			f1 = 1.7F;
		}

		float f2 = f * 4.3F * Mth.sin(f1 * 0.6F * bob);
		transform.mulPose(Vector3f.YP.rotationDegrees(f2));
		transform.translate(0.0D, 0.0D, -0.4D);
		if (!entity.isInWater()) {
			transform.translate(0.2D, 0.1D, 0.0D);
			transform.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
		}
	}
}
