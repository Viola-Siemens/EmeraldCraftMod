package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.RabbleFurnaceMenu;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.RabbleFurnaceRecipeBookComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class RabbleFurnaceScreen extends AbstractContainerScreen<RabbleFurnaceMenu> implements RecipeUpdateListener {
	private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
	private static final ResourceLocation texture = new ResourceLocation(MODID, "textures/gui/container/rabble_furnace.png");
	private static final RabbleFurnaceRecipeBookComponent recipeBookComponent = new RabbleFurnaceRecipeBookComponent();

	private boolean widthTooNarrow;

	public RabbleFurnaceScreen(RabbleFurnaceMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	public void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, button -> {
			recipeBookComponent.toggleVisibility();
			this.leftPos = recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
			((ImageButton)button).setPosition(this.leftPos + 20, this.height / 2 - 49);
		}));
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	public void containerTick() {
		super.containerTick();
		recipeBookComponent.tick();
	}

	@Override
	public void render(@NotNull PoseStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		if (recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.renderBg(transform, partialTicks, x, y);
			recipeBookComponent.render(transform, x, y, partialTicks);
		} else {
			recipeBookComponent.render(transform, x, y, partialTicks);
			super.render(transform, x, y, partialTicks);
			recipeBookComponent.renderGhostRecipe(transform, this.leftPos, this.topPos, true, partialTicks);
		}

		this.renderTooltip(transform, x, y);
		recipeBookComponent.renderTooltip(transform, this.leftPos, this.topPos, x, y);
	}

	@Override
	protected void renderBg(@NotNull PoseStack transform, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.isLit()) {
			int k = this.menu.getLitProgress();
			this.blit(transform, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.menu.getBurnProgress();
		this.blit(transform, i + 79, j + 34, 176, 14, l + 1, 16);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (recipeBookComponent.mouseClicked(x, y, mouseButton)) {
			return true;
		}
		return this.widthTooNarrow && recipeBookComponent.isVisible() || super.mouseClicked(x, y, mouseButton);
	}

	@Override
	protected void slotClicked(@NotNull Slot clickedSlot, int x, int y, @NotNull ClickType type) {
		super.slotClicked(clickedSlot, x, y, type);
		recipeBookComponent.slotClicked(clickedSlot);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		return !recipeBookComponent.keyPressed(key, scanCode, modifiers) && super.keyPressed(key, scanCode, modifiers);
	}

	@Override
	protected boolean hasClickedOutside(double x, double y, int left, int top, int mouseButton) {
		boolean flag = x < (double)left || y < (double)top || x >= (double)(left + this.imageWidth) || y >= (double)(top + this.imageHeight);
		return recipeBookComponent.hasClickedOutside(x, y, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
	}

	@Override
	public boolean charTyped(char code, int modifiers) {
		return recipeBookComponent.charTyped(code, modifiers) || super.charTyped(code, modifiers);
	}

	@Override
	public void recipesUpdated() {
		recipeBookComponent.recipesUpdated();
	}

	@Override @NotNull
	public RecipeBookComponent getRecipeBookComponent() {
		return recipeBookComponent;
	}

	@Override
	public void removed() {
		recipeBookComponent.removed();
		super.removed();
	}
}
