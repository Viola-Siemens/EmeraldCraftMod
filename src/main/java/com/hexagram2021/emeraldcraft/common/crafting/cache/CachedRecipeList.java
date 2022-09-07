package com.hexagram2021.emeraldcraft.common.crafting.cache;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class CachedRecipeList<R extends IRecipe<?>> {
	public static final int INVALID_RELOAD_COUNT = -1;
	private static int reloadCount = 0;

	private final Supplier<IRecipeType<R>> type;
	private final Class<R> recipeClass;
	private Map<ResourceLocation, R> recipes;
	private boolean cachedDataIsClient;
	private int cachedAtReloadCount = INVALID_RELOAD_COUNT;

	public CachedRecipeList(Supplier<IRecipeType<R>> type, Class<R> recipeClass) {
		this.type = type;
		this.recipeClass = recipeClass;
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

	public Collection<R> getRecipes(@Nonnull World level) {
		updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(recipes).values();
	}

	public Collection<ResourceLocation> getRecipeNames(@Nonnull World level) {
		updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(recipes).keySet();
	}

	public R getById(@Nonnull World level, ResourceLocation name) {
		updateCache(level.getRecipeManager(), level.isClientSide());
		return recipes.get(name);
	}

	private void updateCache(RecipeManager manager, boolean isClient) {
		if(recipes!=null && cachedAtReloadCount==reloadCount && (!cachedDataIsClient||isClient)) {
			return;
		}
		this.recipes = manager.getRecipes().stream()
				.filter(iRecipe -> iRecipe.getType()==type.get())
				.flatMap(r -> {
					if(r instanceof IListRecipe) {
						return ((IListRecipe) r).getSubRecipes().stream();
					}
					return Stream.of(r);
				})
				.map(recipeClass::cast)
				.collect(Collectors.toMap(R::getId, Function.identity()));
		this.cachedDataIsClient = isClient;
		this.cachedAtReloadCount = reloadCount;
	}
}
