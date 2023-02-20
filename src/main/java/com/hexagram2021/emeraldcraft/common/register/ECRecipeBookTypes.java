package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.Arrays;
import java.util.List;

public class ECRecipeBookTypes {
	public static final RecipeBookCategories GLASS_KILN_SEARCH = RecipeBookCategories.create("GLASS_KILN_SEARCH", new ItemStack(Items.COMPASS));
	public static final RecipeBookCategories GLASS_KILN_SAND = RecipeBookCategories.create("GLASS_KILN_SAND", new ItemStack(Items.SAND));
	public static final RecipeBookCategories GLASS_KILN_CLAY = RecipeBookCategories.create("GLASS_KILN_CLAY", new ItemStack(Items.CLAY));
	public static final RecipeBookCategories GLASS_KILN_TERRACOTTA = RecipeBookCategories.create("GLASS_KILN_TERRACOTTA", new ItemStack(Items.CYAN_GLAZED_TERRACOTTA), new ItemStack(Items.MAGENTA_GLAZED_TERRACOTTA));

	public static final RecipeBookCategories RABBLE_FURNACE_SEARCH = RecipeBookCategories.create("RABBLE_FURNACE_SEARCH", new ItemStack(Items.COMPASS));
	public static final RecipeBookCategories RABBLE_FURNACE_RESIN = RecipeBookCategories.create("RABBLE_FURNACE_RESIN", new ItemStack(ECBlocks.Decoration.RESIN_BLOCK));
	public static final RecipeBookCategories RABBLE_FURNACE_PAPER = RecipeBookCategories.create("RABBLE_FURNACE_PAPER", new ItemStack(Items.PAPER));

	public static void init(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(ECRecipes.GLASS_KILN, List.of(GLASS_KILN_SEARCH, GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
		event.registerAggregateCategory(GLASS_KILN_SEARCH, List.of(GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
		event.registerRecipeCategoryFinder(ECRecipes.GLASS_KILN_TYPE.get(), recipe -> {
			if(recipe.getResultItem().getDescriptionId().contains("glass")) {
				return GLASS_KILN_SAND;
			}
			if(Arrays.stream(recipe.getIngredients().get(0).getItems()).anyMatch(itemStack -> itemStack.getDescriptionId().contains("terracotta"))) {
				return GLASS_KILN_TERRACOTTA;
			}
			return GLASS_KILN_CLAY;
		});
		event.registerBookCategories(ECRecipes.RABBLE_FURNACE, List.of(RABBLE_FURNACE_SEARCH, RABBLE_FURNACE_RESIN, RABBLE_FURNACE_PAPER));
		event.registerAggregateCategory(RABBLE_FURNACE_SEARCH, List.of(RABBLE_FURNACE_RESIN, RABBLE_FURNACE_PAPER));
		event.registerRecipeCategoryFinder(ECRecipes.RABBLE_FURNACE_TYPE.get(), recipe -> {
			if(recipe.getResultItem().getDescriptionId().contains("resin")) {
				return RABBLE_FURNACE_RESIN;
			}
			return RABBLE_FURNACE_PAPER;
		});
	}
}
