package com.hexagram2021.emeraldcraft.api.continuous_miner;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class ContinuousMinerCustomLoot {
	private static final Map<ResourceLocation, ResourceLocation> BLOCK_TO_LOOT_TABLE = new HashMap<>();

	public static void addBlockLoot(List<ResourceLocation> blockRegistryNames, ResourceLocation lootTable) {
		blockRegistryNames.forEach((blockRegistryName) -> BLOCK_TO_LOOT_TABLE.put(blockRegistryName, lootTable));
	}

	@Nullable
	public static ResourceLocation getBlockLoot(BlockState blockState) {
		return BLOCK_TO_LOOT_TABLE.get(getRegistryName(blockState.getBlock()));
	}
}
