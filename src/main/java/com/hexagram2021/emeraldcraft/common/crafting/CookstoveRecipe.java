package com.hexagram2021.emeraldcraft.common.crafting;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.CookstoveRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record CookstoveRecipe(NonNullList<Ingredient> ingredients, FluidStack fluidStack,
							  Ingredient container, ItemStack result, CookstoveBlockEntity.CookstoveDisplay display,
							  int cookTime, boolean isSimple) implements Recipe<CookstoveBlockEntity>, IPartialMatchRecipe<Container> {
	public static final CachedRecipeList<CookstoveRecipe> recipeList = new CachedRecipeList<>(ECRecipes.COOKSTOVE_TYPE);

	public static final int COOK_TIME = 100;

	public CookstoveRecipe(NonNullList<Ingredient> ingredients, FluidStack fluidStack, Ingredient container, ItemStack result, CookstoveBlockEntity.CookstoveDisplay display, int cookTime) {
		this(ingredients, fluidStack, container, result, display, cookTime, ingredients.stream().allMatch(Ingredient::isSimple));
	}

	@Override
	public boolean matches(CookstoveBlockEntity container, Level level) {
		StackedContents stackedcontents = new StackedContents();
		List<ItemStack> inputs = Lists.newArrayList();
		int count = 0;
		FluidStack inputFluid = container.getFluidStack(CookstoveBlockEntity.TANK_INPUT);
		if(!inputFluid.containsFluid(this.fluidStack)) {
			return false;
		}
		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				count += 1;
				if (this.isSimple) {
					stackedcontents.accountStack(itemStack, 1);
				} else {
					inputs.add(itemStack);
				}
			}
		}

		return count == this.ingredients.size() && (this.isSimple ? stackedcontents.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
	}

	@Override
	public boolean matchesAllowEmpty(Container container) {
		int count = 0;
		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if(itemStack.isEmpty() || this.ingredients.stream().anyMatch(ingredient -> ingredient.test(itemStack))) {
				count += 1;
			}
		}
		return count == container.getContainerSize();
	}

	@Override
	public ItemStack assemble(CookstoveBlockEntity container, RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public CookstoveRecipeSerializer<CookstoveRecipe> getSerializer() {
		return ECRecipeSerializer.COOKSTOVE_SERIALIZER.get();
	}

	@Override
	public RecipeType<CookstoveRecipe> getType() {
		return ECRecipes.COOKSTOVE_TYPE.get();
	}
}
