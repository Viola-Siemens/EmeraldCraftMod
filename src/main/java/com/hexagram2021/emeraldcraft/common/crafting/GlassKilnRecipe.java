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

public class GlassKilnRecipe extends AbstractCookingRecipe {
	public static CachedRecipeList<GlassKilnRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.GLASS_KILN_TYPE,
			GlassKilnRecipe.class
	);

	public GlassKilnRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
		super(ECRecipes.GLASS_KILN_TYPE, id, group, ingredient, result, experience, cookingTime);
	}

	@Override @Nonnull
	public IRecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.GLASS_KILN_SERIALIZER.get();
	}

	@Override @Nonnull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.GLASS_KILN);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}
}
