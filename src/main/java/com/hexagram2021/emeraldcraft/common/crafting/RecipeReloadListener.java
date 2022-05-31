package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeReloadListener implements ResourceManagerReloadListener {
	private final ServerResources serverResources;

	public RecipeReloadListener(ServerResources serverResources) {
		this.serverResources = serverResources;
	}

	@Override
	public void onResourceManagerReload(@Nonnull ResourceManager resourceManager) {
		if(serverResources!=null) {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(server!=null)
			{
				Iterator<ServerLevel> it = server.getAllLevels().iterator();
				// Should only be false when no players are loaded, so the data will be synced on login
				if(it.hasNext()) {

				}
			}
		}
	}

	RecipeManager clientRecipeManager;

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRecipesUpdated(RecipesUpdatedEvent event) {
		clientRecipeManager = event.getRecipeManager();
		if(!Minecraft.getInstance().hasSingleplayerServer())
			buildRecipeLists(clientRecipeManager);
	}

	public static void buildRecipeLists(RecipeManager recipeManager) {
		Collection<Recipe<?>> recipes = recipeManager.getRecipes();
		// Empty recipe list shouldn't happen, but has been known to be caused by other mods
		if(recipes.size()==0)
			return;

		CarpentryTableRecipe.recipeList = filterRecipes(recipes, CarpentryTableRecipe.class, ECRecipes.CARPENTRY_TABLE_TYPE);
		GlassKilnRecipe.recipeList = filterRecipes(recipes, GlassKilnRecipe.class, ECRecipes.GLASS_KILN_TYPE);
		MineralTableRecipe.recipeList = filterRecipes(recipes, MineralTableRecipe.class, ECRecipes.MINERAL_TABLE_TYPE);
		MelterRecipe.recipeList = filterRecipes(recipes, MelterRecipe.class, ECRecipes.MELTER_TYPE);
		IceMakerRecipe.recipeList = filterRecipes(recipes, IceMakerRecipe.class, ECRecipes.ICE_MAKER_TYPE);
	}

	static <R extends Recipe<?>> Map<ResourceLocation, R> filterRecipes(Collection<Recipe<?>> recipes, Class<R> recipeClass, RecipeType<R> recipeType) {
		return recipes.stream()
				.filter(iRecipe -> iRecipe.getType()==recipeType)
				.map(recipeClass::cast)
				.collect(Collectors.toMap(recipe -> recipe.getId(), recipe -> recipe));
	}
}
