package com.hexagram2021.emeraldcraft.common.crafting.compat.jei.replacers;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.CookstoveItemsDisplay;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class CookedDumplingCookstoveRecipeMaker {
	public static Stream<CookstoveRecipe> createRecipesStream() {
		Ingredient rawDumpling = Ingredient.of(ECItems.RAW_DUMPLING);
		return IntStream.rangeClosed(1, CookstoveBlockEntity.COUNT_SLOTS).mapToObj(count -> {
			NonNullList<Ingredient> inputs = NonNullList.withSize(count, Ingredient.EMPTY);
			for(int i = 0; i < count; ++i) {
				inputs.set(i, rawDumpling);
			}
			return new CookstoveRecipe(
					new ResourceLocation(MODID, "jei/cookstove/dumpling_" + count),
					inputs, new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), Ingredient.EMPTY, new ItemStack(ECItems.COOKED_DUMPLING, count),
					new CookstoveItemsDisplay(
							new CookstoveItemsDisplay.Background(0xFCFCFC, new ResourceLocation(MODID, "soup")),
							Ingredient.of(ECItems.COOKED_DUMPLING)
					),
					CookstoveRecipe.COOK_TIME
			);
		});
	}

	private CookedDumplingCookstoveRecipeMaker() {
	}
}
