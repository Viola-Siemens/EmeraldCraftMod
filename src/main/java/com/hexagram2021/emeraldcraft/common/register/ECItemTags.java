package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECItemTags {
	public static final TagKey<Item> GINKGO_LOGS = create("ginkgo_logs");
	public static final TagKey<Item> PALM_LOGS = create("palm_logs");
	public static final TagKey<Item> PEACH_LOGS = create("peach_logs");
	public static final TagKey<Item> PURPURACEUS_STEMS = create("purpuraceus_stems");

	public static final TagKey<Item> TORCHES = create("torches");

	public static final TagKey<Item> WOMBAT_FOOD = create("wombat_food");

	public static final TagKey<Item> MINCE = create("forge", "mince");
	public static final TagKey<Item> WHEAT_DOUGH = create("forge", "dough/wheat");

	private static TagKey<Item> create(String name) {
		return create(MODID, name);
	}
	private static TagKey<Item> create(String namespace, String name) {
		return TagKey.create(Registries.ITEM, new ResourceLocation(namespace, name));
	}
}
