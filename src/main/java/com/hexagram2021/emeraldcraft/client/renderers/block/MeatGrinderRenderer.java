package com.hexagram2021.emeraldcraft.client.renderers.block;

import com.hexagram2021.emeraldcraft.common.blocks.entity.MeatGrinderBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.MeatGrinderBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class MeatGrinderRenderer implements BlockEntityRenderer<MeatGrinderBlockEntity> {
	private final ItemRenderer itemRenderer;

	public MeatGrinderRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(MeatGrinderBlockEntity blockEntity, float partialTick, PoseStack transform, MultiBufferSource buffer, int rgb, int overlay) {
		if(!blockEntity.isWorking()) {
			return;
		}
		transform.pushPose();
		transform.translate(0.5F, blockEntity.getProgress() * 0.4F + 0.3F, 0.5F);
		transform.scale(0.5F, 0.5F, 0.5F);
		transform.rotateAround(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(MeatGrinderBlock.FACING).toYRot()), 0.0F, 0.0F, 0.0F);
		this.itemRenderer.renderStatic(blockEntity.getItem(0), ItemDisplayContext.FIXED, rgb, overlay, transform, buffer, blockEntity.getLevel(), 0);
		transform.popPose();
	}

	@Override
	public int getViewDistance() {
		return 48;
	}
}
