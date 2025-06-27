package com.hexagram2021.emeraldcraft.client.renderers.block;

import com.hexagram2021.emeraldcraft.client.models.CookstoveDisplayModel;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

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
		Level level = blockEntity.getLevel();
		if (level != null) {
			ICookstoveDisplay display = blockEntity.getDisplay();
			int color = LevelRenderer.getLightColor(level, blockEntity.getBlockState(), blockEntity.getBlockPos().above());

			if (display == null) {
				// render ingredients
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
					this.itemRenderer.renderStatic(blockEntity.getItem(i), ItemDisplayContext.FIXED, color, overlay, transform, buffer, level, 0);
					transform.popPose();
				}

				// render fluids
				FluidStack fluidStack = blockEntity.getFluidStack(CookstoveBlockEntity.TANK_INPUT);
				if(!fluidStack.isEmpty()) {
					Fluid fluid = fluidStack.getFluid();
					FluidState fluidState = fluid.defaultFluidState();
					IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluidType());
					Material texture = new Material(InventoryMenu.BLOCK_ATLAS, extensions.getStillTexture());
					int tintColor = extensions.getTintColor(fluidState, level, blockEntity.getBlockPos());
					VertexConsumer builder = buffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidState));
					renderFluid(transform, builder, fluidStack.getAmount(), texture, rgb, tintColor);
				}
			} else {
				// render display
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

	private static void renderFluid(PoseStack transform, VertexConsumer builder, int amount, Material texture, int rgb, int tintColor) {
		TextureAtlasSprite sprite = texture.sprite();
		transform.pushPose();
		float height = (float)(0.5625D + 0.000045D * amount);
		drawQuad(builder, transform, 0.025F, 0.025F, 0.975F, 0.975F, height, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), rgb, tintColor);
		transform.popPose();
	}

	private static void drawVertex(VertexConsumer builder, PoseStack transform, float x, float y, float z, float u, float v, int rgb, int color) {
		builder.vertex(transform.last().pose(), x, y, z).color(color)
				.uv(u, v).uv2(rgb)
				.normal(1, 0, 0).endVertex();
	}

	@SuppressWarnings("SameParameterValue")
	private static void drawQuad(VertexConsumer builder, PoseStack transform, float x0, float z0, float x1, float z1, float y, float u0, float v0, float u1, float v1, int packedLight, int color) {
		drawVertex(builder, transform, x0, y, z0, u0, v0, packedLight, color);
		drawVertex(builder, transform, x0, y, z1, u0, v1, packedLight, color);
		drawVertex(builder, transform, x1, y, z1, u1, v1, packedLight, color);
		drawVertex(builder, transform, x1, y, z0, u1, v0, packedLight, color);
	}
}
