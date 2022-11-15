package com.hexagram2021.emeraldcraft.common.crafting.compat.example;

import com.hexagram2021.emeraldcraft.api.continuous_miner.ContinuousMinerCustomLoot;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class EmeraldCraftContinuousMinerBlocks {
	public static void init() {
		ContinuousMinerCustomLoot.addBlockLoot(
				List.of(ECBlocks.Decoration.VITRIFIED_SAND.getId()),
				new ResourceLocation(MODID, "continuous_miner/ores/obsidian")
		);

		ContinuousMinerCustomLoot.addBlockLoot(List.of(
				ECBlocks.Plant.GINKGO_LOG.getId(),
				ECBlocks.Plant.STRIPPED_GINKGO_LOG.getId(),
				ECBlocks.Plant.GINKGO_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_GINKGO_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/ginkgo_logs"));
		ContinuousMinerCustomLoot.addBlockLoot(List.of(
				ECBlocks.Plant.PALM_LOG.getId(),
				ECBlocks.Plant.STRIPPED_PALM_LOG.getId(),
				ECBlocks.Plant.PALM_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_PALM_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/palm_logs"));
		ContinuousMinerCustomLoot.addBlockLoot(List.of(
				ECBlocks.Plant.PEACH_LOG.getId(),
				ECBlocks.Plant.STRIPPED_PEACH_LOG.getId(),
				ECBlocks.Plant.PEACH_WOOD.getId(),
				ECBlocks.Plant.STRIPPED_PEACH_WOOD.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/peach_logs"));
		ContinuousMinerCustomLoot.addBlockLoot(List.of(
				ECBlocks.Plant.PURPURACEUS_STEM.getId(),
				ECBlocks.Plant.STRIPPED_PURPURACEUS_STEM.getId(),
				ECBlocks.Plant.PURPURACEUS_HYPHAE.getId(),
				ECBlocks.Plant.STRIPPED_PURPURACEUS_HYPHAE.getId()
		), new ResourceLocation(MODID, "continuous_miner/wood/purpuraceus_stems"));

		ContinuousMinerCustomLoot.addBlockLoot(
				List.of(ECBlocks.Plant.PURPURACEUS_NYLIUM.getId()),
				new ResourceLocation(MODID, "continuous_miner/nylium/purpuraceus_nylium")
		);
	}
}
