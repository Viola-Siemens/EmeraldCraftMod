package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.*;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ECBlocks.WorkStation.GLASS_KILN));
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


	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends GlassKilnRecipe> getRecipeClass() {
		return GlassKilnRecipe.class;
	}

	@Override
	public RecipeType<GlassKilnRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.GLASS_KILN;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("block.emeraldcraft.glass_kiln");
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
	public void draw(GlassKilnRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		animatedFlame.draw(poseStack, 1, 20);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 24, 18);

		drawExperience(recipe, poseStack, 0);
		drawCookTime(recipe, poseStack, 45);
	}

	protected void drawExperience(GlassKilnRecipe recipe, PoseStack poseStack, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			TranslatableComponent experienceString = new TranslatableComponent("gui.emeraldcraft.glass_kiln.experience", experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	protected void drawCookTime(GlassKilnRecipe recipe, PoseStack poseStack, int y) {
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			TranslatableComponent timeString = new TranslatableComponent("gui.emeraldcraft.glass_kiln.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, GlassKilnRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem());
	}

	@Override
	public boolean isHandled(GlassKilnRecipe recipe) {
		return !recipe.isSpecial();
	}
}
