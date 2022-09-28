package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MelterRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final Ingredient ingredient;
	protected final FluidType resultFluid;
	protected final int resultAmount;
	protected final int meltingTime;

	public static CachedRecipeList<MelterRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.MELTER_TYPE,
			MelterRecipe.class
	);

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

	@Override @NotNull
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.MELTER_SERIALIZER.get();
	}

	@Override @NotNull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.MELTER);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	@Override @NotNull
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(this.ingredient);
	}

	@Override @NotNull
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

	@Override @Nullable
	public ItemStack assemble(@NotNull Container container) {
		return null;
	}

	@Override @Nullable
	public ItemStack getResultItem() {
		return null;
	}

	@Override
	public boolean matches(Container container, @NotNull Level level) {
		return this.ingredient.test(container.getItem(MelterMenu.INGREDIENT_SLOT));
	}

	@Override @NotNull
	public ResourceLocation getId() {
		return this.id;
	}

	@Override @NotNull
	public RecipeType<?> getType() {
		return ECRecipes.MELTER_TYPE;
	}
}
