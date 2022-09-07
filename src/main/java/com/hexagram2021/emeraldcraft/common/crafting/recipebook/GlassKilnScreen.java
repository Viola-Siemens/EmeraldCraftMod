package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class GlassKilnScreen extends ContainerScreen<GlassKilnMenu> implements IRecipeShownListener {
	private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
	private static final ResourceLocation texture = new ResourceLocation(MODID, "textures/gui/container/glass_kiln.png");
	private final GlassKilnRecipeBookComponent recipeBookComponent = new GlassKilnRecipeBookComponent();

	private boolean widthTooNarrow;

	public GlassKilnScreen(GlassKilnMenu menu, PlayerInventory inventory, ITextComponent component) {
		super(menu, inventory, component);
	}

	@Override
	public void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
		this.addButton(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
			this.recipeBookComponent.initVisuals(this.widthTooNarrow);
			this.recipeBookComponent.toggleVisibility();
			this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
			((ImageButton)button).setPosition(this.leftPos + 20, this.height / 2 - 49);
		}));
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.recipeBookComponent.tick();
	}

	@Override
	public void render(@Nonnull MatrixStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
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
	protected void renderBg(@Nonnull MatrixStack transform, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(texture);
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
		if (this.recipeBookComponent.mouseClicked(p_97834_, p_97835_, p_97836_)) {
			return true;
		}
		return widthTooNarrow && this.recipeBookComponent.isVisible() ? true : super.mouseClicked(p_97834_, p_97835_, p_97836_);
	}

	@Override
	protected void slotClicked(@Nonnull Slot p_97848_, int p_97849_, int p_97850_, @Nonnull ClickType p_97851_) {
		super.slotClicked(p_97848_, p_97849_, p_97850_, p_97851_);
		this.recipeBookComponent.slotClicked(p_97848_);
	}

	@Override
	public boolean keyPressed(int p_97844_, int p_97845_, int p_97846_) {
		return !this.recipeBookComponent.keyPressed(p_97844_, p_97845_, p_97846_) && super.keyPressed(p_97844_, p_97845_, p_97846_);
	}

	@Override
	protected boolean hasClickedOutside(double p_97838_, double p_97839_, int p_97840_, int p_97841_, int p_97842_) {
		boolean flag = p_97838_ < (double)p_97840_ || p_97839_ < (double)p_97841_ || p_97838_ >= (double)(p_97840_ + this.imageWidth) || p_97839_ >= (double)(p_97841_ + this.imageHeight);
		return this.recipeBookComponent.hasClickedOutside(p_97838_, p_97839_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_97842_) && flag;
	}

	@Override
	public boolean charTyped(char p_97831_, int p_97832_) {
		return this.recipeBookComponent.charTyped(p_97831_, p_97832_) || super.charTyped(p_97831_, p_97832_);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookComponent.recipesUpdated();
	}

	@Override @Nonnull
	public RecipeBookGui getRecipeBookComponent() {
		return this.recipeBookComponent;
	}

	@Override
	public void removed() {
		this.recipeBookComponent.removed();
		super.removed();
	}
}
