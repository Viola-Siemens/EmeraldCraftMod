package com.hexagram2021.emeraldcraft.api.fluid;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public enum FluidTypes implements FluidType {
	WATER(0),
	LAVA(1),
	MELTED_EMERALD(2),
	MELTED_IRON(3),
	MELTED_GOLD(4),
	MELTED_COPPER(5),
	RESIN(17);

	private static final Map<String, Supplier<Item>> FLUID_TYPE_ITEM = Maps.newHashMap();
	private static final Map<ResourceLocation, FluidType> BUCKET_FLUID_TYPE = Maps.newHashMap();

	final int guiId;

	static final List<FluidType> ALL_FLUID_TYPES = Lists.newArrayList(FluidTypes.values());
	public static final Map<String, Integer> ALL_FLUID_TYPES_IDS = Util.make(Maps.newHashMap(), map -> {
		FluidType[] fluidTypes = FluidTypes.values();
		for(int i = 0; i < fluidTypes.length; ++i) {
			map.put(fluidTypes[i].toString(), i);
		}
	});

	FluidTypes(int guiId) {
		this.guiId = guiId;
	}

	public static FluidType getFluidFromBucketItem(Item item) {
		if(item == Items.WATER_BUCKET) {
			return WATER;
		}
		if(item == Items.LAVA_BUCKET) {
			return LAVA;
		}
		if(item == ECItems.MELTED_EMERALD_BUCKET.get()) {
			return MELTED_EMERALD;
		}
		if(item == ECItems.MELTED_IRON_BUCKET.get()) {
			return MELTED_IRON;
		}
		if(item == ECItems.MELTED_GOLD_BUCKET.get()) {
			return MELTED_GOLD;
		}
		if(item == ECItems.MELTED_COPPER_BUCKET.get()) {
			return MELTED_COPPER;
		}
		if(item == ECItems.RESIN_BUCKET.get()) {
			return RESIN;
		}
		FluidType ret = BUCKET_FLUID_TYPE.get(getRegistryName(item));
		if(ret == null) {
			throw new IllegalArgumentException("Cannot find fluid from item [" + getRegistryName(item) + "]");
		}
		return ret;
	}

	public static Item getFluidBucketItem(FluidType fluidType) {
		if (FluidTypes.WATER.equals(fluidType)) {
			return Items.WATER_BUCKET;
		}
		if (FluidTypes.LAVA.equals(fluidType)) {
			return Items.LAVA_BUCKET;
		}
		if (FluidTypes.MELTED_EMERALD.equals(fluidType)) {
			return ECItems.MELTED_EMERALD_BUCKET.get();
		}
		if (FluidTypes.MELTED_IRON.equals(fluidType)) {
			return ECItems.MELTED_IRON_BUCKET.get();
		}
		if (FluidTypes.MELTED_GOLD.equals(fluidType)) {
			return ECItems.MELTED_GOLD_BUCKET.get();
		}
		if (FluidTypes.MELTED_COPPER.equals(fluidType)) {
			return ECItems.MELTED_COPPER_BUCKET.get();
		}
		if (FluidTypes.RESIN.equals(fluidType)) {
			return ECItems.RESIN_BUCKET.get();
		}
		Supplier<Item> ret = FLUID_TYPE_ITEM.get(fluidType.toString());
		if(ret == null) {
			throw new IllegalArgumentException("Cannot find item for fluid type [" + fluidType + "]");
		}
		return ret.get();
	}

	public static int getID(FluidType fluidType) {
		int ret = ALL_FLUID_TYPES.indexOf(fluidType);
		if(ret == -1) {
			throw new IllegalArgumentException("Cannot find fluid type [" + fluidType + "]");
		}
		return ret;
	}

	public static FluidType getFluidTypeWithID(int id) {
		return ALL_FLUID_TYPES.get(id);
	}

	public static Item getFluidBucketItemWithID(int id) {
		return getFluidBucketItem(ALL_FLUID_TYPES.get(id));
	}

	public static int getIDFromBucketItem(Item item) {
		return getID(getFluidFromBucketItem(item));
	}

	public static FluidType getFluidTypeFromName(String name) {
		return ALL_FLUID_TYPES.stream().filter(fluidType -> fluidType.toString().equals(name)).findFirst().orElseThrow(
				() -> new IllegalArgumentException("Cannot find fluid type with name [" + name + "]")
		);
	}

	public static boolean isExtraFluidBucket(ItemStack itemStack) {
		return BUCKET_FLUID_TYPE.containsKey(getRegistryName(itemStack.getItem()));
	}

	@Override
	public int getGuiId() {
		return guiId;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}

	/**
	 * API for custom Fluid Type
	 * Call addFluidType before mod construction (if not, you'll get an IllegalArgumentException while recipe parsing "Cannot find fluid type with name").
	 */
	public static void addFluidType(FluidType fluidType, Supplier<Item> bucket, ResourceLocation bucketRegistryName) {
		ALL_FLUID_TYPES_IDS.put(fluidType.toString(), ALL_FLUID_TYPES.size());
		ALL_FLUID_TYPES.add(fluidType);
		FLUID_TYPE_ITEM.put(fluidType.toString(), bucket);
		BUCKET_FLUID_TYPE.put(bucketRegistryName, fluidType);
	}
}