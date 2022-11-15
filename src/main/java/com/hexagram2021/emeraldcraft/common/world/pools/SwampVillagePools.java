package com.hexagram2021.emeraldcraft.common.world.pools;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.register.ECVillagePlacedFeatures;
import com.hexagram2021.emeraldcraft.common.world.ECProcessorLists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.placement.VillagePlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class SwampVillagePools {
	public static final StructureTemplatePool START = Pools.register(
			new StructureTemplatePool(
					new ResourceLocation(MODID, "village/swamp/town_centers"),
					new ResourceLocation("empty"),
					ImmutableList.of(
							Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/town_centers/swamp_fountain_01", ProcessorLists.MOSSIFY_20_PERCENT), 50),
							Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/town_centers/swamp_meeting_point_1", ProcessorLists.MOSSIFY_20_PERCENT), 50),
							Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/town_centers/swamp_meeting_point_2"), 50)
					),
					StructureTemplatePool.Projection.RIGID
			)
	);

	public static void bootstrap() {
	}

	static {
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/streets"),
						new ResourceLocation(MODID, "village/swamp/terminators"),
						ImmutableList.of(
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/corner_01", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/corner_02", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/corner_03", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_01", ECProcessorLists.STREET_SWAMP), 4),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_02", ECProcessorLists.STREET_SWAMP), 4),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_03", ECProcessorLists.STREET_SWAMP), 7),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_04", ECProcessorLists.STREET_SWAMP), 7),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_05", ECProcessorLists.STREET_SWAMP), 3),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/straight_06", ECProcessorLists.STREET_SWAMP), 4),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_01", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_02", ECProcessorLists.STREET_SWAMP), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_03", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_04", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_05", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/crossroad_06", ECProcessorLists.STREET_SWAMP), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/streets/turn_01", ECProcessorLists.STREET_SWAMP), 3)
						),
						StructureTemplatePool.Projection.TERRAIN_MATCHING
				)
		);
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/houses"),
						new ResourceLocation(MODID, "village/swamp/terminators"),
						ImmutableList.of(
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_2", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_3", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_4", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_5", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_6", ProcessorLists.MOSSIFY_10_PERCENT), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_7", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_house_8", ProcessorLists.MOSSIFY_10_PERCENT), 3),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_medium_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_medium_house_2", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_astrologist_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_big_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_armorer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_butcher_shop_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_carpenter_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_cartographer_1", ProcessorLists.MOSSIFY_10_PERCENT), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_fisher_cottage_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_fletcher_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_glazier_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_icer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_large_farm_1", ECProcessorLists.FARM_SWAMP), 4),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_library_1", ProcessorLists.MOSSIFY_10_PERCENT), 6),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_masons_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_miner_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 3),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_small_farm_1", ECProcessorLists.FARM_SWAMP), 4),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_tannery_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_temple_3", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_tool_smith_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_weaponsmith_1", ProcessorLists.MOSSIFY_10_PERCENT), 2),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_animal_pen_1"), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_animal_pen_2"), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_animal_pen_3"), 5),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_accessory_1"), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_meeting_point_4", ProcessorLists.MOSSIFY_70_PERCENT), 3),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/houses/swamp_meeting_point_5"), 1),
								Pair.of(StructurePoolElement.empty(), 10)
						),
						StructureTemplatePool.Projection.RIGID
				)
		);
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/terminators"),
						new ResourceLocation("empty"),
						ImmutableList.of(
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/terminators/terminator_01", ECProcessorLists.STREET_SWAMP), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/terminators/terminator_02", ECProcessorLists.STREET_SWAMP), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/terminators/terminator_03", ECProcessorLists.STREET_SWAMP), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/terminators/terminator_04", ECProcessorLists.STREET_SWAMP), 1)
						),
						StructureTemplatePool.Projection.TERRAIN_MATCHING
				)
		);
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/trees"),
						new ResourceLocation("empty"),
						ImmutableList.of(Pair.of(StructurePoolElement.feature(ECVillagePlacedFeatures.DARK_OAK_VILLAGE), 1)),
						StructureTemplatePool.Projection.RIGID
				)
		);
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/decor"),
						new ResourceLocation("empty"),
						ImmutableList.of(
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/swamp_lamp_1"), 2),
								Pair.of(StructurePoolElement.feature(ECVillagePlacedFeatures.DARK_OAK_VILLAGE), 1),
								Pair.of(StructurePoolElement.feature(ECVillagePlacedFeatures.FLOWER_SWAMP_VILLAGE), 1),
								Pair.of(StructurePoolElement.feature(VillagePlacements.PILE_HAY_VILLAGE), 1),
								Pair.of(StructurePoolElement.empty(), 2)
						),
						StructureTemplatePool.Projection.RIGID
				)
		);
		Pools.register(
				new StructureTemplatePool(
						new ResourceLocation(MODID, "village/swamp/villagers"),
						new ResourceLocation("empty"),
						ImmutableList.of(
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/villagers/nitwit"), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/villagers/baby"), 1),
								Pair.of(StructurePoolElement.legacy(MODID + ":village/swamp/villagers/unemployed"), 10)
						),
						StructureTemplatePool.Projection.RIGID
				)
		);
	}
}
