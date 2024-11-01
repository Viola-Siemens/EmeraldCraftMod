package com.hexagram2021.emeraldcraft.client.renderers.block;

import com.hexagram2021.emeraldcraft.client.models.CookstoveDisplayModel;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CookstoveRenderer implements BlockEntityRenderer<CookstoveBlockEntity> {
	private final BlockRenderDispatcher blockRenderer;
	private final ItemRenderer itemRenderer;
	private final CookstoveDisplayModel displayModel;

	public CookstoveRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderer = context.getBlockRenderDispatcher();
		this.itemRenderer = context.getItemRenderer();
		this.displayModel = new CookstoveDisplayModel(context.bakeLayer(CookstoveDisplayModel.LAYER_LOCATION));
	}

	@Override
	public void render(CookstoveBlockEntity blockEntity, float partialTick, PoseStack transform, MultiBufferSource buffer, int rgb, int overlay) {
		if (blockEntity.getLevel() != null) {
			ICookstoveDisplay display = blockEntity.getDisplay();
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
				display.render(transform, this, blockEntity, buffer, color, overlay);
			}
		}
	}

	@Override
	public int getViewDistance() {
		return 32;
	}

	public BlockRenderDispatcher getBlockRenderer() {
		return this.blockRenderer;
	}
	public ItemRenderer getItemRenderer() {
		return this.itemRenderer;
	}
	public CookstoveDisplayModel getDisplayModel() {
		return this.displayModel;
	}
}
