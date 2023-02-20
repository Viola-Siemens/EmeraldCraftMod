package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipes {
	public static final RecipeBookType GLASS_KILN = RecipeBookType.create("GLASS_KILN");
	public static final RecipeBookType RABBLE_FURNACE = RecipeBookType.create("RABBLE_FURNACE");

	private static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MODID);

	public static final RegistryObject<RecipeType<CarpentryTableRecipe>> CARPENTRY_TABLE_TYPE = register("carpentry");
	public static final RegistryObject<RecipeType<GlassKilnRecipe>> GLASS_KILN_TYPE = register("glass_kiln");
	public static final RegistryObject<RecipeType<MineralTableRecipe>> MINERAL_TABLE_TYPE = register("mineral_table");
	public static final RegistryObject<RecipeType<IceMakerRecipe>> ICE_MAKER_TYPE = register("ice_maker");
	public static final RegistryObject<RecipeType<MelterRecipe>> MELTER_TYPE = register("melter");
	public static final RegistryObject<RecipeType<RabbleFurnaceRecipe>> RABBLE_FURNACE_TYPE = register("rabble_furnace");
	public static final RegistryObject<RecipeType<TradeShadowRecipe>> TRADE_SHADOW_TYPE = register("trade_shadow");

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}

	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
		return REGISTER.register(name, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return new ResourceLocation(MODID, name).toString();
			}
		});
	}
}
