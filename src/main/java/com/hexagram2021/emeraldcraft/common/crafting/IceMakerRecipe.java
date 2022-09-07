package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class IceMakerRecipe implements IRecipe<IInventory> {
	protected final ResourceLocation id;
	protected final String group;
	protected final FluidType inputFluid;
	protected final int inputAmount;
	protected final ItemStack result;
	protected final int freezingTime;

	public static CachedRecipeList<IceMakerRecipe> recipeList = new CachedRecipeList<>(
			() -> ECRecipes.ICE_MAKER_TYPE,
			IceMakerRecipe.class
	);

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

	@Override @Nonnull
	public IRecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.ICE_MAKER_SERIALIZER.get();
	}

	@Override @Nonnull
	public ItemStack getToastSymbol() {
		return new ItemStack(ECBlocks.WorkStation.ICE_MAKER);
	}

	@Override @Nonnull
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

	@Override @Nonnull
	public ItemStack assemble(@Nonnull IInventory container) {
		return this.result.copy();
	}

	@Override @Nonnull
	public ItemStack getResultItem() {
		return this.result;
	}

	@Override
	public boolean matches(@Nonnull IInventory container, @Nonnull World level) {
		return FluidTypes.getID(this.inputFluid) == ((IceMakerBlockEntity)container).getInputFluidTypeIndex();
	}

	@Override @Nonnull
	public ResourceLocation getId() {
		return this.id;
	}

	@Override @Nonnull
	public IRecipeType<?> getType() {
		return ECRecipes.ICE_MAKER_TYPE;
	}
}
