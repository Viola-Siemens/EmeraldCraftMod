package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.MineralTableMenu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MineralTableScreen extends ContainerScreen<MineralTableMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/mineral_table.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

	public MineralTableScreen(MineralTableMenu menu, PlayerInventory inventory, ITextComponent component) {
		super(menu, inventory, component);
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
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
		int k = this.menu.getLitProgress();
		int l = MathHelper.clamp((18 * k + 20 - 1) / 20, 0, 18);
		if (l > 0) {
			this.blit(transform, i + 60, j + 44, 176, 29, l, 4);
		}

		int i1 = this.menu.getBurnProgress();
		if (i1 > 0) {
			int j1 = (int)(28.0F * (1.0F - (float)i1 / 400.0F));
			if (j1 > 0) {
				this.blit(transform, i + 97, j + 16, 176, 0, 9, j1);
			}

			j1 = BUBBLELENGTHS[i1 / 2 % 7];
			if (j1 > 0) {
				this.blit(transform, i + 63, j + 14 + 29 - j1, 185, 29 - j1, 12, j1);
			}
		}

	}
}
