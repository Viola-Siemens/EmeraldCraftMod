package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.MineralTableMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MineralTableRecipeCategory implements IRecipeCategory<MineralTableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "mineral_table");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_mineral_table.png");

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable slotDrawable;
	private final IDrawableAnimated arrow;
	private final IDrawableAnimated bubbles;
	private final IDrawableStatic blazeHeat;

	public MineralTableRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 64, 60)
				.addPadding(1, 0, 0, 50)
				.build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE));

		arrow = guiHelper.drawableBuilder(TEXTURE, 64, 0, 9, 28)
				.buildAnimated(400, IDrawableAnimated.StartDirection.TOP, false);

		ITickTimer bubblesTickTimer = new BubblesTickTimer(guiHelper);
		bubbles = guiHelper.drawableBuilder(TEXTURE, 73, 0, 12, 29)
				.buildAnimated(bubblesTickTimer, IDrawableAnimated.StartDirection.BOTTOM);

		blazeHeat = guiHelper.createDrawable(TEXTURE, 64, 29, 18, 4);

		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends MineralTableRecipe> getRecipeClass() {
		return MineralTableRecipe.class;
	}

	@Override
	public String getTitle() {
		return new TranslationTextComponent("block.emeraldcraft.mineral_table").getString();
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
	public void setIngredients(MineralTableRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void draw(MineralTableRecipe recipe, MatrixStack poseStack, double mouseX, double mouseY) {
		blazeHeat.draw(poseStack, 5, 30);
		bubbles.draw(poseStack, 8, 0);
		arrow.draw(poseStack, 42, 2);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MineralTableRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(MineralTableMenu.INGREDIENT_SLOT, true, 23, 2);
		itemStacks.init(MineralTableMenu.RESULT_SLOT, false, 80, 2);

		itemStacks.setBackground(MineralTableMenu.RESULT_SLOT, slotDrawable);

		itemStacks.set(ingredients);
	}

	private static class BubblesTickTimer implements ITickTimer {
		private static final int[] BUBBLE_LENGTHS = new int[]{29, 23, 18, 13, 9, 5, 0};
		private final ITickTimer internalTimer;

		public BubblesTickTimer(IGuiHelper guiHelper) {
			this.internalTimer = guiHelper.createTickTimer(14, BUBBLE_LENGTHS.length - 1, false);
		}

		@Override
		public int getValue() {
			int timerValue = this.internalTimer.getValue();
			return BUBBLE_LENGTHS[timerValue];
		}

		@Override
		public int getMaxValue() {
			return BUBBLE_LENGTHS[0];
		}
	}
}
