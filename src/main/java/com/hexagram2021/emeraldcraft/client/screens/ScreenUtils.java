package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public final class ScreenUtils {
	public static void renderFluid(GuiGraphics transform, FluidStack fluidStack, int tankX, int tankY, int fluidWidth, int maxTankHeight, int maxTankCapability) {
		if(fluidStack.isEmpty()) {
			return;
		}
		IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid().getFluidType());
		ResourceLocation fluidTexture = extensions.getStillTexture().withPrefix("textures/").withSuffix(".png");
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
		int tintColor = extensions.getTintColor();
		float a = ((tintColor >> 24) & 0xff) / 255.0F;
		float r = ((tintColor >> 16) & 0xff) / 255.0F;
		float g = ((tintColor >> 8) & 0xff) / 255.0F;
		float b = (tintColor & 0xff) / 255.0F;
		transform.setColor(r, g, b, a);
		int remaining = Mth.clamp((maxTankCapability - fluidStack.getAmount()) * maxTankHeight / maxTankCapability, 0, maxTankHeight - 1);
		transform.blit(tankX, tankY + remaining, 0, fluidWidth, maxTankHeight - remaining - 1, sprite);
		transform.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void renderFluidFromContainer(GuiGraphics transform, Container container, int tankIndex, int tankX, int tankY, int fluidWidth, int maxTankHeight, int maxTankCapability) {
		if(container instanceof Tank tank) {
			renderFluid(transform, tank.getFluidStack(tankIndex), tankX, tankY, fluidWidth, maxTankHeight, maxTankCapability);
		}
	}
}
