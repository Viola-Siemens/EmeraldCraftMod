package com.hexgram2021.emeraldcraft.api.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class ECSerializableRecipe implements Recipe<Container> {
	protected final ItemStack outputDummy;
	protected final RecipeType<?> type;
	protected final ResourceLocation id;

	protected ECSerializableRecipe(ItemStack outputDummy, RecipeType<?> type, ResourceLocation id)
	{
		this.outputDummy = outputDummy;
		this.type = type;
		this.id = id;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}

	@Override
	public ItemStack getToastSymbol()
	{
		return getECSerializer().getIcon();
	}

	@Override
	public boolean matches(Container inv, Level worldIn)
	{
		return false;
	}

	@Override
	public ItemStack assemble(Container inv)
	{
		return this.outputDummy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return false;
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return getECSerializer();
	}

	protected abstract ECRecipeSerializer<?> getECSerializer();

	@Override
	public RecipeType<?> getType()
	{
		return this.type;
	}
}
