package com.hexagram2021.emeraldcraft.api.fluid;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public enum FluidTypes implements FluidType {
	water(0),
	lava(1),
	melted_emerald(2),
	melted_iron(3),
	melted_gold(4);

	private static final HashMap<String, Supplier<Item>> FLUID_TYPE_ITEM = new HashMap<>();
	private static final HashMap<ResourceLocation, FluidType> BUCKET_FLUID_TYPE = new HashMap<>();

	final int guiid;

	private static final List<FluidType> FLUID_TYPES = Lists.newArrayList(water, lava, melted_emerald, melted_iron, melted_gold);

	FluidTypes(int guiid) {
		this.guiid = guiid;
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
		FluidType ret = BUCKET_FLUID_TYPE.get(item.getRegistryName());
		if(ret == null) {
			throw new IllegalArgumentException("Cannot find fluid from item [" + item.getRegistryName() + "]");
		}
		return ret;
	}

	public static Item getFluidBucketItem(FluidType fluidType) {
		if (FluidTypes.water.equals(fluidType)) {
			return Items.WATER_BUCKET;
		}
		if (FluidTypes.lava.equals(fluidType)) {
			return Items.LAVA_BUCKET;
		}
		if (FluidTypes.melted_emerald.equals(fluidType)) {
			return ECItems.MELTED_EMERALD_BUCKET.get();
		}
		if (FluidTypes.melted_iron.equals(fluidType)) {
			return ECItems.MELTED_IRON_BUCKET.get();
		}
		if (FluidTypes.melted_gold.equals(fluidType)) {
			return ECItems.MELTED_GOLD_BUCKET.get();
		}
		Supplier<Item> ret = FLUID_TYPE_ITEM.get(fluidType.toString());
		if(ret == null) {
			throw new IllegalArgumentException("Cannot find item for fluid type [" + fluidType + "]");
		}
		return ret.get();
	}

	public static int getID(FluidType fluidType) {
		int ret = FLUID_TYPES.indexOf(fluidType);
		if(ret == -1) {
			throw new IllegalArgumentException("Cannot find fluid type [" + fluidType.toString() + "]");
		}
		return ret;
	}

	public static FluidType getFluidTypeWithID(int id) {
		return FLUID_TYPES.get(id);
	}

	public static Item getFluidBucketItemWithID(int id) {
		return getFluidBucketItem(FLUID_TYPES.get(id));
	}

	public static int getIDFromBucketItem(Item item) {
		return getID(getFluidFromBucketItem(item));
	}

	public static FluidType getFluidTypeFromName(String name) {
		return FLUID_TYPES.stream().filter(fluidType -> fluidType.toString().equals(name)).findFirst().orElseThrow(
				() -> new IllegalArgumentException("Cannot find fluid type with name [" + name + "]")
		);
	}

	public static boolean isExtraFluidBucket(Item item) {
		return BUCKET_FLUID_TYPE.containsKey(item.getRegistryName());
	}

	@Override
	public int getGUIID() {
		return guiid;
	}

	/**
	 * API for custom Fluid Type
	 *
	 * Call addFluidType before mod construction (if not, you'll get an IllegalArgumentException while recipe parsing "Cannot find fluid type with name").
	 */
	public static void addFluidType(FluidType fluidType, Supplier<Item> bucket, ResourceLocation bucketRegistryName) {
		FLUID_TYPES.add(fluidType);
		FLUID_TYPE_ITEM.put(fluidType.toString(), bucket);
		BUCKET_FLUID_TYPE.put(bucketRegistryName, fluidType);
	}
}