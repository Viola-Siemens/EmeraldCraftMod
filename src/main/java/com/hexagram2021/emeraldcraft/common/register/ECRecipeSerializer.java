package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.CarpentryTableRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.GlassKilnRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.MineralTableRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
