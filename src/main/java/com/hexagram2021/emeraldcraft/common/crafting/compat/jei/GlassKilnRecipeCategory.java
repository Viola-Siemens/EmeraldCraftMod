package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.GLASS_KILN));
		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
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
	public RecipeType<GlassKilnRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.GLASS_KILN;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.glass_kiln");
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
	public void draw(GlassKilnRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics transform, double mouseX, double mouseY) {
		animatedFlame.draw(transform, 1, 20);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(transform, 24, 18);

		drawExperience(recipe, transform, 0);
		drawCookTime(recipe, transform, 45);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawExperience(GlassKilnRecipe recipe, GuiGraphics transform, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			Component experienceString = Component.translatable("gui.emeraldcraft.glass_kiln.experience", experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			transform.drawString(fontRenderer, experienceString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(GlassKilnRecipe recipe, GuiGraphics transform, int y) {
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.glass_kiln.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			transform.drawString(fontRenderer, timeString, background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, GlassKilnRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@Override
	public boolean isHandled(GlassKilnRecipe recipe) {
		return !recipe.isSpecial();
	}
}
