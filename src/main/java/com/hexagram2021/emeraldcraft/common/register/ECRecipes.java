package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegisterEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipes {
	public static RecipeType<CarpentryTableRecipe> CARPENTRY_TABLE_TYPE;
	public static RecipeType<GlassKilnRecipe> GLASS_KILN_TYPE;
	public static RecipeType<MineralTableRecipe> MINERAL_TABLE_TYPE;
	public static RecipeType<IceMakerRecipe> ICE_MAKER_TYPE;
	public static RecipeType<MelterRecipe> MELTER_TYPE;

	public static void init(RegisterEvent event) {
		event.register(Registry.RECIPE_TYPE_REGISTRY, helper -> {
			CARPENTRY_TABLE_TYPE = register("carpentry", helper);
			GLASS_KILN_TYPE = register("glass_kiln", helper);
			MINERAL_TABLE_TYPE = register("mineral_table", helper);
			ICE_MAKER_TYPE = register("ice_maker", helper);
			MELTER_TYPE = register("melter", helper);
		});
	}

	private static <T extends Recipe<?>> RecipeType<T> register(String path, RegisterEvent.RegisterHelper<RecipeType<?>> helper) {
		ResourceLocation name = new ResourceLocation(MODID, path);
		RecipeType<T> ret = new RecipeType<>() {
			@Override
			public String toString() {
				return name.toString();
			}
		};
		helper.register(name, ret);
		return ret;
	}
}
