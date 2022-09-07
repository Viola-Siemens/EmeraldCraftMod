package com.hexagram2021.emeraldcraft.common.crafting.cache;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface IListRecipe {
	List<? extends IRecipe<?>> getSubRecipes();
}
