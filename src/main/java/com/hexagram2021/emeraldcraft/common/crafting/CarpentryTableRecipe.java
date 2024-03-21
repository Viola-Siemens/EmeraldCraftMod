package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

public class CarpentryTableRecipe extends SingleItemRecipe {
	public static final CachedRecipeList<CarpentryTableRecipe> recipeList = new CachedRecipeList<>(ECRecipes.CARPENTRY_TABLE_TYPE);

	public CarpentryTableRecipe(String group, Ingredient ingredient, ItemStack result) {
		super(ECRecipes.CARPENTRY_TABLE_TYPE.get(), ECRecipeSerializer.CARPENTRY_SERIALIZER.get(), group, ingredient, result);
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	public ItemStack getResult() {
		return this.result;
	}
}