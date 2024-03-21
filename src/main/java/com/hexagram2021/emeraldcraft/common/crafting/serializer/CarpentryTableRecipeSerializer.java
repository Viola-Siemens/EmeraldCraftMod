package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public class CarpentryTableRecipeSerializer<T extends CarpentryTableRecipe> extends SingleItemRecipe.Serializer<T> {
	public CarpentryTableRecipeSerializer(SingleItemMaker<T> factory) {
		super(factory);
	}
}
