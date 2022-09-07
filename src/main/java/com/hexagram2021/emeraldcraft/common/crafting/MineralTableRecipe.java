package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MineralTableRecipe extends AbstractCookingRecipe {

	public static CachedRecipeList<MineralTableRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.MINERAL_TABLE_TYPE,
			MineralTableRecipe.class
	);

	public static int BURN_TIME = 500;

	public MineralTableRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
		super(ECRecipes.MINERAL_TABLE_TYPE, id, group, ingredient, result, experience, cookingTime);
	}

	@Override @Nonnull
	public IRecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.MINERAL_TABLE_SERIALIZER.get();
	}

	@Override @Nonnull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}
}
