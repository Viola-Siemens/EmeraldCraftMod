package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RecipeBookRegistry;

import java.util.Arrays;
import java.util.List;

public class ECRecipeBookTypes {
	public static final RecipeBookType GLASS_KILN = RecipeBookType.create("GLASS_KILN");

	public static final RecipeBookCategories GLASS_KILN_SEARCH = RecipeBookCategories.create("GLASS_KILN_SEARCH", new ItemStack(Items.COMPASS));
	public static final RecipeBookCategories GLASS_KILN_SAND = RecipeBookCategories.create("GLASS_KILN_SAND", new ItemStack(Items.SAND));
	public static final RecipeBookCategories GLASS_KILN_CLAY = RecipeBookCategories.create("GLASS_KILN_CLAY", new ItemStack(Items.CLAY));
	public static final RecipeBookCategories GLASS_KILN_TERRACOTTA = RecipeBookCategories.create("GLASS_KILN_TERRACOTTA", new ItemStack(Items.CYAN_GLAZED_TERRACOTTA), new ItemStack(Items.MAGENTA_GLAZED_TERRACOTTA));

	public static void init() {
		RecipeBookRegistry.addCategoriesToType(GLASS_KILN, List.of(GLASS_KILN_SEARCH, GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
		RecipeBookRegistry.addAggregateCategories(GLASS_KILN_SEARCH, List.of(GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
		RecipeBookRegistry.addCategoriesFinder(ECRecipes.GLASS_KILN_TYPE, (recipe) -> {
			if(recipe.getResultItem().getDescriptionId().contains("glass")) {
				return GLASS_KILN_SAND;
			}
			if(Arrays.stream(recipe.getIngredients().get(0).getItems()).anyMatch(itemStack -> itemStack.getDescriptionId().contains("terracotta"))) {
				return GLASS_KILN_TERRACOTTA;
			}
			return GLASS_KILN_CLAY;
		});
	}
}
