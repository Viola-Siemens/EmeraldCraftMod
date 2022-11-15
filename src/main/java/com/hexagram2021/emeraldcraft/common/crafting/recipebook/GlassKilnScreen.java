package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
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
public class GlassKilnScreen extends AbstractContainerScreen<GlassKilnMenu> implements RecipeUpdateListener {
	private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
	private static final ResourceLocation texture = new ResourceLocation(MODID, "textures/gui/container/glass_kiln.png");
	private static final GlassKilnRecipeBookComponent recipeBookComponent = new GlassKilnRecipeBookComponent();

	private boolean widthTooNarrow;

	public GlassKilnScreen(GlassKilnMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	public void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_97863_) -> {
			recipeBookComponent.toggleVisibility();
			this.leftPos = recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
			((ImageButton)p_97863_).setPosition(this.leftPos + 20, this.height / 2 - 49);
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
	public boolean mouseClicked(double p_97834_, double p_97835_, int p_97836_) {
		if (recipeBookComponent.mouseClicked(p_97834_, p_97835_, p_97836_)) {
			return true;
		}
		return this.widthTooNarrow && recipeBookComponent.isVisible() ? true : super.mouseClicked(p_97834_, p_97835_, p_97836_);
	}

	@Override
	protected void slotClicked(@NotNull Slot p_97848_, int p_97849_, int p_97850_, @NotNull ClickType p_97851_) {
		super.slotClicked(p_97848_, p_97849_, p_97850_, p_97851_);
		recipeBookComponent.slotClicked(p_97848_);
	}

	@Override
	public boolean keyPressed(int p_97844_, int p_97845_, int p_97846_) {
		return !recipeBookComponent.keyPressed(p_97844_, p_97845_, p_97846_) && super.keyPressed(p_97844_, p_97845_, p_97846_);
	}

	@Override
	protected boolean hasClickedOutside(double p_97838_, double p_97839_, int p_97840_, int p_97841_, int p_97842_) {
		boolean flag = p_97838_ < (double)p_97840_ || p_97839_ < (double)p_97841_ || p_97838_ >= (double)(p_97840_ + this.imageWidth) || p_97839_ >= (double)(p_97841_ + this.imageHeight);
		return recipeBookComponent.hasClickedOutside(p_97838_, p_97839_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_97842_) && flag;
	}

	@Override
	public boolean charTyped(char p_97831_, int p_97832_) {
		return recipeBookComponent.charTyped(p_97831_, p_97832_) || super.charTyped(p_97831_, p_97832_);
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
