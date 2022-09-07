package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class GlassKilnRecipeBookComponent extends RecipeBookGui {
	private static final TextComponent FILTER_NAME = new TranslationTextComponent("gui.recipebook.toggleRecipes.kilnable");

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
	public void setupGhostRecipe(IRecipe<?> p_100122_, List<Slot> p_100123_) {
		ItemStack itemstack = p_100122_.getResultItem();
		this.ghostRecipe.setRecipe(p_100122_);
		this.ghostRecipe.addIngredient(Ingredient.of(itemstack), (p_100123_.get(2)).x, (p_100123_.get(2)).y);
		NonNullList<Ingredient> ingredients = p_100122_.getIngredients();
		Slot slot = p_100123_.get(1);
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
				Slot slot1 = p_100123_.get(i);
				this.ghostRecipe.addIngredient(ingredient, slot1.x, slot1.y);
			}
		}

	}


	@Override @Nonnull
	protected TextComponent getRecipeFilterName() {
		return FILTER_NAME;
	}

	protected Set<Item> getFuelItems() {
		return AbstractFurnaceTileEntity.getFuel().keySet();
	}
}
