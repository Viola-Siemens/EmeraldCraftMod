package com.hexagram2021.emeraldcraft.common.crafting.cache;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class CachedRecipeList<R extends Recipe<?>> {
    public static final int INVALID_RELOAD_COUNT = -1;
    private static int reloadCount = 0;

    private final RecipeType<R> type;
    private final Class<R> recipeClass;
    @Nullable
    private Map<ResourceLocation, R> recipes;
    private boolean cachedDataIsClient;
    private int cachedAtReloadCount = INVALID_RELOAD_COUNT;

    public CachedRecipeList(RecipeType<R> type, Class<R> recipeClass) {
        this.type = type;
        this.recipeClass = recipeClass;
    }

    public static void onTagsUpdated() {
        ++reloadCount;
    }

    public static void onRecipeUpdatedClient() {
        ++reloadCount;
    }

    public static int getReloadCount() {
        return reloadCount;
    }

    public Collection<R> getRecipes(Level level) {
        updateCache(level.getRecipeManager(), level.isClientSide());
        return Objects.requireNonNull(this.recipes).values();
    }

    public Collection<ResourceLocation> getRecipeNames(Level level) {
        updateCache(level.getRecipeManager(), level.isClientSide());
        return Objects.requireNonNull(this.recipes).keySet();
    }

    public R getById(Level level, ResourceLocation name) {
        updateCache(level.getRecipeManager(), level.isClientSide());
        return Objects.requireNonNull(this.recipes).get(name);
    }

    private void updateCache(RecipeManager manager, boolean isClient) {
        if (this.recipes != null && this.cachedAtReloadCount == reloadCount && (!this.cachedDataIsClient || isClient)) {
            return;
        }
        this.recipes = manager.getRecipes().stream()
                .filter(iRecipe -> iRecipe.getType() == type)
                .flatMap(r -> {
                    if (r instanceof IListRecipe listRecipe) {
                        return listRecipe.getSubRecipes().stream();
                    }
                    return Stream.of(r);
                })
                .map(this.recipeClass::cast)
                .collect(Collectors.toMap(R::getId, Function.identity()));
        this.cachedDataIsClient = isClient;
        this.cachedAtReloadCount = reloadCount;
    }
}
