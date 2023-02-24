package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
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

public class MelterRecipeCategory implements IRecipeCategory<MelterRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "melter");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_melter.png");

	public static final int MELTTIME = 200;

	private final IDrawable background;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
	private final IDrawableAnimated animatedFlame;
	private final IDrawableStatic[] resultFluids;

	public MelterRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 109, 54);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.MELTER));

		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 109, 14, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});

		IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 109, 0, 14, 14);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

		this.resultFluids = new IDrawableStatic[] {
				guiHelper.createDrawable(TEXTURE, 0, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 12, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 24, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 36, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 48, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 60, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 72, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 84, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 96, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 108, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 120, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 132, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 144, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 156, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 168, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 180, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 192, 54, 12, 49),
				guiHelper.createDrawable(TEXTURE, 204, 54, 12, 49)
		};
	}

	@Override
	public RecipeType<MelterRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.MELTER;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.melter");
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
	public void draw(MelterRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		this.animatedFlame.draw(poseStack, 1, 20);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 24, 18);
		drawCookTime(recipe, poseStack, 18);
		drawFluidAmount(recipe, poseStack, 48);

		this.resultFluids[recipe.getFluidType().getGUIID()].draw(poseStack, 65, 2);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(MelterRecipe recipe, PoseStack poseStack, int y) {
		int meltTime = recipe.getMeltingTime();
		if (meltTime > 0) {
			int cookTimeSeconds = meltTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.melter.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawFluidAmount(MelterRecipe recipe, PoseStack poseStack, int y) {
		int fluidAmount = recipe.getFluidAmount();
		if (fluidAmount > 0) {
			Component amountString = Component.translatable("gui.emeraldcraft.melter.fluid.amount", String.format("%.2f", fluidAmount / 100.0F));
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(amountString);
			fontRenderer.draw(poseStack, amountString, this.background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MelterRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 36).addItemStack(new ItemStack(FluidTypes.getFluidBucketItem(recipe.getFluidType())));
	}

	protected IDrawableAnimated getArrow(MelterRecipe recipe) {
		int meltTime = recipe.getMeltingTime();
		if (meltTime <= 0) {
			meltTime = MELTTIME;
		}
		return this.cachedArrows.getUnchecked(meltTime);
	}

	@Override
	public boolean isHandled(MelterRecipe recipe) {
		return !recipe.isSpecial();
	}
}
