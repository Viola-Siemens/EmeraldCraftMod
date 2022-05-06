package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableMenu;
import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CarpentryTableCategory implements IRecipeCategory<CarpentryTableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "carpentry");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_vanilla.png");

	public static final int width = 82;
	public static final int height = 34;

	private final IDrawable background;
	private final IDrawable icon;

	public CarpentryTableCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 220, width, height);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends CarpentryTableRecipe> getRecipeClass() {
		return CarpentryTableRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("block.emeraldcraft.carpentry_table");
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
	public void setIngredients(CarpentryTableRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CarpentryTableRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(CarpentryTableMenu.INPUT_SLOT, true, 0, 8);
		guiItemStacks.init(CarpentryTableMenu.RESULT_SLOT, false, 60, 8);

		guiItemStacks.set(ingredients);
	}

	@Override
	public boolean isHandled(CarpentryTableRecipe recipe) {
		return !recipe.isSpecial();
	}
}
