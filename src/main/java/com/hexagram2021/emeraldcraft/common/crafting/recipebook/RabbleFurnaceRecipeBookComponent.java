package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class RabbleFurnaceRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
	private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.boilable");

	@Override
	public void setupGhostRecipe(RecipeHolder<?> recipeHolder, List<Slot> slots) {
		ItemStack itemstack = recipeHolder.value().getResultItem(Objects.requireNonNull(this.minecraft.level).registryAccess());
		this.ghostRecipe.setRecipe(recipeHolder);
		this.ghostRecipe.addIngredient(Ingredient.of(itemstack), (slots.get(4)).x, (slots.get(4)).y);
		NonNullList<Ingredient> ingredients = recipeHolder.value().getIngredients();
		Slot slot = slots.get(3);
		if (slot.getItem().isEmpty()) {
			if (this.fuels == null) {
				this.fuels = Ingredient.of(this.getFuelItems().stream().map(ItemStack::new));
			}

			this.ghostRecipe.addIngredient(this.fuels, slot.x, slot.y);
		}

		Iterator<Ingredient> iterator = ingredients.iterator();

		for(int i = 0; i < 3; ++i) {
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
