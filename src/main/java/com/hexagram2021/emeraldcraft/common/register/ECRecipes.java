package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipes {
	public static final RecipeBookType GLASS_KILN = RecipeBookType.create("GLASS_KILN");
	public static final RecipeBookType RABBLE_FURNACE = RecipeBookType.create("RABBLE_FURNACE");

	private static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<CarpentryTableRecipe>> CARPENTRY_TABLE_TYPE = register("carpentry");
	public static final DeferredHolder<RecipeType<?>, RecipeType<GlassKilnRecipe>> GLASS_KILN_TYPE = register("glass_kiln");
	public static final DeferredHolder<RecipeType<?>, RecipeType<MineralTableRecipe>> MINERAL_TABLE_TYPE = register("mineral_table");
	public static final DeferredHolder<RecipeType<?>, RecipeType<IceMakerRecipe>> ICE_MAKER_TYPE = register("ice_maker");
	public static final DeferredHolder<RecipeType<?>, RecipeType<MelterRecipe>> MELTER_TYPE = register("melter");
	public static final DeferredHolder<RecipeType<?>, RecipeType<RabbleFurnaceRecipe>> RABBLE_FURNACE_TYPE = register("rabble_furnace");
	public static final DeferredHolder<RecipeType<?>, RecipeType<MeatGrinderRecipe>> MEAT_GRINDER_TYPE = register("meat_grinder");
	public static final DeferredHolder<RecipeType<?>, RecipeType<CookstoveRecipe>> COOKSTOVE_TYPE = register("cookstove");
	public static final DeferredHolder<RecipeType<?>, RecipeType<SuspiciousStewCookstoveRecipe>> SUSPICIOUS_STEW_COOKSTOVE_TYPE = register("suspicious_stew_cookstove");
	public static final DeferredHolder<RecipeType<?>, RecipeType<CookedDumplingCookstoveRecipe>> COOKED_DUMPLING_COOKSTOVE_TYPE = register("cooked_dumpling_cookstove");
	public static final DeferredHolder<RecipeType<?>, RecipeType<TradeShadowRecipe>> TRADE_SHADOW_TYPE = register("trade_shadow");

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}

	private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String name) {
		return REGISTER.register(name, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return new ResourceLocation(MODID, name).toString();
			}
		});
	}
}
