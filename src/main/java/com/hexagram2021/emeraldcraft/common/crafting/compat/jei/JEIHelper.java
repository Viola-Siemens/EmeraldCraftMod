package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.GlassKilnScreen;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.IceMakerScreen;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.MelterScreen;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.MineralTableScreen;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
	}

	private static final ResourceLocation UID = new ResourceLocation(MODID, "main");
	public static IDrawableStatic slotDrawable;

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
				new IceMakerRecipeCategory(guiHelper)
		);

		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) { }

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ECLogger.info("Adding recipes to JEI!!");
		registration.addRecipes(ECJEIRecipeTypes.CARPENTRY_TABLE, getRecipes(CarpentryTableRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.GLASS_KILN, getRecipes(GlassKilnRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.MINERAL_TABLE, getRecipes(MineralTableRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.MELTER, getRecipes(MelterRecipe.recipeList));
		registration.addRecipes(ECJEIRecipeTypes.ICE_MAKER, getRecipes(IceMakerRecipe.recipeList));
	}

	private <T extends Recipe<?>> List<T> getRecipes(CachedRecipeList<T> cachedList) {
		return new ArrayList<>(cachedList.getRecipes(Minecraft.getInstance().level));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				ECJEIRecipeTypes.GLASS_KILN,
				GlassKilnMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				RecipeTypes.FUELING,
				GlassKilnMenu.FUEL_SLOT,1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MineralTableMenu.class,
				ECJEIRecipeTypes.MINERAL_TABLE,
				MineralTableMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				ECJEIRecipeTypes.MELTER,
				MelterMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				RecipeTypes.FUELING,
				MelterMenu.FUEL_SLOT,1,
				3, 36
		);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE), ECJEIRecipeTypes.CARPENTRY_TABLE);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.GLASS_KILN), ECJEIRecipeTypes.GLASS_KILN, RecipeTypes.FUELING);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE), ECJEIRecipeTypes.MINERAL_TABLE);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MELTER), ECJEIRecipeTypes.MELTER);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.ICE_MAKER), ECJEIRecipeTypes.ICE_MAKER);
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
