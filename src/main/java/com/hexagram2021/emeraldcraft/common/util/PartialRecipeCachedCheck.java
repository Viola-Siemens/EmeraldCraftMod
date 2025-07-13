package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.crafting.IPartialMatchRecipe;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public interface PartialRecipeCachedCheck<CP extends Container, T extends Recipe<? extends CP> & IPartialMatchRecipe<CP>> {
    static <CP extends Container, C extends CP, T extends Recipe<C> & IPartialMatchRecipe<CP>> Optional<Pair<ResourceLocation, T>> getRecipeFor(RecipeManager recipeManager, RecipeType<T> recipeType, CP container, @Nullable ResourceLocation lastRecipe) {
        Map<ResourceLocation, T> map = recipeManager.byType(recipeType);
        if (lastRecipe != null) {
            T recipe = map.get(lastRecipe);
            if (recipe != null && recipe.matchesAllowEmpty(container)) {
                return Optional.of(Pair.of(lastRecipe, recipe));
            }
        }

        return map.entrySet().stream()
                .filter(entry -> entry.getValue().matchesAllowEmpty(container)).findFirst()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    static <CP extends Container, C extends CP, T extends Recipe<C> & IPartialMatchRecipe<CP>> PartialRecipeCachedCheck<CP, T> createCheck(final RecipeType<T> recipeType) {
        return new PartialRecipeCachedCheck<>() {
            @Nullable
            private ResourceLocation lastRecipe;

            @Override
            public Optional<T> getRecipeFor(CP container, Level level) {
                RecipeManager recipeManager = level.getRecipeManager();
                Optional<Pair<ResourceLocation, T>> optional = PartialRecipeCachedCheck.getRecipeFor(recipeManager, recipeType, container, this.lastRecipe);
                if (optional.isPresent()) {
                    Pair<ResourceLocation, T> pair = optional.get();
                    this.lastRecipe = pair.getFirst();
                    return Optional.of(pair.getSecond());
                }
                return Optional.empty();
            }
        };
    }

    static <CP extends Container, T extends Recipe<? extends CP> & IPartialMatchRecipe<CP>> PartialRecipeCachedCheck<CP, T> createDummy() {
        return (container, level) -> Optional.empty();
    }

    Optional<T> getRecipeFor(CP container, Level level);
}
