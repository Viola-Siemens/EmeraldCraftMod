package com.hexagram2021.emeraldcraft.common.crafting.recipebook;


import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class GlassKilnRecipeBookComponent extends RecipeBookComponent {
	private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.kilnable");

	@Nullable
	private Ingredient fuels;

	@Override
	protected void initFilterButtonTextures() {
		this.filterButton.initTextureValues(152, 182, 28, 18, RECIPE_BOOK_LOCATION);
	}

	@Override
	public void slotClicked(@Nullable Slot slot) {
		super.slotClicked(slot);
		if (slot != null && slot.index < this.menu.getSize()) {
			this.ghostRecipe.clear();
		}
	}

	@Override
	public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		ItemStack itemstack = recipe.getResultItem(this.minecraft.level.registryAccess());
		this.ghostRecipe.setRecipe(recipe);
		this.ghostRecipe.addIngredient(Ingredient.of(itemstack), (slots.get(2)).x, (slots.get(2)).y);
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		Slot slot = slots.get(1);
		if (slot.getItem().isEmpty()) {
			if (this.fuels == null) {
				this.fuels = Ingredient.of(this.getFuelItems().stream().map(ItemStack::new));
			}

			this.ghostRecipe.addIngredient(this.fuels, slot.x, slot.y);
		}

		Iterator<Ingredient> iterator = ingredients.iterator();

		for(int i = 0; i < 2; ++i) {
			if (!iterator.hasNext()) {
				return;
			}

			Ingredient ingredient = iterator.next();
			if (!ingredient.isEmpty()) {
				Slot slot1 = slots.get(i);
				this.ghostRecipe.addIngredient(ingredient, slot1.x, slot1.y);
			}
		}

	}


	@Override
	protected Component getRecipeFilterName() {
		return FILTER_NAME;
	}

	@SuppressWarnings("deprecation")
	protected Set<Item> getFuelItems() {
		return AbstractFurnaceBlockEntity.getFuel().keySet();
	}
}
