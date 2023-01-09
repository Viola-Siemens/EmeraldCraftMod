package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
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

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class IceMakerRecipeCategory implements IRecipeCategory<IceMakerRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "ice_maker");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_ice_maker.png");

	public static final int FREEZETIME = 100;

	private final IDrawable background;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
	private final IDrawableAnimated animatedFlame;
	private final IDrawableStatic[] inputFluids;

	public IceMakerRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 148, 56);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.ICE_MAKER));

		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 148, 8, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});

		IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 148, 0, 32, 8);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.LEFT, true);

		this.inputFluids = new IDrawableStatic[] {
				guiHelper.createDrawable(TEXTURE, 0, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 12, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 24, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 36, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 48, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 60, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 72, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 84, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 96, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 108, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 120, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 132, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 144, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 156, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 168, 56, 12, 49),
				guiHelper.createDrawable(TEXTURE, 180, 56, 12, 49)
		};
	}

	@Override
	public RecipeType<IceMakerRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.ICE_MAKER;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.ice_maker");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(IceMakerRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		animatedFlame.draw(poseStack, 1, 47);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 90, 16);
		drawCookTime(recipe, poseStack, 49);

		inputFluids[recipe.getFluidType().getGUIID()].draw(poseStack, 72, 1);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(IceMakerRecipe recipe, PoseStack poseStack, int y) {
		int freezeTime = recipe.getFreezingTime();
		if (freezeTime > 0) {
			int cookTimeSeconds = freezeTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.ice_maker.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IceMakerRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 19).addItemStack(recipe.getResultItem());
	}

	protected IDrawableAnimated getArrow(IceMakerRecipe recipe) {
		int freezeTime = recipe.getFreezingTime();
		if (freezeTime <= 0) {
			freezeTime = FREEZETIME;
		}
		return this.cachedArrows.getUnchecked(freezeTime);
	}
}
