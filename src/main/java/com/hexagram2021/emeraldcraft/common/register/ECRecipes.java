package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ECRecipes {
    //TODO: 适配自定义配方书
    public static final RecipeBookType GLASS_KILN = /*RecipeBookType.valueOf("GLASS_KILN")*/ RecipeBookType.FURNACE;
    public static final RecipeBookType RABBLE_FURNACE = /*RecipeBookType.valueOf("RABBLE_FURNACE")*/ RecipeBookType.FURNACE;

    public static final RecipeType<CarpentryTableRecipe> CARPENTRY_TABLE_TYPE = register("carpentry");
    public static final RecipeType<GlassKilnRecipe> GLASS_KILN_TYPE = register("glass_kiln");
    public static final RecipeType<MineralTableRecipe> MINERAL_TABLE_TYPE = register("mineral_table");
    public static final RecipeType<IceMakerRecipe> ICE_MAKER_TYPE = register("ice_maker");
    public static final RecipeType<MelterRecipe> MELTER_TYPE = register("melter");
    public static final RecipeType<RabbleFurnaceRecipe> RABBLE_FURNACE_TYPE = register("rabble_furnace");
    public static final RecipeType<MeatGrinderRecipe> MEAT_GRINDER_TYPE = register("meat_grinder");
    public static final RecipeType<CookstoveRecipe> COOKSTOVE_TYPE = register("cookstove");
    public static final RecipeType<SuspiciousStewCookstoveRecipe> SUSPICIOUS_STEW_COOKSTOVE_TYPE = register("suspicious_stew_cookstove");

    public static final RecipeType<CookedDumplingCookstoveRecipe> COOKED_DUMPLING_COOKSTOVE_TYPE = register("cooked_dumpling_cookstove");

    public static final RecipeType<TradeShadowRecipe> TRADE_SHADOW_TYPE = register("trade_shadow");

    private static <T extends Recipe<?>> RecipeType<T> register(String name) {
        ResourceLocation id = EmeraldCraft.id(name);
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    public static void init() {
    }
}
