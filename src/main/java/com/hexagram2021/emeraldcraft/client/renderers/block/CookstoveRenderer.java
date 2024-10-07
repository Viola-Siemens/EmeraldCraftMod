package com.hexagram2021.emeraldcraft.client.renderers.block;

import com.hexagram2021.emeraldcraft.client.models.CookstoveDisplayModel;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CookstoveRenderer implements BlockEntityRenderer<CookstoveBlockEntity> {
	public static final ResourceLocation COOKSTOVE_ATLAS = new ResourceLocation(MODID, "textures/atlas/cookstove_shapes.png");

	private final ItemRenderer itemRenderer;
	private final CookstoveDisplayModel displayModel;

	public CookstoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
		this.displayModel = new CookstoveDisplayModel(context.bakeLayer(CookstoveDisplayModel.LAYER_LOCATION));
	}

	@Override
	public void render(CookstoveBlockEntity blockEntity, float partialTick, PoseStack transform, MultiBufferSource buffer, int rgb, int overlay) {
		if (blockEntity.getLevel() != null) {
			CookstoveBlockEntity.CookstoveDisplay display = blockEntity.getDisplay();
			int color = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos().above());
			if (display == null) {
				for (int i = 0; i < CookstoveBlockEntity.COUNT_SLOTS; ++i) {
					ItemStack itemStack = blockEntity.getItem(i);
					if (itemStack.isEmpty()) {
						continue;
					}
					double angle = (i - 4) * Math.PI / 4.0D;
					double dx = Math.cos(angle) * 0.375D;
					double dz = Math.sin(angle) * 0.375D;
					double animateTick = blockEntity.animateTick * 7.0D / 3.0D / SharedConstants.TICKS_PER_SECOND;
					double deltaHeight = Math.max(0.0D, Math.sin(animateTick) * 0.25D - 0.15D) * Math.pow(Math.sin(animateTick), 15.0D);
					double deltaRot = deltaHeight > 0.0D ? Math.sin(4.0D * animateTick) * 10.0D : 0.0D;
					transform.pushPose();
					transform.translate(dx + 0.5D, 0.575F + deltaHeight, dz + 0.5D);
					transform.mulPose(Axis.YP.rotationDegrees(Mth.RAD_TO_DEG * (float) angle + 90.0F));
					transform.mulPose(Axis.XP.rotationDegrees(90.0F + (float) deltaRot));
					transform.scale(0.3F, 0.3F, 0.3F);
					this.itemRenderer.renderStatic(blockEntity.getItem(i), ItemDisplayContext.FIXED, color, overlay, transform, buffer, blockEntity.getLevel(), 0);
					transform.popPose();
				}
			} else {
				ItemStack[] displayItems = display.ingredient().getItems();
				for(int i = 0; i < displayItems.length; ++i) {
					double angle = i * Math.PI * 2.0D / displayItems.length;
					double dz = Math.cos(angle) * 0.125D;
					double dx = Math.sin(angle) * 0.125D;
					transform.pushPose();
					transform.translate(dx + 0.5D, 0.6F, dz + 0.5D);
					transform.mulPose(Axis.YP.rotationDegrees(Mth.RAD_TO_DEG * (float) angle));
					transform.mulPose(Axis.XP.rotationDegrees(85.0F));
					transform.scale(0.25F, 0.25F, 0.25F);
					this.itemRenderer.renderStatic(displayItems[i], ItemDisplayContext.FIXED, color, overlay, transform, buffer, blockEntity.getLevel(), 0);
					transform.popPose();
				}
				transform.pushPose();
				transform.translate(0.5D, 0.575F, 0.5D);
				Material shape = new Material(COOKSTOVE_ATLAS, display.background().shape().withPrefix("cookstove_shapes/"));
				int backgroundColor = display.background().color();
				int r = (backgroundColor >> 16) & 0xff;
				int g = (backgroundColor >> 8) & 0xff;
				int b = backgroundColor & 0xff;
				this.displayModel.renderToBuffer(transform, shape.buffer(buffer, RenderType::entityTranslucent), color, overlay, r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
				transform.popPose();
			}
		}
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
