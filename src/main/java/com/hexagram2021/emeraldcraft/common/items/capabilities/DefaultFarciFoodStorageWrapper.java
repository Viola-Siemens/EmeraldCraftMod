package com.hexagram2021.emeraldcraft.common.items.capabilities;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.food.FoodProperties;

public record DefaultFarciFoodStorageWrapper(FoodProperties foodProperties) implements IFoodStorage {
	@Override
	public void setFoodTag(ListTag tag) {
	}
}
