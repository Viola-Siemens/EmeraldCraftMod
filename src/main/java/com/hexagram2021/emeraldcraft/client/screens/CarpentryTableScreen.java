package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.CarpentryTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class CarpentryTableScreen extends AbstractContainerScreen<CarpentryTableMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/carpentry.png");
	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;

	public CarpentryTableScreen(CarpentryTableMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
		menu.registerUpdateListener(this::containerChanged);
		--this.titleLabelY;
	}

	@Override
	public void render(GuiGraphics transform, int x, int y, float partialTicks) {
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		this.renderBackground(transform);
		int i = this.leftPos;
		int j = this.topPos;
		transform.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int k = (int)(41.0F * this.scrollOffs);
		transform.blit(BG_LOCATION, i + 119, j + 15 + k, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
		int l = this.leftPos + 52;
		int i1 = this.topPos + 14;
		int j1 = this.startIndex + 12;
		this.renderButtons(transform, x, y, l, i1, j1);
		this.renderRecipes(transform, l, i1, j1);
	}

	@Override
	protected void renderTooltip(GuiGraphics transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if (this.displayRecipes) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;
			List<CarpentryTableRecipe> list = this.menu.getRecipes();

			for(int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
				int i1 = l - this.startIndex;
				int j1 = i + i1 % 4 * 16;
				int k1 = j + i1 / 4 * 18 + 2;
				if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
					transform.renderTooltip(this.font, list.get(l).getResultItem(this.minecraft.level.registryAccess()), x, y);
				}
			}
		}

	}

	private void renderButtons(GuiGraphics transform, int x, int y, int recipeX, int recipeY, int endIndex) {
		for(int i = this.startIndex; i < endIndex && i < this.menu.getNumRecipes(); ++i) {
			int line = i - this.startIndex;
			int buttonX = recipeX + line % 4 * 16;
			int lineY = line / 4;
			int buttonY = recipeY + lineY * 18 + 2;
			int buttonHeight = this.imageHeight;
			if (i == this.menu.getSelectedRecipeIndex()) {
				buttonHeight += 18;
			} else if (x >= buttonX && y >= buttonY && x < buttonX + 16 && y < buttonY + 18) {
				buttonHeight += 36;
			}

			transform.blit(BG_LOCATION, buttonX, buttonY - 1, 0, buttonHeight, 16, 18);
		}

	}

	private void renderRecipes(GuiGraphics transform, int x, int y, int endIndex) {
		List<CarpentryTableRecipe> list = this.menu.getRecipes();

		for(int i = this.startIndex; i < endIndex && i < this.menu.getNumRecipes(); ++i) {
			int line = i - this.startIndex;
			int itemX = x + line % 4 * 16;
			int lineY = line / 4;
			int itemY = y + lineY * 18 + 2;
			transform.renderItem(list.get(i).getResultItem(this.minecraft.level.registryAccess()), itemX, itemY);
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		this.scrolling = false;
		if (this.displayRecipes) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;

			for(int l = this.startIndex; l < k; ++l) {
				int i1 = l - this.startIndex;
				double d0 = x - (double)(i + i1 % 4 * 16);
				double d1 = y - (double)(j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
					return true;
				}
			}

			i = this.leftPos + 119;
			j = this.topPos + 9;
			if (x >= (double)i && x < (double)(i + 12) && y >= (double)j && y < (double)(j + 54)) {
				this.scrolling = true;
			}
		}

		return super.mouseClicked(x, y, mouseButton);
	}

	@Override
	public boolean mouseDragged(double fromX, double fromY, int activeButton, double toX, double toY) {
		if (this.scrolling && this.isScrollBarActive()) {
			int i = this.topPos + 14;
			int j = i + 54;
			this.scrollOffs = ((float)fromY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
			return true;
		} else {
			return super.mouseDragged(fromX, fromY, activeButton, toX, toY);
		}
	}

	@Override
	public boolean mouseScrolled(double x, double y, double delta) {
		if (this.isScrollBarActive()) {
			int i = this.getOffscreenRows();
			this.scrollOffs = (float)((double)this.scrollOffs - delta / (double)i);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D) * 4;
		}

		return true;
	}

	private boolean isScrollBarActive() {
		return this.displayRecipes && this.menu.getNumRecipes() > 12;
	}

	protected int getOffscreenRows() {
		return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
	}

	private void containerChanged() {
		this.displayRecipes = this.menu.hasInputItem();
		if (!this.displayRecipes) {
			this.scrollOffs = 0.0F;
			this.startIndex = 0;
		}
	}
}
