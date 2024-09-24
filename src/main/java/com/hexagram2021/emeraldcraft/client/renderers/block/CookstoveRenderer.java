package com.hexagram2021.emeraldcraft.client.renderers.block;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CookstoveRenderer implements BlockEntityRenderer<CookstoveBlockEntity> {
	private final ItemRenderer itemRenderer;

	public CookstoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(CookstoveBlockEntity blockEntity, float partialTick, PoseStack transform, MultiBufferSource buffer, int rgb, int overlay) {
		for(int i = 0; i < CookstoveBlockEntity.COUNT_SLOTS; ++i) {
			ItemStack itemStack = blockEntity.getItem(i);
			if(itemStack.isEmpty()) {
				continue;
			}
			double angle = (i - 4) * Math.PI / 4.0D;
			double dx = Math.cos(angle) * 0.375D;
			double dz = Math.sin(angle) * 0.375D;
			transform.pushPose();
			transform.translate(dx + 0.5D, 0.7F, dz + 0.5D);
			transform.rotateAround(Axis.XP.rotationDegrees(90), 0.0F, 0.0F, 0.0F);
			transform.scale(0.3F, 0.3F, 0.3F);
			this.itemRenderer.renderStatic(blockEntity.getItem(i), ItemDisplayContext.FIXED, rgb, overlay, transform, buffer, blockEntity.getLevel(), 0);
			transform.popPose();
		}
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
