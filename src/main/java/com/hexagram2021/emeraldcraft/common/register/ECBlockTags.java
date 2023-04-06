package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECBlockTags {
	public static final TagKey<Block> GINKGO_LOGS = create("ginkgo_logs");
	public static final TagKey<Block> PALM_LOGS = create("palm_logs");
	public static final TagKey<Block> PEACH_LOGS = create("peach_logs");
	public static final TagKey<Block> PURPURACEUS_STEMS = create("purpuraceus_stems");

	public static final TagKey<Block> TORCHES = create("torches");

	public static final TagKey<Block> VOLCANIC_CAVES_LAVA_POOL_REPLACEABLE = create("volcanic_caves_lava_pool_replaceable");

	private static TagKey<Block> create(String name) {
		return TagKey.create(Registries.BLOCK, new ResourceLocation(MODID, name));
	}
}
