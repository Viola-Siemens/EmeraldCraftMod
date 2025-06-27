package com.hexagram2021.emeraldcraft.common.items.capabilities;

import com.hexagram2021.emeraldcraft.common.items.foods.FarciFoodItem;
import com.hexagram2021.emeraldcraft.common.register.ECCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class ItemStackFoodHandler implements ICapabilityProvider, INBTSerializable<ListTag> {
	private final FarciFoodStorage foodStorage;
	private final LazyOptional<IFoodStorage> holder;

	public ItemStackFoodHandler(ItemStack itemStack, FarciFoodItem farciFoodItem) {
		this.foodStorage = new FarciFoodStorage(itemStack, farciFoodItem);
		this.holder = LazyOptional.of(() -> this.foodStorage);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		return ECCapabilities.FOOD_CAPABILITY.orEmpty(cap, this.holder);
	}

	@Override
	public ListTag serializeNBT() {
		return this.foodStorage.serializeNBT();
	}

	@Override
	public void deserializeNBT(ListTag nbt) {
		this.foodStorage.deserializeNBT(nbt);
	}
}
