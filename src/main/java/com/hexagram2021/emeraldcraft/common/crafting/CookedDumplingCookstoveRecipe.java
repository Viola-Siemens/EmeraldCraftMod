package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.CookedDumplingCookstoveRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record CookedDumplingCookstoveRecipe(FluidStack fluidStack, Ingredient container, ICookstoveDisplay display,
											int cookTime) implements ICookstoveRecipe {
	public static final int COOK_TIME = 100;

	private static final Ingredient RAW_DUMPLING = Ingredient.of(ECItems.RAW_DUMPLING);
	private static final NonNullList<Ingredient> INGREDIENTS = NonNullList.of(Ingredient.EMPTY, RAW_DUMPLING);
	private static final ItemStack RESULT = new ItemStack(ECItems.COOKED_DUMPLING);

	@Override
	public boolean matches(CookstoveBlockEntity container, Level level) {
		FluidStack inputFluid = container.getFluidStack(CookstoveBlockEntity.TANK_INPUT);
		if(!inputFluid.containsFluid(this.fluidStack)) {
			return false;
		}
		ItemStack template = null;
		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				if(!RAW_DUMPLING.test(itemStack)) {
					return false;
				}
				if(template == null) {
					template = itemStack;
				} else if(!ItemStack.isSameItemSameTags(template, itemStack)) {
					return false;
				}
			}
		}

		return template != null;
	}

	@Override
	public boolean matchesAllowEmpty(Container container) {
		ItemStack template = null;
		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				if(!RAW_DUMPLING.test(itemStack)) {
					return false;
				}
				if(template == null) {
					template = itemStack;
				} else if(!ItemStack.isSameItemSameTags(template, itemStack)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack assemble(CookstoveBlockEntity container, RegistryAccess registryAccess) {
		ItemStack result = RESULT.copy();
		boolean tag = false;
		int cnt = 0;

		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				if(!tag) {
					CompoundTag nbt = itemStack.getTag();
					if(nbt != null) {
						result.setTag(nbt.copy());
						tag = true;
					}
				}
				cnt += 1;
			}
		}
		result.setCount(cnt);

		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return RESULT;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return INGREDIENTS;
	}

	@Override
	public CookedDumplingCookstoveRecipeSerializer<CookedDumplingCookstoveRecipe> getSerializer() {
		return ECRecipeSerializer.COOKED_DUMPLING_COOKSTOVE_SERIALIZER.get();
	}

	@Override
	public RecipeType<CookedDumplingCookstoveRecipe> getType() {
		return ECRecipes.COOKED_DUMPLING_COOKSTOVE_TYPE.get();
	}
}
