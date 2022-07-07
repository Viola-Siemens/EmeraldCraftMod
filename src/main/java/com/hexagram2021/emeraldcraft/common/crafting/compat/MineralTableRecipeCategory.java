package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE));

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
	public RecipeType<MineralTableRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.MINERAL_TABLE;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("block.emeraldcraft.mineral_table");
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
	public void draw(MineralTableRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		blazeHeat.draw(poseStack, 5, 30);
		bubbles.draw(poseStack, 8, 0);
		arrow.draw(poseStack, 42, 2);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MineralTableRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 24, 3).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 3).addItemStack(recipe.getResultItem()).setBackground(slotDrawable, -1, -1);
	}

	private static class BubblesTickTimer implements ITickTimer {
		@SuppressWarnings("JavadocReference")
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
