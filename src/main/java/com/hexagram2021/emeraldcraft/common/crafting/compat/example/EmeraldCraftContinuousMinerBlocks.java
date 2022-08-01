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
	}
}
