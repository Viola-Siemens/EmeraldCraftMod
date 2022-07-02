package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Map;

public class IceMakerRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final FluidType inputFluid;
	protected final int inputAmount;
	protected final ItemStack result;
	protected final int freezingTime;

	public static Map<ResourceLocation, IceMakerRecipe> recipeList = Collections.emptyMap();

	public static int FREEZING_TIME = 50;
	public static int DEFAULT_INPUT_AMOUNT = 100;

	public IceMakerRecipe(ResourceLocation id, String group, FluidType inputFluid, int inputAmount, ItemStack result, int freezingTime) {
		this.id = id;
		this.group = group;
		this.inputFluid = inputFluid;
		this.inputAmount = inputAmount;
		this.result = result;
		this.freezingTime = freezingTime;
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.ICE_MAKER_SERIALIZER.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.ICE_MAKER);
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public FluidType getFluidType() {
		return this.inputFluid;
	}

	public int getFluidAmount() {
		return this.inputAmount;
	}

	public int getFreezingTime() {
		return this.freezingTime;
	}

	@Override
	public ItemStack assemble(Container container) {
		return this.result.copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.result;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return this.inputFluid.getID() == ((IceMakerBlockEntity)container).getInputFluidTypeIndex();
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.ICE_MAKER_TYPE;
	}
}
