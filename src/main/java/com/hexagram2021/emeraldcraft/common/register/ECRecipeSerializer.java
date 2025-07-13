package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ECRecipeSerializer {
    public static final CarpentryTableRecipeSerializer<CarpentryTableRecipe> CARPENTRY_SERIALIZER = register(
            "carpentry", new CarpentryTableRecipeSerializer<>(CarpentryTableRecipe::new)
    );
    public static final GlassKilnRecipeSerializer<GlassKilnRecipe> GLASS_KILN_SERIALIZER = register(
            "glass_kiln", new GlassKilnRecipeSerializer<>(GlassKilnRecipe::new, 100)
    );
    public static final MineralTableRecipeSerializer<MineralTableRecipe> MINERAL_TABLE_SERIALIZER = register(
            "mineral_table", new MineralTableRecipeSerializer<>(MineralTableRecipe::new, MineralTableRecipe.BURN_TIME)
    );
    public static final IceMakerRecipeSerializer<IceMakerRecipe> ICE_MAKER_SERIALIZER = register(
            "ice_maker", new IceMakerRecipeSerializer<>(IceMakerRecipe::new, IceMakerRecipe.FREEZING_TIME)
    );
    public static final MelterRecipeSerializer<MelterRecipe> MELTER_SERIALIZER = register(
            "melter", new MelterRecipeSerializer<>(MelterRecipe::new, MelterRecipe.MELTING_TIME)
    );
    public static final RabbleFurnaceRecipeSerializer<RabbleFurnaceRecipe> RABBLE_FURNACE_SERIALIZER = register(
            "rabble_furnace", new RabbleFurnaceRecipeSerializer<>(RabbleFurnaceRecipe::new, RabbleFurnaceRecipe.RABBLING_TIME)
    );
    public static final MeatGrinderRecipeSerializer<MeatGrinderRecipe> MEAT_GRINDER_SERIALIZER = register(
            "meat_grinder", new MeatGrinderRecipeSerializer<>(MeatGrinderRecipe::new, MeatGrinderRecipe.GRIND_TIME)
    );
    public static final CookstoveRecipeSerializer<CookstoveRecipe> COOKSTOVE_SERIALIZER = register(
            "cookstove", new CookstoveRecipeSerializer<>(CookstoveRecipe::new, CookstoveRecipe.COOK_TIME)
    );
    public static final SuspiciousStewCookstoveRecipeSerializer<SuspiciousStewCookstoveRecipe> SUSPICIOUS_STEW_COOKSTOVE_SERIALIZER = register(
            "suspicious_stew_cookstove", new SuspiciousStewCookstoveRecipeSerializer<>(SuspiciousStewCookstoveRecipe::new, SuspiciousStewCookstoveRecipe.COOK_TIME)
    );
    public static final CookedDumplingCookstoveRecipeSerializer<CookedDumplingCookstoveRecipe> COOKED_DUMPLING_COOKSTOVE_SERIALIZER = register(
            "cooked_dumpling_cookstove", new CookedDumplingCookstoveRecipeSerializer<>(CookedDumplingCookstoveRecipe::new, CookedDumplingCookstoveRecipe.COOK_TIME)
    );
    public static final SimpleCraftingRecipeSerializer<DumplingRecipe> CRAFTING_DUMPLING_SERIALIZER = register(
            "crafting_dumpling", new SimpleCraftingRecipeSerializer<>(DumplingRecipe::new)
    );
    public static final TradeShadowRecipeSerializer<TradeShadowRecipe> TRADE_SHADOW_SERIALIZER = register(
            "trade_shadow", new TradeShadowRecipeSerializer<>(TradeShadowRecipe::new)
    );

    private static <T extends RecipeSerializer<?>> T register(String name, T recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, EmeraldCraft.id(name), recipeSerializer);
    }

    public static void init() {
    }
}
