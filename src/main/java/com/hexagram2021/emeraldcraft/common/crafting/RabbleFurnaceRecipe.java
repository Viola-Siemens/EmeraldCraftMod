package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record RabbleFurnaceRecipe(String group, String category, Ingredient ingredient, Ingredient mix1, Ingredient mix2,
								  ItemStack result, float experience, int rabblingTime) implements Recipe<Container>, IPartialMatchRecipe<Container> {
	public static final CachedRecipeList<RabbleFurnaceRecipe> recipeList = new CachedRecipeList<>(ECRecipes.RABBLE_FURNACE_TYPE);

	public static final int RABBLING_TIME = 100;

	@Override
	public boolean matches(Container container, Level level) {
		return this.ingredient.test(container.getItem(0)) && (
				(this.mix1.test(container.getItem(1)) && this.mix2.test(container.getItem(2))) ||
						(this.mix1.test(container.getItem(2)) && this.mix2.test(container.getItem(1)))
		);
	}

	@Override
	public boolean matchesAllowEmpty(Container container) {
		boolean empty1 = container.getItem(1).isEmpty();
		boolean empty2 = container.getItem(2).isEmpty();
		boolean mix1 = (empty1 || this.mix1.test(container.getItem(1))) && (empty2 || this.mix2.test(container.getItem(2)));
		boolean mix2 = (empty1 || this.mix2.test(container.getItem(1))) && (empty2 || this.mix1.test(container.getItem(2)));
		return (container.getItem(0).isEmpty() || this.ingredient.test(container.getItem(0))) && (mix1 || mix2);
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.RABBLE_FURNACE_SERIALIZER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.RABBLE_FURNACE);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(this.ingredient);
		list.add(this.mix1);
		list.add(this.mix2);
		return list;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.RABBLE_FURNACE_TYPE.get();
	}
}
