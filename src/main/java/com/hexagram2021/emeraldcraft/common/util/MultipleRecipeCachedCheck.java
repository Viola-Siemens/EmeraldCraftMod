package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Optional;

public interface MultipleRecipeCachedCheck<C extends Container, T extends Recipe<C>> {
	@SafeVarargs
	static <C extends Container, T extends Recipe<C>> MultipleRecipeCachedCheck<C, T> createCheck(final RecipeType<? extends T>... recipeTypes) {
		return new MultipleRecipeCachedCheck<>() {
			@SuppressWarnings("unchecked")
			private final RecipeManager.CachedCheck<C, T>[] checks = Arrays.stream(recipeTypes).map(RecipeManager::createCheck).toArray(RecipeManager.CachedCheck[]::new);

			@Override
			public Optional<T> getRecipeFor(C container, Level level) {
				for (RecipeManager.CachedCheck<C, T> check : checks) {
					Optional<T> optional = check.getRecipeFor(container, level);
					if(optional.isPresent()) {
						return optional;
					}
				}
				return Optional.empty();
			}
		};
	}

	Optional<T> getRecipeFor(C container, Level level);
}
