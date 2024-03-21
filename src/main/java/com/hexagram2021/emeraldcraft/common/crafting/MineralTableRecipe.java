package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MineralTableRecipe extends AbstractCookingRecipe {

	public static final CachedRecipeList<MineralTableRecipe> recipeList = new CachedRecipeList<>(ECRecipes.MINERAL_TABLE_TYPE);

	public static final int BURN_TIME = 500;

	public MineralTableRecipe(String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
		super(ECRecipes.MINERAL_TABLE_TYPE.get(), group, CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.MINERAL_TABLE_SERIALIZER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	public ItemStack getResult() {
		return this.result;
	}
}
