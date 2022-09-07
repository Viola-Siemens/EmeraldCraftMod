package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CarpentryTableRecipe extends SingleItemRecipe {
	public static final CachedRecipeList<CarpentryTableRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.CARPENTRY_TABLE_TYPE,
			CarpentryTableRecipe.class
	);

	public CarpentryTableRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
		super(ECRecipes.CARPENTRY_TABLE_TYPE, ECRecipeSerializer.CARPENTRY_SERIALIZER.get(), id, group, ingredient, result);
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(IInventory inv, @Nonnull World level) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override @Nonnull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}
}