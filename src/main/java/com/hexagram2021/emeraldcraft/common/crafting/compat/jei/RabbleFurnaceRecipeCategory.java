package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class RabbleFurnaceRecipeCategory implements IRecipeCategory<RabbleFurnaceRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "rabble_furnace");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_rabble_furnace.png");
	public static final int COOKTIME = 100;

	private final IDrawable background;
	private final IDrawableAnimated animatedFlame;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public RabbleFurnaceRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 120, 54);
		IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 120, 0, 14, 14);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.RABBLE_FURNACE));
		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 120, 14, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	protected IDrawableAnimated getArrow(RabbleFurnaceRecipe recipe) {
		int cookTime = recipe.getRabblingTime();
		if (cookTime <= 0) {
			cookTime = COOKTIME;
		}
		return this.cachedArrows.getUnchecked(cookTime);
	}

	@Override
	public RecipeType<RabbleFurnaceRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.RABBLE_FURNACE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.rabble_furnace");
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
	public void draw(RabbleFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		this.animatedFlame.draw(poseStack, 39, 20);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 62, 18);

		drawExperience(recipe, poseStack, 0);
		drawCookTime(recipe, poseStack, 45);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawExperience(RabbleFurnaceRecipe recipe, PoseStack poseStack, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			Component experienceString = Component.translatable("gui.emeraldcraft.rabble_furnace.experience", experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			fontRenderer.draw(poseStack, experienceString, this.background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(RabbleFurnaceRecipe recipe, PoseStack poseStack, int y) {
		int cookTime = recipe.getRabblingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.rabble_furnace.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RabbleFurnaceRecipe recipe, IFocusGroup focuses) {
		List<Ingredient> list = recipe.getIngredients();
		builder.addSlot(RecipeIngredientRole.INPUT, 39, 1).addIngredients(list.get(0));
		if(list.size() > 1) {
			builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addIngredients(list.get(1));
			if(list.size() > 2) {
				builder.addSlot(RecipeIngredientRole.INPUT, 1, 23).addIngredients(list.get(2));
			}
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 19).addItemStack(recipe.getResultItem());
	}

	@Override
	public boolean isHandled(RabbleFurnaceRecipe recipe) {
		return !recipe.isSpecial();
	}
}
