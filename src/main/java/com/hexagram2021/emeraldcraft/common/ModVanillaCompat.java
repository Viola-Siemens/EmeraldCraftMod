package com.hexagram2021.emeraldcraft.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.item.AxeItem;

import java.util.Map;

public class ModVanillaCompat {
	public static void setup() {
		Map<Block, Block> stripables = Maps.newHashMap(AxeItem.STRIPABLES);
		stripables.putAll(
				ImmutableMap.<Block, Block>builder()
						.put(ECBlocks.Plant.GINKGO_LOG.get(), ECBlocks.Plant.STRIPPED_GINKGO_LOG.get())
						.put(ECBlocks.Plant.GINKGO_WOOD.get(), ECBlocks.Plant.STRIPPED_GINKGO_WOOD.get())
						.put(ECBlocks.Plant.PALM_LOG.get(), ECBlocks.Plant.STRIPPED_PALM_LOG.get())
						.put(ECBlocks.Plant.PALM_WOOD.get(), ECBlocks.Plant.STRIPPED_PALM_WOOD.get())
						.put(ECBlocks.Plant.PEACH_LOG.get(), ECBlocks.Plant.STRIPPED_PEACH_LOG.get())
						.put(ECBlocks.Plant.PEACH_WOOD.get(), ECBlocks.Plant.STRIPPED_PEACH_WOOD.get())
						.build()
		);
		AxeItem.STRIPABLES = stripables;

		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PALM_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PALM_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PEACH_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PEACH_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECItems.GINKGO_NUT.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.PEACH.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.CYAN_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.MAGENTA_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.HIGAN_BANA.asItem(), 0.65F);
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
		registerFlammable(ECBlocks.Plant.PALM_LEAVES.get(), 30, 60);
		registerFlammable(ECBlocks.Plant.PALM_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.PALM_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_PALM_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_PALM_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.PALM_PLANKS.get(), 5, 20);
		registerFlammable(ECBlocks.TO_SLAB.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_STAIRS.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE_GATE.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.Plant.PEACH_LEAVES.get(), 30, 60);
		registerFlammable(ECBlocks.Plant.PEACH_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.PEACH_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_PEACH_LOG.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.STRIPPED_PEACH_WOOD.get(), 5, 5);
		registerFlammable(ECBlocks.Plant.PEACH_PLANKS.get(), 5, 20);
		registerFlammable(ECBlocks.TO_SLAB.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_STAIRS.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), 5, 20);
		registerFlammable(ECBlocks.TO_FENCE_GATE.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), 5, 20);
	}

	private static final FireBlock fireblock = (FireBlock) Blocks.FIRE;

	public static void registerFlammable(Block blockIn, int encouragement, int flammability) {
		fireblock.setFlammable(blockIn, encouragement, flammability);
	}
}
