package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MelterRecipe implements IRecipe<IInventory> {
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

	@Override @Nonnull
	public IRecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.MELTER_SERIALIZER.get();
	}

	@Override @Nonnull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.MELTER);
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}

	@Override @Nonnull
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(this.ingredient);
	}

	@Override @Nonnull
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

	@Override @Nonnull
	public ItemStack assemble(@Nonnull IInventory container) {
		return new ItemStack(Items.BARRIER);
	}

	@Override @Nonnull
	public ItemStack getResultItem() {
		return new ItemStack(Items.BARRIER);
	}

	@Override
	public boolean matches(IInventory container, @Nonnull World level) {
		return this.ingredient.test(container.getItem(MelterMenu.INGREDIENT_SLOT));
	}

	@Override @Nonnull
	public ResourceLocation getId() {
		return this.id;
	}

	@Override @Nonnull
	public IRecipeType<?> getType() {
		return ECRecipes.MELTER_TYPE;
	}
}
