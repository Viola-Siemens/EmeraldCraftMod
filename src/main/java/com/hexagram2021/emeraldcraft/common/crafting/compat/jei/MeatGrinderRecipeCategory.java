package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.MeatGrinderRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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

public class MeatGrinderRecipeCategory implements IRecipeCategory<MeatGrinderRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "meat_grinder");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_meat_grinder.png");

	public static final int width = 82;
	public static final int height = 46;

	private final IDrawable background;
	private final IDrawable icon;

	public MeatGrinderRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, width, height);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.MEAT_GRINDER));
	}

	@Override
	public RecipeType<MeatGrinderRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.MEAT_GRINDER;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.meat_grinder");
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
	public void draw(MeatGrinderRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics transform, double mouseX, double mouseY) {
		this.drawBonus(recipe, transform, 34);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawBonus(MeatGrinderRecipe recipe, GuiGraphics transform, int y) {
		float bonusChance = recipe.getBonusChance();
		if (bonusChance > 0.0F) {
			Component timeString = Component.translatable("gui.emeraldcraft.meat_grinder.bonus", String.format("%.2f%%", bonusChance * 100.0F));
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			transform.drawString(fontRenderer, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MeatGrinderRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@Override
	public boolean isHandled(MeatGrinderRecipe recipe) {
		return !recipe.isSpecial();
	}
}
