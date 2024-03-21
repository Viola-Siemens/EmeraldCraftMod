package com.hexagram2021.emeraldcraft.common.crafting.cache;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@SuppressWarnings("unused")
public class CachedRecipeList<R extends Recipe<?>> {
	public static final int INVALID_RELOAD_COUNT = -1;
	private static int reloadCount = 0;

	private final Supplier<RecipeType<R>> type;
	@Nullable
	private Map<ResourceLocation, RecipeHolder<R>> recipes;
	private boolean cachedDataIsClient;
	private int cachedAtReloadCount = INVALID_RELOAD_COUNT;

	public CachedRecipeList(Supplier<RecipeType<R>> type) {
		this.type = type;
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event) {
		++reloadCount;
	}

	@SubscribeEvent
	public static void onRecipeUpdatedClient(RecipesUpdatedEvent event) {
		++reloadCount;
	}

	public static int getReloadCount() {
		return reloadCount;
	}

	public Collection<RecipeHolder<R>> getRecipes(Level level) {
		this.updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(this.recipes).values();
	}

	public Collection<ResourceLocation> getRecipeNames(Level level) {
		this.updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(this.recipes).keySet();
	}

	public RecipeHolder<?> getById(Level level, ResourceLocation name) {
		this.updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(this.recipes).get(name);
	}

	@SuppressWarnings("unchecked")
	private void updateCache(RecipeManager manager, boolean isClient) {
		if(this.recipes != null && this.cachedAtReloadCount == reloadCount && (!this.cachedDataIsClient || isClient)) {
			return;
		}
		this.recipes = manager.getRecipes().stream()
				.filter(recipeHolder -> recipeHolder.value().getType().equals(this.type.get()))
				.flatMap(r -> {
					if(r.value() instanceof IListRecipe listRecipe) {
						return listRecipe.getSubRecipes().stream();
					}
					return Stream.of(r);
				})
				.map(rh -> (RecipeHolder<R>)rh)
				.collect(Collectors.toMap(RecipeHolder::id, Function.identity()));
		this.cachedDataIsClient = isClient;
		this.cachedAtReloadCount = reloadCount;
	}
}
