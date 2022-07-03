package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Map;

public class MelterRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final Ingredient ingredient;
	protected final FluidType resultFluid;
	protected final int resultAmount;
	protected final int meltingTime;

	public static Map<ResourceLocation, MelterRecipe> recipeList = Collections.emptyMap();

	public static int MELTING_TIME = 200;

	public MelterRecipe(ResourceLocation id, String group, Ingredient ingredient, FluidType resultFluid, int resultAmount, int meltingTime) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.resultFluid = resultFluid;
		this.resultAmount = resultAmount;
		this.meltingTime = meltingTime;
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.MELTER_SERIALIZER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.MELTER);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(this.ingredient);
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public FluidType getFluidType() {
		return resultFluid;
	}

	public int getFluidAmount() {
		return resultAmount;
	}

	public int getMeltingTime() {
		return this.meltingTime;
	}

	@Override
	public ItemStack assemble(Container container) {
		return null;
	}

	@Override
	public ItemStack getResultItem() {
		return null;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return this.ingredient.test(container.getItem(MelterMenu.INGREDIENT_SLOT));
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.MELTER_TYPE;
	}
}
