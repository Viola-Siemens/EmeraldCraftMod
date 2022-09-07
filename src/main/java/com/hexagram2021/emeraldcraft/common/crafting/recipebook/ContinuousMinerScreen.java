package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.ContinuousMinerMenu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class ContinuousMinerScreen extends ContainerScreen<ContinuousMinerMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/continuous_miner.png");

	public ContinuousMinerScreen(ContinuousMinerMenu menu, PlayerInventory inventory, ITextComponent component) {
		super(menu, inventory, component);
		--this.titleLabelY;
	}

	@Override
	public void render(@Nonnull MatrixStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack transform, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BG_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int energyLevel = this.menu.getFluidLevel();
		if(energyLevel > 0) {
			int k = MathHelper.clamp((ContinuousMinerBlockEntity.MAX_FLUID_LEVEL - 1 - energyLevel) / 5, 0, 49);
			this.blit(transform, i + 119, j + 20 + k, 176, k, 12, 49 - k);
		}
	}
}
