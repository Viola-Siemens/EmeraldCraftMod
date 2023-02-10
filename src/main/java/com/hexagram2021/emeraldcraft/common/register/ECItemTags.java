package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECItemTags {
	public static final TagKey<Item> GINKGO_LOGS = create("ginkgo_logs");
	public static final TagKey<Item> PALM_LOGS = create("palm_logs");
	public static final TagKey<Item> PEACH_LOGS = create("peach_logs");
	public static final TagKey<Item> PURPURACEUS_STEMS = create("purpuraceus_stems");

	public static final TagKey<Item> TORCHES = create("torches");

	private static TagKey<Item> create(String name) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MODID, name));
	}
}
