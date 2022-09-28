package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.MineralTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MineralTableScreen extends AbstractContainerScreen<MineralTableMenu> {
	private static final ResourceLocation MINERAL_TABLE_LOCATION = new ResourceLocation(MODID, "textures/gui/container/mineral_table.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

	public MineralTableScreen(MineralTableMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	public void render(@NotNull PoseStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(@NotNull PoseStack transform, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MINERAL_TABLE_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int k = this.menu.getLitProgress();
		int l = Mth.clamp((18 * k + 20 - 1) / 20, 0, 18);
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
