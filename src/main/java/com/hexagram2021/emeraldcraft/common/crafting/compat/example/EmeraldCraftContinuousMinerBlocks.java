package com.hexagram2021.emeraldcraft.common.crafting.compat.example;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.api.continuous_miner.ContinuousMinerCustomLoot;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.util.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class EmeraldCraftContinuousMinerBlocks {
	public static void init() {
		ContinuousMinerCustomLoot.addBlockLoot(
				ImmutableList.of(ECBlocks.Decoration.VITRIFIED_SAND.getId()),
				new ResourceLocation(MODID, "continuous_miner/ores/obsidian")
		);

		ContinuousMinerCustomLoot.addBlockLoot(ImmutableList.of(
				ECBlocks.Plant.GINKGO_LOG.getId(),
				ECBlocks.Plant.STRIPPED_GINKGO_LOG.getId(),
				ECBlocks.Plant.GINKGO_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_GINKGO_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/ginkgo_logs"));
		ContinuousMinerCustomLoot.addBlockLoot(ImmutableList.of(
				ECBlocks.Plant.PALM_LOG.getId(),
				ECBlocks.Plant.STRIPPED_PALM_LOG.getId(),
				ECBlocks.Plant.PALM_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_PALM_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/palm_logs"));
		ContinuousMinerCustomLoot.addBlockLoot(ImmutableList.of(
				ECBlocks.Plant.PEACH_LOG.getId(),
				ECBlocks.Plant.STRIPPED_PEACH_LOG.getId(),
				ECBlocks.Plant.PEACH_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_PEACH_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/peach_logs"));
	}
}
