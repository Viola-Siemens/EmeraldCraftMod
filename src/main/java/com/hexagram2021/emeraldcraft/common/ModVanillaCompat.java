package com.hexagram2021.emeraldcraft.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class ModVanillaCompat {
	public static void setup() {
		//STRIPPABLES
		Map<Block, Block> strippables = Maps.newHashMap(AxeItem.STRIPPABLES);
		strippables.putAll(
				ImmutableMap.<Block, Block>builder()
						.put(ECBlocks.Plant.GINKGO_LOG.get(), ECBlocks.Plant.STRIPPED_GINKGO_LOG.get())
						.put(ECBlocks.Plant.GINKGO_WOOD.get(), ECBlocks.Plant.STRIPPED_GINKGO_WOOD.get())
						.put(ECBlocks.Plant.PALM_LOG.get(), ECBlocks.Plant.STRIPPED_PALM_LOG.get())
						.put(ECBlocks.Plant.PALM_WOOD.get(), ECBlocks.Plant.STRIPPED_PALM_WOOD.get())
						.put(ECBlocks.Plant.PEACH_LOG.get(), ECBlocks.Plant.STRIPPED_PEACH_LOG.get())
						.put(ECBlocks.Plant.PEACH_WOOD.get(), ECBlocks.Plant.STRIPPED_PEACH_WOOD.get())
						.put(ECBlocks.Plant.PURPURACEUS_STEM.get(), ECBlocks.Plant.STRIPPED_PURPURACEUS_STEM.get())
						.put(ECBlocks.Plant.PURPURACEUS_HYPHAE.get(), ECBlocks.Plant.STRIPPED_PURPURACEUS_HYPHAE.get())
						.build()
		);
		AxeItem.STRIPPABLES = strippables;

		//COMPOSTABLES
		/* 30% */
		ComposterBlock.COMPOSTABLES.put(ECItems.CHILI_SEED.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECItems.CABBAGE_SEED.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.GINKGO_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PALM_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PALM_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PEACH_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PEACH_SAPLING.asItem(), 0.3F);
		/* 65% */
		ComposterBlock.COMPOSTABLES.put(ECItems.CHILI.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.CABBAGE.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.GINKGO_NUT.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.PEACH.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.WILD_CHILI.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.WILD_CABBAGE.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.CYAN_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.MAGENTA_PETUNIA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.HIGAN_BANA.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECItems.WARPED_WART.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PURPURACEUS_ROOTS.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PURPURACEUS_FUNGUS.asItem(), 0.65F);
		/* 85% */
		ComposterBlock.COMPOSTABLES.put(ECBlocks.Plant.PURPURACEUS_WART_BLOCK.asItem(), 0.85F);

		//FLAMMABLES
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
		registerFlammable(ECBlocks.Decoration.RESIN_BLOCK.get(), 5, 20);

		//FLOWER POTS
		registerFlowerPot(ECBlocks.Plant.CYAN_PETUNIA.get(), ECBlocks.Plant.POTTED_CYAN_PETUNIA::get);
		registerFlowerPot(ECBlocks.Plant.MAGENTA_PETUNIA.get(), ECBlocks.Plant.POTTED_MAGENTA_PETUNIA::get);
		registerFlowerPot(ECBlocks.Plant.HIGAN_BANA.get(), ECBlocks.Plant.POTTED_HIGAN_BANA::get);
		registerFlowerPot(ECBlocks.Plant.GINKGO_SAPLING.get(), ECBlocks.Plant.POTTED_GINKGO_SAPLING::get);
		registerFlowerPot(ECBlocks.Plant.PALM_SAPLING.get(), ECBlocks.Plant.POTTED_PALM_SAPLING::get);
		registerFlowerPot(ECBlocks.Plant.PEACH_SAPLING.get(), ECBlocks.Plant.POTTED_PEACH_SAPLING::get);
		registerFlowerPot(ECBlocks.Plant.PURPURACEUS_FUNGUS.get(), ECBlocks.Plant.POTTED_PURPURACEUS_FUNGUS::get);
		registerFlowerPot(ECBlocks.Plant.PURPURACEUS_ROOTS.get(), ECBlocks.Plant.POTTED_PURPURACEUS_ROOTS::get);

		//Villages
		Villages.setup();
	}

	private static final FireBlock fireblock = (FireBlock)Blocks.FIRE;
	public static void registerFlammable(Block blockIn, int encouragement, int flammability) {
		fireblock.setFlammable(blockIn, encouragement, flammability);
	}

	private static final FlowerPotBlock flowerpotblock = (FlowerPotBlock)Blocks.FLOWER_POT;
	public static void registerFlowerPot(Block flower, Supplier<Block> pot) {
		flowerpotblock.addPlant(Objects.requireNonNull(getRegistryName(flower)), pot);
	}
}
