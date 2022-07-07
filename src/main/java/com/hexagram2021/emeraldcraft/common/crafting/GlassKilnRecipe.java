package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Collections;
import java.util.Map;

public class GlassKilnRecipe extends AbstractCookingRecipe {
	public static CachedRecipeList<GlassKilnRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.GLASS_KILN_TYPE,
			GlassKilnRecipe.class
	);

	public GlassKilnRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
		super(ECRecipes.GLASS_KILN_TYPE, id, group, ingredient, result, experience, cookingTime);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.GLASS_KILN_SERIALIZER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.GLASS_KILN);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}
}
