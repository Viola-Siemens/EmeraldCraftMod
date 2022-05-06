package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.GlassKilnScreen;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

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
				new CarpentryTableCategory(guiHelper),
				new GlassKilnRecipeCategory(guiHelper),
				new MineralTableRecipeCategory(guiHelper)
		);

		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) { }

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ECLogger.info("Adding recipes to JEI!!");
		registration.addRecipes(new ArrayList<>(CarpentryTableRecipe.recipeList.values()), CarpentryTableCategory.UID);
		registration.addRecipes(new ArrayList<>(GlassKilnRecipe.recipeList.values()), GlassKilnRecipeCategory.UID);
		registration.addRecipes(new ArrayList<>(MineralTableRecipe.recipeList.values()), MineralTableRecipeCategory.UID);
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
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE), CarpentryTableCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.GLASS_KILN), GlassKilnRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeCatalyst(new ItemStack(ECBlocks.WorkStation.MINERAL_TABLE), MineralTableRecipeCategory.UID);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(GlassKilnScreen.class, 78, 32, 28, 23, GlassKilnRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
		registration.addRecipeClickArea(MineralTableScreen.class, 97, 16, 14, 30, MineralTableRecipeCategory.UID);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) { }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) { }
}
