package com.hexagram2021.emeraldcraft.common.crafting.cache;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public interface IListRecipe {
	List<? extends RecipeHolder<Recipe<?>>> getSubRecipes();
}
