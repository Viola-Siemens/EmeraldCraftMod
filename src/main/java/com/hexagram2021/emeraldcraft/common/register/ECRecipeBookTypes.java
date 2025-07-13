package com.hexagram2021.emeraldcraft.common.register;

public class ECRecipeBookTypes {
    //TODO: 适配自定义配方书
/*    public static final RecipeBookCategories GLASS_KILN_SEARCH = RecipeBookCategories.valueOf("GLASS_KILN_SEARCH");
    public static final RecipeBookCategories GLASS_KILN_SAND = RecipeBookCategories.valueOf("GLASS_KILN_SAND");
    public static final RecipeBookCategories GLASS_KILN_CLAY = RecipeBookCategories.valueOf("GLASS_KILN_CLAY");
    public static final RecipeBookCategories GLASS_KILN_TERRACOTTA = RecipeBookCategories.valueOf("GLASS_KILN_TERRACOTTA");

    public static final RecipeBookCategories RABBLE_FURNACE_SEARCH = RecipeBookCategories.valueOf("RABBLE_FURNACE_SEARCH");
    public static final RecipeBookCategories RABBLE_FURNACE_RESIN = RecipeBookCategories.valueOf("RABBLE_FURNACE_RESIN");
    public static final RecipeBookCategories RABBLE_FURNACE_PAPER = RecipeBookCategories.valueOf("RABBLE_FURNACE_PAPER");*/

    public static void init() {
/*        event.registerBookCategories(ECRecipes.GLASS_KILN, List.of(GLASS_KILN_SEARCH, GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
        event.registerAggregateCategory(GLASS_KILN_SEARCH, List.of(GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
        event.registerRecipeCategoryFinder(ECRecipes.GLASS_KILN_TYPE.get(), recipe -> {
            if(recipe instanceof GlassKilnRecipe glassKilnRecipe) {
                if (glassKilnRecipe.getCategory().equals("sand")) {
                    return GLASS_KILN_SAND;
                }
                if (glassKilnRecipe.getCategory().equals("terracotta")) {
                    return GLASS_KILN_TERRACOTTA;
                }
            }
            return GLASS_KILN_CLAY;
        });
        event.registerBookCategories(ECRecipes.RABBLE_FURNACE, List.of(RABBLE_FURNACE_SEARCH, RABBLE_FURNACE_RESIN, RABBLE_FURNACE_PAPER));
        event.registerAggregateCategory(RABBLE_FURNACE_SEARCH, List.of(RABBLE_FURNACE_RESIN, RABBLE_FURNACE_PAPER));
        event.registerRecipeCategoryFinder(ECRecipes.RABBLE_FURNACE_TYPE.get(), recipe -> {
            if(recipe instanceof RabbleFurnaceRecipe rabbleFurnaceRecipe) {
                if (rabbleFurnaceRecipe.category().equals("resin")) {
                    return RABBLE_FURNACE_RESIN;
                }
            }
            return RABBLE_FURNACE_PAPER;
        });*/
    }
}
