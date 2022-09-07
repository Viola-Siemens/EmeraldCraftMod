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
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
@JeiPlugin
public class JEIHelper implements IModPlugin {
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
		registration.addRecipes(getRecipes(CarpentryTableRecipe.recipeList), CarpentryTableRecipeCategory.UID);
		registration.addRecipes(getRecipes(GlassKilnRecipe.recipeList), GlassKilnRecipeCategory.UID);
		registration.addRecipes(getRecipes(MineralTableRecipe.recipeList), MineralTableRecipeCategory.UID);
		registration.addRecipes(getRecipes(MelterRecipe.recipeList), MelterRecipeCategory.UID);
		registration.addRecipes(getRecipes(IceMakerRecipe.recipeList), IceMakerRecipeCategory.UID);
	}

	private <T extends IRecipe<?>> List<T> getRecipes(CachedRecipeList<T> cachedList) {
		return new ArrayList<>(cachedList.getRecipes(Minecraft.getInstance().level));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				GlassKilnRecipeCategory.UID,
				GlassKilnMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				GlassKilnMenu.class,
				VanillaRecipeCategoryUid.FUEL,
				GlassKilnMenu.FUEL_SLOT,1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MineralTableMenu.class,
				MineralTableRecipeCategory.UID,
				MineralTableMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				MelterRecipeCategory.UID,
				MelterMenu.INGREDIENT_SLOT, 1,
				3, 36
		);
		registration.addRecipeTransferHandler(
				MelterMenu.class,
				VanillaRecipeCategoryUid.FUEL,
				MelterMenu.FUEL_SLOT,1,
				3, 36
		);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE), CarpentryTableRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.GLASS_KILN), GlassKilnRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE), MineralTableRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MELTER), MelterRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.ICE_MAKER), IceMakerRecipeCategory.UID);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(GlassKilnScreen.class, 78, 32, 28, 23, GlassKilnRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeClickArea(MineralTableScreen.class, 97, 16, 14, 30, MineralTableRecipeCategory.UID);
		registration.addRecipeClickArea(MelterScreen.class, 63, 32, 28, 23, MelterRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeClickArea(IceMakerScreen.class, 96, 32, 28, 23, IceMakerRecipeCategory.UID);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) { }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) { }
}
