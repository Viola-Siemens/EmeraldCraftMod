package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.client.screens.GlassKilnScreen;
import com.hexagram2021.emeraldcraft.client.screens.IceMakerScreen;
import com.hexagram2021.emeraldcraft.client.screens.MelterScreen;
import com.hexagram2021.emeraldcraft.client.screens.MineralTableScreen;
import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.crafting.menu.*;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@JeiPlugin
public class JEIHelper implements IModPlugin {
	public interface ECJEIRecipeTypes {
		RecipeType<CarpentryTableRecipe> CARPENTRY_TABLE = new RecipeType<>(CarpentryTableRecipeCategory.UID, CarpentryTableRecipe.class);
		RecipeType<GlassKilnRecipe> GLASS_KILN = new RecipeType<>(GlassKilnRecipeCategory.UID, GlassKilnRecipe.class);
		RecipeType<MineralTableRecipe> MINERAL_TABLE = new RecipeType<>(MineralTableRecipeCategory.UID, MineralTableRecipe.class);
		RecipeType<IceMakerRecipe> ICE_MAKER = new RecipeType<>(IceMakerRecipeCategory.UID, IceMakerRecipe.class);
		RecipeType<MelterRecipe> MELTER = new RecipeType<>(MelterRecipeCategory.UID, MelterRecipe.class);
		RecipeType<TradeShadowRecipe> TRADES = new RecipeType<>(VillagerTradeCategory.UID,  TradeShadowRecipe.class);
	}

	public interface ECJEIMenuTypes {
		MenuType<CarpentryTableMenu> CARPENTRY_TABLE = new MenuType<>(CarpentryTableMenu::new);
		MenuType<GlassKilnMenu> GLASS_KILN = new MenuType<>(GlassKilnMenu::new);
		MenuType<MineralTableMenu> MINERAL_TABLE = new MenuType<>(MineralTableMenu::new);
		MenuType<IceMakerMenu> ICE_MAKER = new MenuType<>(IceMakerMenu::new);
		MenuType<MelterMenu> MELTER = new MenuType<>(MelterMenu::new);
	}

	private static final ResourceLocation UID = new ResourceLocation(MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) { }

	@Override
	public void registerIngredients(IModIngredientRegistration registry) { }

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		//Recipes
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(
				new CarpentryTableRecipeCategory(guiHelper),
				new GlassKilnRecipeCategory(guiHelper),
				new MineralTableRecipeCategory(guiHelper),
				new MelterRecipeCategory(guiHelper),
				new IceMakerRecipeCategory(guiHelper),
				new VillagerTradeCategory(guiHelper)
		);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) { }

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ECLogger.info("Adding EC recipes to JEI!!");
		registration.addRecipes(ECJEIRecipeTypes.CARPENTRY_TABLE, getRecipes(CarpentryTableRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.GLASS_KILN, getRecipes(GlassKilnRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.MINERAL_TABLE, getRecipes(MineralTableRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.MELTER, getRecipes(MelterRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.ICE_MAKER, getRecipes(IceMakerRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.TRADES, getRecipes(TradeShadowRecipe.recipeList));
	}

	private <T extends Recipe<?>> List<T> getRecipes(CachedRecipeList<T> cachedList) {
		return new ArrayList<>(cachedList.getRecipes(Minecraft.getInstance().level));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(
				CarpentryTableMenu.class,
				ECJEIMenuTypes.CARPENTRY_TABLE,
				ECJEIRecipeTypes.CARPENTRY_TABLE,
				CarpentryTableMenu.INPUT_SLOT, 1,
				CarpentryTableMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				ECJEIMenuTypes.GLASS_KILN,
				ECJEIRecipeTypes.GLASS_KILN,
				GlassKilnMenu.INGREDIENT_SLOT, 1,
				GlassKilnMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				ECJEIMenuTypes.GLASS_KILN,
				RecipeTypes.FUELING,
				GlassKilnMenu.FUEL_SLOT,1,
				GlassKilnMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				MineralTableMenu.class,
				ECJEIMenuTypes.MINERAL_TABLE,
				ECJEIRecipeTypes.MINERAL_TABLE,
				MineralTableMenu.INGREDIENT_SLOT, 1,
				MineralTableMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				ECJEIMenuTypes.MELTER,
				ECJEIRecipeTypes.MELTER,
				MelterMenu.INGREDIENT_SLOT, 1,
				MelterMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				ECJEIMenuTypes.MELTER,
				RecipeTypes.FUELING,
				MelterMenu.FUEL_SLOT,1,
				MelterMenu.INV_SLOT_START, 36
		);
		registration.addRecipeTransferHandler(
				IceMakerMenu.class,
				ECJEIMenuTypes.ICE_MAKER,
				ECJEIRecipeTypes.ICE_MAKER,
				IceMakerMenu.INGREDIENT_INPUT_SLOT, 1,
				IceMakerMenu.INV_SLOT_START, 36
		);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE), ECJEIRecipeTypes.CARPENTRY_TABLE);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.GLASS_KILN), ECJEIRecipeTypes.GLASS_KILN, RecipeTypes.FUELING);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE), ECJEIRecipeTypes.MINERAL_TABLE);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MELTER), ECJEIRecipeTypes.MELTER);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.ICE_MAKER), ECJEIRecipeTypes.ICE_MAKER);
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), ECJEIRecipeTypes.TRADES);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(GlassKilnScreen.class, 78, 32, 28, 23, ECJEIRecipeTypes.GLASS_KILN, RecipeTypes.FUELING);
		registration.addRecipeClickArea(MineralTableScreen.class, 97, 16, 14, 30, ECJEIRecipeTypes.MINERAL_TABLE);
		registration.addRecipeClickArea(MelterScreen.class, 63, 32, 28, 23, ECJEIRecipeTypes.MELTER, RecipeTypes.FUELING);
		registration.addRecipeClickArea(IceMakerScreen.class, 96, 32, 28, 23, ECJEIRecipeTypes.ICE_MAKER);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) { }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) { }
}
