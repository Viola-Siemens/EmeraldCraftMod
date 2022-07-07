package com.hexagram2021.emeraldcraft.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;

import java.util.HashMap;
import java.util.Map;

public class ModVanillaCompat {
	public static void setup() {
		Map<Block, Block> strippables = Maps.newHashMap(AxeItem.STRIPPABLES);
		strippables.putAll(
				ImmutableMap.<Block, Block>builder()
						.put(ECBlocks.Plant.GINKGO_LOG.get(), ECBlocks.Plant.STRIPPED_GINKGO_LOG.get())
						.put(ECBlocks.Plant.GINKGO_WOOD.get(), ECBlocks.Plant.STRIPPED_GINKGO_WOOD.get())
						.build()
		);
		AxeItem.STRIPPABLES = strippables;

		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECItems.GINKGO_NUT.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.CYAN_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.MAGENTA_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.WARPED_WART.asItem(), 0.65F);

		registerFlammable(ECBlocks.Plant.GINKGO_LEAVES.get(), 30, 60);
		registerFlammable(ECBlocks.Plant.GINKGO_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.GINKGO_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_GINKGO_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_GINKGO_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.GINKGO_PLANKS.get(), 5, 20);
		registerFlammable(ECBlocks.TO_SLAB.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_STAIRS.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE_GATE.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), 5, 20);
	}

	private static final FireBlock fireblock = (FireBlock)Blocks.FIRE;

	public static void registerFlammable(Block blockIn, int encouragement, int flammability) {
		fireblock.setFlammable(blockIn, encouragement, flammability);
	}
}
