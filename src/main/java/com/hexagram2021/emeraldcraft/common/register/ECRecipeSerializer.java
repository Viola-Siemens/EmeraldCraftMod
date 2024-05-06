package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECRecipeSerializer {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

	public static final RegistryObject<CarpentryTableRecipeSerializer<CarpentryTableRecipe>> CARPENTRY_SERIALIZER = REGISTER.register(
			"carpentry", () -> new CarpentryTableRecipeSerializer<>(CarpentryTableRecipe::new)
	);
	public static final RegistryObject<GlassKilnRecipeSerializer<GlassKilnRecipe>> GLASS_KILN_SERIALIZER = REGISTER.register(
			"glass_kiln", () -> new GlassKilnRecipeSerializer<>(GlassKilnRecipe::new, 100)
	);
	public static final RegistryObject<MineralTableRecipeSerializer<MineralTableRecipe>> MINERAL_TABLE_SERIALIZER = REGISTER.register(
			"mineral_table", () -> new MineralTableRecipeSerializer<>(MineralTableRecipe::new, MineralTableRecipe.BURN_TIME)
	);
	public static final RegistryObject<IceMakerRecipeSerializer<IceMakerRecipe>> ICE_MAKER_SERIALIZER = REGISTER.register(
			"ice_maker", () -> new IceMakerRecipeSerializer<>(IceMakerRecipe::new, IceMakerRecipe.FREEZING_TIME)
	);
	public static final RegistryObject<MelterRecipeSerializer<MelterRecipe>> MELTER_SERIALIZER = REGISTER.register(
			"melter", () -> new MelterRecipeSerializer<>(MelterRecipe::new, MelterRecipe.MELTING_TIME)
	);
	public static final RegistryObject<RabbleFurnaceRecipeSerializer<RabbleFurnaceRecipe>> RABBLE_FURNACE_SERIALIZER = REGISTER.register(
			"rabble_furnace", () -> new RabbleFurnaceRecipeSerializer<>(RabbleFurnaceRecipe::new, RabbleFurnaceRecipe.RABBLING_TIME)
	);
	public static final RegistryObject<MeatGrinderRecipeSerializer<MeatGrinderRecipe>> MEAT_GRINDER_SERIALIZER = REGISTER.register(
			"meat_grinder", () -> new MeatGrinderRecipeSerializer<>(MeatGrinderRecipe::new, MeatGrinderRecipe.GRIND_TIME)
	);
	public static final RegistryObject<CookstoveRecipeSerializer<CookstoveRecipe>> COOKSTOVE_SERIALIZER = REGISTER.register(
			"cookstove", () -> new CookstoveRecipeSerializer<>(CookstoveRecipe::new, CookstoveRecipe.COOK_TIME)
	);
	public static final RegistryObject<SimpleCraftingRecipeSerializer<DumplingRecipe>> CRAFTING_DUMPLING_SERIALIZER = REGISTER.register(
			"crafting_dumpling", () -> new SimpleCraftingRecipeSerializer<>(DumplingRecipe::new)
	);
	public static final RegistryObject<TradeShadowRecipeSerializer<TradeShadowRecipe>> TRADE_SHADOW_SERIALIZER = REGISTER.register(
			"trade_shadow", () -> new TradeShadowRecipeSerializer<>(TradeShadowRecipe::new)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
