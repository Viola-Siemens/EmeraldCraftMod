package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipes {
	public static IRecipeType<CarpentryTableRecipe> CARPENTRY_TABLE_TYPE;
	public static IRecipeType<GlassKilnRecipe> GLASS_KILN_TYPE;
	public static IRecipeType<MineralTableRecipe> MINERAL_TABLE_TYPE;
	public static IRecipeType<IceMakerRecipe> ICE_MAKER_TYPE;
	public static IRecipeType<MelterRecipe> MELTER_TYPE;

	public static void registerRecipeTypes() {
		CARPENTRY_TABLE_TYPE = register("carpentry");
		GLASS_KILN_TYPE = register("glass_kiln");
		MINERAL_TABLE_TYPE = register("mineral_table");
		ICE_MAKER_TYPE = register("ice_maker");
		MELTER_TYPE = register("melter");
	}

	private static <T extends IRecipe<?>> IRecipeType<T> register(String path) {
		ResourceLocation name = new ResourceLocation(MODID, path);
		return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
			@Override
			public String toString() {
				return name.toString();
			}
		});
	}
}
