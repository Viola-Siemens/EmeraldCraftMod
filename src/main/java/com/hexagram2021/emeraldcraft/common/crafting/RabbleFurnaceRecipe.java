package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RabbleFurnaceRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final String category;
	protected final Ingredient ingredient;
	@Nullable
	protected final Ingredient mix1;
	@Nullable
	protected final Ingredient mix2;
	protected final ItemStack result;
	protected final float experience;
	protected final int rabblingTime;

	public static final CachedRecipeList<RabbleFurnaceRecipe> recipeList = new CachedRecipeList<>(
			ECRecipes.RABBLE_FURNACE_TYPE,
			RabbleFurnaceRecipe.class
	);

	public static final int RABBLING_TIME = 100;

	public RabbleFurnaceRecipe(ResourceLocation id, String group, String category, Ingredient ingredient, @Nullable Ingredient mix1, @Nullable Ingredient mix2,
							   ItemStack result, float experience, int cookingTime) {
		this.id = id;
		this.group = group;
		this.category = category;
		this.ingredient = ingredient;
		this.mix1 = mix1;
		this.mix2 = mix2;
		this.result = result;
		this.experience = experience;
		this.rabblingTime = cookingTime;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return this.ingredient.test(container.getItem(0)) &&
				(this.mix1 == null || this.mix1.test(container.getItem(1))) &&
				(this.mix2 == null || this.mix2.test(container.getItem(2)));
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

	public int getRabblingTime() {
		return this.rabblingTime;
	}

	public float getExperience() {
		return this.experience;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(this.ingredient);
		if(this.mix1 != null) {
			list.add(this.mix1);
		}
		if(this.mix2 != null) {
			list.add(this.mix2);
		}
		return list;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	public ItemStack getResult() {
		return this.result;
	}

	public String getCategory() {
		return this.category;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.RABBLE_FURNACE_TYPE.get();
	}
}
