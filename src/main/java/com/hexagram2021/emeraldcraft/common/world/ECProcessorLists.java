package com.hexagram2021.emeraldcraft.common.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.template.*;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECProcessorLists {
	public static final StructureProcessorList STREET_SWAMP = register(
			"street_swamp", ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(
					new RuleEntry(new BlockMatchRuleTest(Blocks.GRASS_PATH), new BlockMatchRuleTest(Blocks.WATER), Blocks.DARK_OAK_PLANKS.defaultBlockState()),
					new RuleEntry(new RandomBlockMatchRuleTest(Blocks.GRASS_PATH, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.GRASS_BLOCK.defaultBlockState()),
					new RuleEntry(new RandomBlockMatchRuleTest(Blocks.GRASS_PATH, 0.05F), AlwaysTrueRuleTest.INSTANCE, Blocks.WATER.defaultBlockState()),
					new RuleEntry(new BlockMatchRuleTest(Blocks.GRASS_BLOCK), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.defaultBlockState()),
					new RuleEntry(new BlockMatchRuleTest(Blocks.DIRT), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.defaultBlockState())
			)))
	);

	public static final StructureProcessorList FARM_SWAMP = register(
			"farm_swamp", ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(
					new RuleEntry(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.defaultBlockState()),
					new RuleEntry(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.defaultBlockState()),
					new RuleEntry(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.defaultBlockState())
			)))
	);

	private static StructureProcessorList register(String name, ImmutableList<StructureProcessor> list) {
		ResourceLocation resourcelocation = new ResourceLocation(MODID, name);
		StructureProcessorList structureprocessorlist = new StructureProcessorList(list);
		return WorldGenRegistries.register(WorldGenRegistries.PROCESSOR_LIST, resourcelocation, structureprocessorlist);
	}
}
