package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRecipes {
	public static RecipeType<CarpentryTableRecipe> CARPENTRY_TABLE_TYPE;
	public static RecipeType<GlassKilnRecipe> GLASS_KILN_TYPE;
	public static RecipeType<MineralTableRecipe> MINERAL_TABLE_TYPE;
	public static RecipeType<IceMakerRecipe> ICE_MAKER_TYPE;
	public static RecipeType<MelterRecipe> MELTER_TYPE;

	@SubscribeEvent
	public static void registerRecipeTypes(RegistryEvent.Register<Block> event) {
		CARPENTRY_TABLE_TYPE = register("carpentry");
		GLASS_KILN_TYPE = register("glass_kiln");
		MINERAL_TABLE_TYPE = register("mineral_table");
		ICE_MAKER_TYPE = register("ice_maker");
		MELTER_TYPE = register("melter");
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
