package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public record IceMakerRecipe(String group, FluidStack inputFluid, ItemStack result, int freezingTime) implements Recipe<Container> {
	public static final CachedRecipeList<IceMakerRecipe> recipeList = new CachedRecipeList<>(ECRecipes.ICE_MAKER_TYPE);

	public static final int FREEZING_TIME = 50;

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

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return ((IceMakerBlockEntity)container).getFluidStack(IceMakerBlockEntity.TANK_INPUT).containsFluid(this.inputFluid);
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.ICE_MAKER_TYPE.get();
	}
}
