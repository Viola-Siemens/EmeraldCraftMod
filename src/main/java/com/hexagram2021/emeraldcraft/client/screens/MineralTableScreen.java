package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.MineralTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MineralTableScreen extends AbstractContainerScreen<MineralTableMenu> {
	private static final ResourceLocation FUEL_LENGTH_SPRITE = new ResourceLocation(MODID, "container/mineral_table/fuel_length");
	private static final ResourceLocation BREW_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/mineral_table/brew_progress");
	private static final ResourceLocation BUBBLES_SPRITE = new ResourceLocation(MODID, "container/mineral_table/bubbles");
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/mineral_table.png");
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
	public void render(GuiGraphics transform, int x, int y, float partialTicks) {
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		transform.blit(BG_LOCATION, left, top, 0, 0, this.imageWidth, this.imageHeight);
		int litProgress = this.menu.getLitProgress();
		int length = Mth.clamp((18 * litProgress + 20 - 1) / 20, 0, 18);
		if (length > 0) {
			transform.blitSprite(FUEL_LENGTH_SPRITE, 18, 4, 0, 0, left + 60, top + 44, length, 4);
		}

		int burnProgress = this.menu.getBurnProgress();
		if (burnProgress > 0) {
			int progress = (int)(28.0F * (1.0F - (float)burnProgress / 400.0F));
			if (progress > 0) {
				transform.blitSprite(BREW_PROGRESS_SPRITE, 9, 28, 0, 0, left + 97, top + 16, 9, progress);
			}

			progress = BUBBLELENGTHS[burnProgress / 2 % 7];
			if (progress > 0) {
				transform.blitSprite(BUBBLES_SPRITE, 12, 29, 0, 29 - progress, left + 63, top + 14 + 29 - progress, 12, progress);
			}
		}
	}
}
