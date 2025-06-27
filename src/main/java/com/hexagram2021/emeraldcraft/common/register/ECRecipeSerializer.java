package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipeSerializer {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

	public static final DeferredHolder<RecipeSerializer<?>, CarpentryTableRecipeSerializer<CarpentryTableRecipe>> CARPENTRY_SERIALIZER = REGISTER.register(
			"carpentry", () -> new CarpentryTableRecipeSerializer<>(CarpentryTableRecipe::new)
	);
	public static final DeferredHolder<RecipeSerializer<?>, GlassKilnRecipeSerializer<GlassKilnRecipe>> GLASS_KILN_SERIALIZER = REGISTER.register(
			"glass_kiln", () -> new GlassKilnRecipeSerializer<>(GlassKilnRecipe::new, 100)
	);
	public static final DeferredHolder<RecipeSerializer<?>, MineralTableRecipeSerializer<MineralTableRecipe>> MINERAL_TABLE_SERIALIZER = REGISTER.register(
			"mineral_table", () -> new MineralTableRecipeSerializer<>(MineralTableRecipe::new, MineralTableRecipe.BURN_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, IceMakerRecipeSerializer<IceMakerRecipe>> ICE_MAKER_SERIALIZER = REGISTER.register(
			"ice_maker", () -> new IceMakerRecipeSerializer<>(IceMakerRecipe::new, IceMakerRecipe.FREEZING_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, MelterRecipeSerializer<MelterRecipe>> MELTER_SERIALIZER = REGISTER.register(
			"melter", () -> new MelterRecipeSerializer<>(MelterRecipe::new, MelterRecipe.MELTING_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, RabbleFurnaceRecipeSerializer<RabbleFurnaceRecipe>> RABBLE_FURNACE_SERIALIZER = REGISTER.register(
			"rabble_furnace", () -> new RabbleFurnaceRecipeSerializer<>(RabbleFurnaceRecipe::new, RabbleFurnaceRecipe.RABBLING_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, MeatGrinderRecipeSerializer<MeatGrinderRecipe>> MEAT_GRINDER_SERIALIZER = REGISTER.register(
			"meat_grinder", () -> new MeatGrinderRecipeSerializer<>(MeatGrinderRecipe::new, MeatGrinderRecipe.GRIND_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, CookstoveRecipeSerializer<CookstoveRecipe>> COOKSTOVE_SERIALIZER = REGISTER.register(
			"cookstove", () -> new CookstoveRecipeSerializer<>(CookstoveRecipe::new, CookstoveRecipe.COOK_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, SuspiciousStewCookstoveRecipeSerializer<SuspiciousStewCookstoveRecipe>> SUSPICIOUS_STEW_COOKSTOVE_SERIALIZER = REGISTER.register(
			"suspicious_stew_cookstove", () -> new SuspiciousStewCookstoveRecipeSerializer<>(SuspiciousStewCookstoveRecipe::new, SuspiciousStewCookstoveRecipe.COOK_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, CookedDumplingCookstoveRecipeSerializer<CookedDumplingCookstoveRecipe>> COOKED_DUMPLING_COOKSTOVE_SERIALIZER = REGISTER.register(
			"cooked_dumpling_cookstove", () -> new CookedDumplingCookstoveRecipeSerializer<>(CookedDumplingCookstoveRecipe::new, CookedDumplingCookstoveRecipe.COOK_TIME)
	);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<DumplingRecipe>> CRAFTING_DUMPLING_SERIALIZER = REGISTER.register(
			"crafting_dumpling", () -> new SimpleCraftingRecipeSerializer<>(DumplingRecipe::new)
	);
	public static final DeferredHolder<RecipeSerializer<?>, TradeShadowRecipeSerializer<TradeShadowRecipe>> TRADE_SHADOW_SERIALIZER = REGISTER.register(
			"trade_shadow", () -> new TradeShadowRecipeSerializer<>(TradeShadowRecipe::new)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
