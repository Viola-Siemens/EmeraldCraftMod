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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("DataFlowIssue")
@OnlyIn(Dist.CLIENT)
public class CarpentryTableScreen extends AbstractContainerScreen<CarpentryTableMenu> {
	private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation(MODID, "container/carpentry/scroller");
	private static final ResourceLocation SCROLLER_DISABLED_SPRITE = new ResourceLocation(MODID, "container/carpentry/scroller_disabled");
	private static final ResourceLocation RECIPE_SELECTED_SPRITE = new ResourceLocation(MODID, "container/carpentry/recipe_selected");
	private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = new ResourceLocation(MODID, "container/carpentry/recipe_highlighted");
	private static final ResourceLocation RECIPE_SPRITE = new ResourceLocation(MODID, "container/carpentry/recipe");
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
		transform.blit(BG_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		int scrollBarY = (int)(41.0F * this.scrollOffs);
		ResourceLocation resourcelocation = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
		transform.blitSprite(resourcelocation, this.leftPos + 119, this.topPos + 15 + scrollBarY, 12, 15);
		int buttonX = this.leftPos + 52;
		int buttonY = this.topPos + 14;
		int endIndex = this.startIndex + 12;
		this.renderButtons(transform, x, y, buttonX, buttonY, endIndex);
		this.renderRecipes(transform, buttonX, buttonY, endIndex);
	}

	@Override
	protected void renderTooltip(GuiGraphics transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if (this.displayRecipes) {
			int left = this.leftPos + 52;
			int top = this.topPos + 14;
			int maxIndex = this.startIndex + 12;
			List<RecipeHolder<CarpentryTableRecipe>> list = this.menu.getRecipes();

			for(int i = this.startIndex; i < maxIndex && i < this.menu.getNumRecipes(); ++i) {
				int index = i - this.startIndex;
				int buttonX = left + index % 4 * 16;
				int buttonY = top + index / 4 * 18 + 2;
				if (x >= buttonX && x < buttonX + 16 && y >= buttonY && y < buttonY + 18) {
					transform.renderTooltip(this.font, list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), x, y);
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
			ResourceLocation sprite;
			if (i == this.menu.getSelectedRecipeIndex()) {
				sprite = RECIPE_SELECTED_SPRITE;
			} else if (x >= buttonX && y >= buttonY && x < buttonX + 16 && y < buttonY + 18) {
				sprite = RECIPE_HIGHLIGHTED_SPRITE;
			} else {
				sprite = RECIPE_SPRITE;
			}

			transform.blitSprite(sprite, buttonX, buttonY - 1, 16, 18);
		}

	}

	private void renderRecipes(GuiGraphics transform, int x, int y, int endIndex) {
		List<RecipeHolder<CarpentryTableRecipe>> list = this.menu.getRecipes();

		for(int i = this.startIndex; i < endIndex && i < this.menu.getNumRecipes(); ++i) {
			int line = i - this.startIndex;
			int itemX = x + line % 4 * 16;
			int lineY = line / 4;
			int itemY = y + lineY * 18 + 2;
			transform.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), itemX, itemY);
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
	public boolean mouseScrolled(double x, double y, double deltaX, double deltaY) {
		if (this.isScrollBarActive()) {
			int i = this.getOffscreenRows();
			this.scrollOffs = (float)((double)this.scrollOffs - deltaY / (double)i);
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
