package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import com.hexagram2021.emeraldcraft.common.world.pools.SwampVillagePools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final StructureFeature<?, ?> SHELTER = register(
			new ResourceLocation(MODID, "shelter"),
			ECStructures.SHELTER.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> NETHER_WARFIELD = register(
			new ResourceLocation(MODID, "nether_warfield"),
			ECStructures.NETHER_WARFIELD.configured(new VillageConfig(() -> NetherWarfieldPools.START, 6))
	);
	public static final StructureFeature<?, ?> VILLAGE_SWAMP = register(
			new ResourceLocation(MODID, "village_swamp"),
			Structure.VILLAGE.configured(new VillageConfig(() -> SwampVillagePools.START, 6))
	);
	public static final StructureFeature<?, ?> BADLANDS_CAMP = register(
			new ResourceLocation(MODID, "badlands_camp"),
			ECStructures.BADLANDS_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> BIRCH_CAMP = register(
			new ResourceLocation(MODID, "birch_camp"),
			ECStructures.BIRCH_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> DESERT_CAMP = register(
			new ResourceLocation(MODID, "desert_camp"),
			ECStructures.DESERT_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> JUNGLE_CAMP = register(
			new ResourceLocation(MODID, "jungle_camp"),
			ECStructures.JUNGLE_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> PLAINS_CAMP = register(
			new ResourceLocation(MODID, "plains_camp"),
			ECStructures.PLAINS_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> SAVANNA_CAMP = register(
			new ResourceLocation(MODID, "savanna_camp"),
			ECStructures.SAVANNA_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> SNOW_CAMP = register(
			new ResourceLocation(MODID, "snow_camp"),
			ECStructures.SNOW_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> STONY_CAMP = register(
			new ResourceLocation(MODID, "stony_camp"),
			ECStructures.STONY_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> SWAMP_CAMP = register(
			new ResourceLocation(MODID, "swamp_camp"),
			ECStructures.SWAMP_CAMP.configured(NoFeatureConfig.INSTANCE)
	);
	public static final StructureFeature<?, ?> TAIGA_CAMP = register(
			new ResourceLocation(MODID, "taiga_camp"),
			ECStructures.TAIGA_CAMP.configured(NoFeatureConfig.INSTANCE)
	);

	public static List<CampType> ALL_CAMPS = Lists.newArrayList(
			CampTypes.BADLANDS, CampTypes.BIRCH,
			CampTypes.DESERT, CampTypes.JUNGLE,
			CampTypes.PLAINS, CampTypes.SAVANNA,
			CampTypes.SNOW, CampTypes.STONY,
			CampTypes.SWAMP, CampTypes.TAIGA
	);

	private static StructureFeature<?, ?> register(ResourceLocation id, StructureFeature<?, ?> configuredStructureFeature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, id, configuredStructureFeature);
	}

	public static void init() {}
}
