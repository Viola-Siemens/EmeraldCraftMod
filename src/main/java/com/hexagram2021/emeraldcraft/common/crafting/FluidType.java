package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum FluidType {
	water(0),
	lava(1),
	melted_emerald(2),
	melted_iron(3),
	melted_gold(4),
	melted_copper(5);

	final int id;

	public static final FluidType[] FLUID_TYPES = { water, lava, melted_emerald, melted_iron, melted_gold, melted_copper };

	FluidType(int id) {
		this.id = id;
	}

	public static FluidType getFluidFromBucketItem(Item item) {
		if(item == Items.WATER_BUCKET) {
			return water;
		}
		if(item == Items.LAVA_BUCKET) {
			return lava;
		}
		if(item == ECItems.MELTED_EMERALD_BUCKET.get()) {
			return melted_emerald;
		}
		if(item == ECItems.MELTED_IRON_BUCKET.get()) {
			return melted_iron;
		}
		if(item == ECItems.MELTED_GOLD_BUCKET.get()) {
			return melted_gold;
		}
		if(item == ECItems.MELTED_COPPER_BUCKET.get()) {
			return melted_copper;
		}
		throw new IllegalStateException("Cannot find fluid from item [" + item.getRegistryName() + "]");
	}

	public static Item getFluidBucketItem(FluidType fluidType) {
		return switch (fluidType){
			case water -> Items.WATER_BUCKET;
			case lava -> Items.LAVA_BUCKET;
			case melted_emerald -> ECItems.MELTED_EMERALD_BUCKET.get();
			case melted_iron -> ECItems.MELTED_IRON_BUCKET.get();
			case melted_gold -> ECItems.MELTED_GOLD_BUCKET.get();
			case melted_copper -> ECItems.MELTED_COPPER_BUCKET.get();
		};
	}

	public int getID() {
		return id;
	}
}