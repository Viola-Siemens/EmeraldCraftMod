package com.hexagram2021.emeraldcraft.common.items.capabilities;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.Contract;

@AutoRegisterCapability
public interface IFoodStorage {
	@Contract(pure = true)
	FoodProperties foodProperties();

	void setFoodTag(ListTag tag);
}
