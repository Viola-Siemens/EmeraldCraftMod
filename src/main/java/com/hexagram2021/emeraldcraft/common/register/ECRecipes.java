package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipes {
	public static final RecipeType<CarpentryTableRecipe> CARPENTRY_TABLE_TYPE = register("carpentry");
	public static final RecipeType<GlassKilnRecipe> GLASS_KILN_TYPE = register("glass_kiln");
	public static final RecipeType<MineralTableRecipe> MINERAL_TABLE_TYPE = register("mineral_table");
	public static final RecipeType<IceMakerRecipe> ICE_MAKER_TYPE = register("ice_maker");;
	public static final RecipeType<MelterRecipe> MELTER_TYPE = register("melter");

	public static void registerRecipeTypes() {

	}

	private static <T extends Recipe<?>> RecipeType<T> register(String path) {
		ResourceLocation name = new ResourceLocation(MODID, path);
		return Registry.register(Registry.RECIPE_TYPE, name, new RecipeType<T>() {
			@Override
			public String toString() {
				return name.toString();
			}
		});
	}
}
