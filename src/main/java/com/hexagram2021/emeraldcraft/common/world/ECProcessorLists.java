package com.hexagram2021.emeraldcraft.common.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECProcessorLists {
	public static final Holder<StructureProcessorList> STREET_SWAMP = register(
			"street_swamp", ImmutableList.of(new RuleProcessor(ImmutableList.of(
					new ProcessorRule(new BlockMatchTest(Blocks.DIRT_PATH), new BlockMatchTest(Blocks.WATER), Blocks.DARK_OAK_PLANKS.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.05F), AlwaysTrueTest.INSTANCE, Blocks.WATER.defaultBlockState()),
					new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()),
					new ProcessorRule(new BlockMatchTest(Blocks.DIRT), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState())
			)))
	);

	public static final Holder<StructureProcessorList> FARM_SWAMP = register(
			"farm_swamp", ImmutableList.of(new RuleProcessor(ImmutableList.of(
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.CARROTS.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.POTATOES.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS.defaultBlockState())
			)))
	);

	private static Holder<StructureProcessorList> register(String name, ImmutableList<StructureProcessor> list) {
		ResourceLocation resourcelocation = new ResourceLocation(MODID, name);
		StructureProcessorList structureprocessorlist = new StructureProcessorList(list);
		return BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, resourcelocation, structureprocessorlist);
	}
}
