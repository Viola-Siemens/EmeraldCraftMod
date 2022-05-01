package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Map;

public class CarpentryTableRecipe extends SingleItemRecipe {
	public static Map<ResourceLocation, CarpentryTableRecipe> recipeList  = Collections.emptyMap();;

	public CarpentryTableRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
		super(ECRecipes.CARPENTRY_TABLE_TYPE, ECRecipeSerializer.CARPENTRY_SERIALIZER.get(), id, group, ingredient, result);
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(Container inv, Level level) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}
}