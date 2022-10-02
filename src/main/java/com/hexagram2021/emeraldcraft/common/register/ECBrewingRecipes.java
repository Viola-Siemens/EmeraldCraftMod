package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class ECBrewingRecipes {
	public static void init() {
		addFullRecipe(Potions.WEAKNESS, Potions.LONG_WEAKNESS, null, ECItems.WARPED_WART, ECPotions.HUNGER, ECPotions.LONG_HUNGER, ECPotions.STRONG_HUNGER);
		addFullRecipe(Potions.POISON, Potions.LONG_POISON, Potions.STRONG_POISON, ECItems.WARPED_WART, ECPotions.WITHER, ECPotions.LONG_WITHER, ECPotions.STRONG_WITHER);
		addFullRecipe(Potions.REGENERATION, Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, ECItems.WARPED_WART, ECPotions.ABSORPTION, ECPotions.LONG_ABSORPTION, ECPotions.STRONG_ABSORPTION);
		addFullRecipeWithoutStronger(Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION, ECItems.WARPED_WART, ECPotions.BLINDNESS, ECPotions.LONG_BLINDNESS);
		addFullRecipeWithoutLonger(Potions.HEALING, Potions.STRONG_HEALING, ECItems.WARPED_WART, ECPotions.SATURATION, ECPotions.STRONG_SATURATION);
	}

	@SuppressWarnings("SameParameterValue")
	private static void addFullRecipe(Potion input, Potion input_longer, Potion input_stronger, ItemLike add, Potion origin, Potion longer, Potion stronger) {
		addRecipe(input, add, origin);
		if(input_longer != null) {
			addRecipe(input_longer, add, longer);
		}
		if(input_stronger != null) {
			addRecipe(input_stronger, add, stronger);
		}
		addProlongRecipe(origin, longer);
		addStrengthenRecipe(origin, stronger);
	}

	@SuppressWarnings("SameParameterValue")
	private static void addFullRecipeWithoutStronger(Potion input, Potion input_longer, ItemLike add, Potion origin, Potion longer) {
		addRecipe(input, add, origin);
		if(input_longer != null) {
			addRecipe(input_longer, add, longer);
		}
		addProlongRecipe(origin, longer);
	}

	@SuppressWarnings("SameParameterValue")
	private static void addFullRecipeWithoutLonger(Potion input, Potion input_stronger, ItemLike add, Potion origin, Potion stronger) {
		addRecipe(input, add, origin);
		if(input_stronger != null) {
			addRecipe(input_stronger, add, stronger);
		}
		addStrengthenRecipe(origin, stronger);
	}

	private static void addProlongRecipe(Potion origin, Potion longer) {
		addRecipe(origin, Items.REDSTONE, longer);
	}

	private static void addStrengthenRecipe(Potion origin, Potion stronger) {
		addRecipe(origin, Items.GLOWSTONE_DUST, stronger);
	}

	private static void addRecipe(Potion input, ItemLike add, Potion output) {
		BrewingRecipeRegistry.addRecipe(new ECBrewingRecipe(
				PotionUtils.setPotion(new ItemStack(Items.POTION), input),
				add,
				PotionUtils.setPotion(new ItemStack(Items.POTION), output)
		));
		BrewingRecipeRegistry.addRecipe(new ECBrewingRecipe(
				PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), input),
				add,
				PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), output)
		));
		BrewingRecipeRegistry.addRecipe(new ECBrewingRecipe(
				PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), input),
				add,
				PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), output)
		));
	}

	private static final class ECBrewingRecipe extends BrewingRecipe {
		public ECBrewingRecipe(ItemStack input, ItemLike add, ItemStack output) {
			super(Ingredient.of(input), Ingredient.of(add), output);
		}

		@Override
		public boolean isInput(@NotNull ItemStack input) {
			ItemStack[] itemStacks = this.getInput().getItems();
			for(ItemStack itemstack : itemStacks) {
				if (itemstack.sameItem(input) && PotionUtils.getPotion(input).equals(PotionUtils.getPotion(itemstack))) {
					return true;
				}
			}
			return false;
		}
	}
}
