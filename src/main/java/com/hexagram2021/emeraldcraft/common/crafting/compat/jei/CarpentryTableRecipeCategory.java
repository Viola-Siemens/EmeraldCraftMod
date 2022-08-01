package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CarpentryTableRecipeCategory implements IRecipeCategory<CarpentryTableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "carpentry");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_vanilla.png");

	public static final int width = 82;
	public static final int height = 34;

	private final IDrawable background;
	private final IDrawable icon;

	public CarpentryTableRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 220, width, height);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE));
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends CarpentryTableRecipe> getRecipeClass() {
		return CarpentryTableRecipe.class;
	}

	@Override
	public RecipeType<CarpentryTableRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.CARPENTRY_TABLE;
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
	public void setRecipe(IRecipeLayoutBuilder builder, CarpentryTableRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getResultItem());
	}

	@Override
	public boolean isHandled(CarpentryTableRecipe recipe) {
		return !recipe.isSpecial();
	}
}
