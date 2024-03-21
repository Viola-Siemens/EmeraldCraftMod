package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.RabbleFurnaceMenu;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.RabbleFurnaceRecipeBookComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class RabbleFurnaceScreen extends AbstractContainerScreen<RabbleFurnaceMenu> implements RecipeUpdateListener {
	private static final ResourceLocation LIT_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/rabble_furnace/lit_progress");
	private static final ResourceLocation BURN_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/rabble_furnace/burn_progress");
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/rabble_furnace.png");
	public final RabbleFurnaceRecipeBookComponent recipeBookComponent = new RabbleFurnaceRecipeBookComponent();

	private boolean widthTooNarrow;

	public RabbleFurnaceScreen(RabbleFurnaceMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	public void init() {
		super.init();
		assert this.minecraft != null;

		this.widthTooNarrow = this.width < 379;
		this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		this.addRenderableWidget(new ImageButton(this.leftPos + 84, this.height / 2 - 33, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
			this.recipeBookComponent.toggleVisibility();
			this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
			button.setPosition(this.leftPos + 84, this.height / 2 - 33);
		}));
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	public void containerTick() {
		super.containerTick();
		this.recipeBookComponent.tick();
	}

	@Override
	public void render(GuiGraphics transform, int x, int y, float partialTicks) {
		if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.renderBg(transform, partialTicks, x, y);
			this.recipeBookComponent.render(transform, x, y, partialTicks);
		} else {
			this.recipeBookComponent.render(transform, x, y, partialTicks);
			super.render(transform, x, y, partialTicks);
			this.recipeBookComponent.renderGhostRecipe(transform, this.leftPos, this.topPos, true, partialTicks);
		}

		this.renderTooltip(transform, x, y);
		this.recipeBookComponent.renderTooltip(transform, this.leftPos, this.topPos, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		transform.blit(BG_LOCATION, left, top, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.isLit()) {
			int litProgress = this.menu.getLitProgress();
			transform.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 12 - litProgress, left + 56, top + 36 + 12 - litProgress, 14, litProgress + 1);
		}

		int progress = this.menu.getBurnProgress();
		transform.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, left + 79, top + 34, progress + 1, 16);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (this.recipeBookComponent.mouseClicked(x, y, mouseButton)) {
			return true;
		}
		return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(x, y, mouseButton);
	}

	@Override
	protected void slotClicked(Slot clickedSlot, int x, int y, ClickType type) {
		super.slotClicked(clickedSlot, x, y, type);
		this.recipeBookComponent.slotClicked(clickedSlot);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		return !this.recipeBookComponent.keyPressed(key, scanCode, modifiers) && super.keyPressed(key, scanCode, modifiers);
	}

	@Override
	protected boolean hasClickedOutside(double x, double y, int left, int top, int mouseButton) {
		boolean flag = x < (double)left || y < (double)top || x >= (double)(left + this.imageWidth) || y >= (double)(top + this.imageHeight);
		return this.recipeBookComponent.hasClickedOutside(x, y, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
	}

	@Override
	public boolean charTyped(char code, int modifiers) {
		return this.recipeBookComponent.charTyped(code, modifiers) || super.charTyped(code, modifiers);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookComponent.recipesUpdated();
	}

	@Override
	public RecipeBookComponent getRecipeBookComponent() {
		return this.recipeBookComponent;
	}
}
