package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.crafting.IPartialMatchRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Optional;

public interface MultiplePartialRecipeCachedCheck<CP extends Container, T extends Recipe<? extends CP> & IPartialMatchRecipe<CP>> {
	@SafeVarargs
	static <CP extends Container, C extends CP, T extends Recipe<C> & IPartialMatchRecipe<CP>> MultiplePartialRecipeCachedCheck<CP, T> createCheck(final RecipeType<? extends T>... recipeTypes) {
		return new MultiplePartialRecipeCachedCheck<>() {
			@SuppressWarnings("unchecked")
			private final PartialRecipeCachedCheck<CP, T>[] checks = Arrays.stream(recipeTypes).map(PartialRecipeCachedCheck::createCheck).toArray(PartialRecipeCachedCheck[]::new);

			@Override
			public Optional<RecipeHolder<T>> getRecipeFor(CP container, Level level) {
				for (PartialRecipeCachedCheck<CP, T> check : checks) {
					Optional<RecipeHolder<T>> optional = check.getRecipeFor(container, level);
					if(optional.isPresent()) {
						return optional;
					}
				}
				return Optional.empty();
			}
		};
	}

	Optional<RecipeHolder<T>> getRecipeFor(CP container, Level level);
}
