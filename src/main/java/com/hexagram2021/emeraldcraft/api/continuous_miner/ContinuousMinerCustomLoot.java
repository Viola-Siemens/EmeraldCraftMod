package com.hexagram2021.emeraldcraft.api.continuous_miner;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContinuousMinerCustomLoot {
	private static final Map<ResourceLocation, ResourceLocation> BLOCK_TO_LOOT_TABLE = new HashMap<>();

	public static void addBlockLoot(List<ResourceLocation> blockRegistryNames, ResourceLocation lootTable) {
		blockRegistryNames.forEach((blockRegistryName) -> BLOCK_TO_LOOT_TABLE.put(blockRegistryName, lootTable));
	}

	public static ResourceLocation getBlockLoot(BlockState blockState) {
		return BLOCK_TO_LOOT_TABLE.get(blockState.getBlock().getRegistryName());
	}
}
