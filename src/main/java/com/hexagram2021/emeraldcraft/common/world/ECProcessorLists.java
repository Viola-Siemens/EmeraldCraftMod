package com.hexagram2021.emeraldcraft.common.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECProcessorLists {
	private static final DeferredRegister<StructureProcessorList> REGISTER = DeferredRegister.create(Registries.PROCESSOR_LIST, MODID);

	public static final RegistryObject<StructureProcessorList> STREET_SWAMP = register(
			"street_swamp", ImmutableList.of(new RuleProcessor(ImmutableList.of(
					new ProcessorRule(new BlockMatchTest(Blocks.DIRT_PATH), new BlockMatchTest(Blocks.WATER), Blocks.DARK_OAK_PLANKS.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.05F), AlwaysTrueTest.INSTANCE, Blocks.WATER.defaultBlockState()),
					new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()),
					new ProcessorRule(new BlockMatchTest(Blocks.DIRT), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState())
			)))
	);

	public static final RegistryObject<StructureProcessorList> FARM_SWAMP = register(
			"farm_swamp", ImmutableList.of(new RuleProcessor(ImmutableList.of(
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.CARROTS.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.POTATOES.defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS.defaultBlockState())
			)))
	);

	private static RegistryObject<StructureProcessorList> register(String name, ImmutableList<StructureProcessor> list) {
		return REGISTER.register(name, () -> new StructureProcessorList(list));
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
