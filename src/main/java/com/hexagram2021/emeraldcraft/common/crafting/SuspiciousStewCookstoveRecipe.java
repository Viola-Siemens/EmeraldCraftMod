package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.SuspiciousStewCookstoveRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraftforge.fluids.FluidStack;

public record SuspiciousStewCookstoveRecipe(FluidStack fluidStack, Ingredient container, ItemStack result, ICookstoveDisplay display,
											int cookTime) implements ICookstoveRecipe {
	public static final int COOK_TIME = 100;

	private static final Ingredient BROWN_MUSHROOM = Ingredient.of(Items.BROWN_MUSHROOM);
	private static final Ingredient RED_MUSHROOM = Ingredient.of(Items.RED_MUSHROOM);
	private static final Ingredient SMALL_FLOWERS = Ingredient.of(ItemTags.SMALL_FLOWERS);
	private static final NonNullList<Ingredient> INGREDIENTS = NonNullList.of(Ingredient.EMPTY, BROWN_MUSHROOM, RED_MUSHROOM, SMALL_FLOWERS);

	@Override
	public boolean matches(CookstoveBlockEntity container, Level level) {
		FluidStack inputFluid = container.getFluidStack(CookstoveBlockEntity.TANK_INPUT);
		if(!inputFluid.containsFluid(this.fluidStack)) {
			return false;
		}

		boolean brown = false;
		boolean red = false;
		boolean flower = false;

		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				if (BROWN_MUSHROOM.test(itemStack)) {
					if(brown) {
						return false;
					}
					brown = true;
				} else if (RED_MUSHROOM.test(itemStack)) {
					if(red) {
						return false;
					}
					red = true;
				} else if (SMALL_FLOWERS.test(itemStack)) {
					if(flower) {
						return false;
					}
					flower = true;
				} else {
					return false;
				}
			}
		}

		return brown && red && flower;
	}

	@Override
	public boolean matchesAllowEmpty(Container container) {
		boolean brown = false;
		boolean red = false;
		boolean flower = false;

		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemStack = container.getItem(i);
			if (!itemStack.isEmpty()) {
				if (BROWN_MUSHROOM.test(itemStack)) {
					if(brown) {
						return false;
					}
					brown = true;
				} else if (RED_MUSHROOM.test(itemStack)) {
					if(red) {
						return false;
					}
					red = true;
				} else if (SMALL_FLOWERS.test(itemStack)) {
					if(flower) {
						return false;
					}
					flower = true;
				} else {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack assemble(CookstoveBlockEntity container, RegistryAccess registryAccess) {
		ItemStack result = this.result.copy();

		for(int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack ingredient = container.getItem(i);
			if (!ingredient.isEmpty()) {
				SuspiciousEffectHolder effect = SuspiciousEffectHolder.tryGet(ingredient.getItem());
				if (effect != null) {
					SuspiciousStewItem.saveMobEffects(result, effect.getSuspiciousEffects());
					break;
				}
			}
		}

		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return INGREDIENTS;
	}

	@Override
	public SuspiciousStewCookstoveRecipeSerializer<SuspiciousStewCookstoveRecipe> getSerializer() {
		return ECRecipeSerializer.SUSPICIOUS_STEW_COOKSTOVE_SERIALIZER.get();
	}

	@Override
	public RecipeType<SuspiciousStewCookstoveRecipe> getType() {
		return ECRecipes.SUSPICIOUS_STEW_COOKSTOVE_TYPE.get();
	}
}
