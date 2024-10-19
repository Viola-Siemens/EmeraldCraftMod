package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ISynchronizableContainer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IFluidSyncMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class ScreenUtils {
	private static final int TEXTURE_SIZE = 16;

	public static void renderFluid(GuiGraphics transform, FluidStack fluidStack, int tankX, int tankY, int fluidWidth, int maxTankHeight, int maxTankCapability) {
		if(fluidStack.isEmpty()) {
			return;
		}
		IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid().getFluidType());
		ResourceLocation fluidTexture = extensions.getStillTexture();
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
		int tintColor = extensions.getTintColor();
		float a = ((tintColor >> 24) & 0xff) / 255.0F;
		float r = ((tintColor >> 16) & 0xff) / 255.0F;
		float g = ((tintColor >> 8) & 0xff) / 255.0F;
		float b = (tintColor & 0xff) / 255.0F;
		transform.setColor(r, g, b, a);
		int remaining = Mth.clamp((maxTankCapability - fluidStack.getAmount()) * maxTankHeight / maxTankCapability, 0, maxTankHeight - 1);
		int fluidHeight = maxTankHeight - remaining - 1;
		int widthCount = fluidWidth / TEXTURE_SIZE;
		int heightCount = fluidHeight / TEXTURE_SIZE;
		int widthMod = fluidWidth % TEXTURE_SIZE;
		int heightMod = fluidHeight % TEXTURE_SIZE;
		for(int i = 0; i < widthCount; ++i) {
			for(int j = 0; j < heightCount; ++j) {
				drawFluidWithMask(
						transform, sprite.atlasLocation(),
						tankX + i * TEXTURE_SIZE, tankY + remaining + j * TEXTURE_SIZE, 0,
						sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
						TEXTURE_SIZE, TEXTURE_SIZE
				);
			}
			if(heightMod != 0) {
				drawFluidWithMask(
						transform, sprite.atlasLocation(),
						tankX + i * TEXTURE_SIZE, tankY + remaining + heightCount * TEXTURE_SIZE, 0,
						sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
						TEXTURE_SIZE, heightMod
				);
			}
		}
		if(widthMod != 0) {
			for (int j = 0; j < heightCount; ++j) {
				drawFluidWithMask(
						transform, sprite.atlasLocation(),
						tankX + widthCount * TEXTURE_SIZE, tankY + remaining + j * TEXTURE_SIZE, 0,
						sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
						widthMod, TEXTURE_SIZE
				);
			}
			if (heightMod != 0) {
				drawFluidWithMask(
						transform, sprite.atlasLocation(),
						tankX + widthCount * TEXTURE_SIZE, tankY + remaining + heightCount * TEXTURE_SIZE, 0,
						sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
						widthMod, heightMod
				);
			}
		}
		transform.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void renderFluidFromContainer(GuiGraphics transform, Container container, int tankIndex, int tankX, int tankY, int fluidWidth, int maxTankHeight, int maxTankCapability) {
		if(container instanceof Tank tank) {
			renderFluid(transform, tank.getFluidStack(tankIndex), tankX, tankY, fluidWidth, maxTankHeight, maxTankCapability);
		}
	}

	public static void handleFluidSyncPacket(List<FluidStack> fluidStacks) {
		Player player = Minecraft.getInstance().player;
		if(player != null && player.containerMenu instanceof IFluidSyncMenu menu) {
			if(menu.getContainer() instanceof ISynchronizableContainer container) {
				for(int i = 0; i < fluidStacks.size(); ++i) {
					container.setFluidStack(i, fluidStacks.get(i));
				}
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private static void drawFluidWithMask(GuiGraphics transform, ResourceLocation atlasLocation, int xCoord, int yCoord, float blitOffset, float minU, float maxU, float minV, float maxV, int width, int height) {
		RenderSystem.setShaderTexture(0, atlasLocation);
		maxU = maxU - ((TEXTURE_SIZE - width) / (float)TEXTURE_SIZE * (maxU - minU));
		maxV = maxV - ((TEXTURE_SIZE - height) / (float)TEXTURE_SIZE * (maxV - minV));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = transform.pose().last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		int x2 = xCoord + width, y2 = yCoord + height;
		bufferbuilder.vertex(matrix4f, xCoord, yCoord, blitOffset).uv(minU, minV).endVertex();
		bufferbuilder.vertex(matrix4f, xCoord, y2, blitOffset).uv(minU, maxV).endVertex();
		bufferbuilder.vertex(matrix4f, x2, y2, blitOffset).uv(maxU, maxV).endVertex();
		bufferbuilder.vertex(matrix4f, x2, yCoord, blitOffset).uv(maxU, minV).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
	}

	private ScreenUtils() {
	}
}
