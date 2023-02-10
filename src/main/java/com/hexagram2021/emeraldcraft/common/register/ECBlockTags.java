package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBlockTags {
	public static final TagKey<Block> GINKGO_LOGS = create("ginkgo_logs");
	public static final TagKey<Block> PALM_LOGS = create("palm_logs");
	public static final TagKey<Block> PEACH_LOGS = create("peach_logs");
	public static final TagKey<Block> PURPURACEUS_STEMS = create("purpuraceus_stems");

	public static final TagKey<Block> TORCHES = create("torches");

	private static TagKey<Block> create(String name) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MODID, name));
	}
}
