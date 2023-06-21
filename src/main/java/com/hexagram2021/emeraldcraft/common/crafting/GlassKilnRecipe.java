package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class GlassKilnRecipe extends AbstractCookingRecipe {
	private final String category;

	public static final CachedRecipeList<GlassKilnRecipe> recipeList = new CachedRecipeList<>(
			ECRecipes.GLASS_KILN_TYPE,
			GlassKilnRecipe.class
	);

	public GlassKilnRecipe(ResourceLocation id, String group, String category, Ingredient ingredient,
						   ItemStack result, float experience, int cookingTime) {
		super(ECRecipes.GLASS_KILN_TYPE.get(), id, group, CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
		this.category = category;
	}

	@Override @NotNull
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.GLASS_KILN_SERIALIZER.get();
	}

	@Override @NotNull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.GLASS_KILN);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	@NotNull
	public ItemStack getResult() {
		return this.result;
	}

	public String getCategory() {
		return this.category;
	}
}
