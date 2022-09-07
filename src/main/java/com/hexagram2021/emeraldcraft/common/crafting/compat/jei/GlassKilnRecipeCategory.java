package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.*;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class GlassKilnRecipeCategory implements IRecipeCategory<GlassKilnRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "glass_kiln");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_vanilla.png");
	public static final int COOKTIME = 100;

	private final IDrawable background;
	private final IDrawableAnimated animatedFlame;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public GlassKilnRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 114, 82, 54);
		IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 82, 114, 14, 14);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ECBlocks.WorkStation.GLASS_KILN));
		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<Integer, IDrawableAnimated>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 82, 128, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	protected IDrawableAnimated getArrow(GlassKilnRecipe recipe) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = COOKTIME;
		}
		return this.cachedArrows.getUnchecked(cookTime);
	}


	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends GlassKilnRecipe> getRecipeClass() {
		return GlassKilnRecipe.class;
	}

	@Override
	public String getTitle() {
		return new TranslationTextComponent("block.emeraldcraft.glass_kiln").getString();
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setIngredients(GlassKilnRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void draw(GlassKilnRecipe recipe, MatrixStack poseStack, double mouseX, double mouseY) {
		animatedFlame.draw(poseStack, 1, 20);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 24, 18);

		drawExperience(recipe, poseStack, 0);
		drawCookTime(recipe, poseStack, 45);
	}

	protected void drawExperience(GlassKilnRecipe recipe, MatrixStack poseStack, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			TranslationTextComponent experienceString = new TranslationTextComponent("gui.emeraldcraft.glass_kiln.experience", experience);
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	protected void drawCookTime(GlassKilnRecipe recipe, MatrixStack poseStack, int y) {
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			TranslationTextComponent timeString = new TranslationTextComponent("gui.emeraldcraft.glass_kiln.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GlassKilnRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(GlassKilnMenu.INGREDIENT_SLOT, true, 0, 0);
		guiItemStacks.init(GlassKilnMenu.RESULT_SLOT, false, 60, 18);

		guiItemStacks.set(ingredients);
	}

	@Override
	public boolean isHandled(GlassKilnRecipe recipe) {
		return !recipe.isSpecial();
	}
}
