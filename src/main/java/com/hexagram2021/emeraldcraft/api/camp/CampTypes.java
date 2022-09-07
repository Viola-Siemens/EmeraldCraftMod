package com.hexagram2021.emeraldcraft.api.camp;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.register.ECConfiguredStructures;
import com.hexagram2021.emeraldcraft.common.world.CampFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.*;

public enum CampTypes implements CampType {
	BADLANDS {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.BADLANDS.location(),
				Biomes.BADLANDS_PLATEAU.location(),
				Biomes.ERODED_BADLANDS.location(),
				Biomes.MODIFIED_BADLANDS_PLATEAU.location(),
				Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU.location(),
				Biomes.WOODED_BADLANDS_PLATEAU.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.BADLANDS_CAMP;
		}
	},
	BIRCH {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.BIRCH_FOREST.location(),
				Biomes.BIRCH_FOREST_HILLS.location(),
				Biomes.TALL_BIRCH_FOREST.location(),
				Biomes.TALL_BIRCH_HILLS.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.BIRCH_CAMP;
		}
	},
	DESERT {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.DESERT.location(),
				Biomes.DESERT_HILLS.location(),
				Biomes.BEACH.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.DESERT_CAMP;
		}
	},
	JUNGLE {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.BAMBOO_JUNGLE.location(),
				Biomes.BAMBOO_JUNGLE_HILLS.location(),
				Biomes.JUNGLE.location(),
				Biomes.JUNGLE_EDGE.location(),
				Biomes.JUNGLE_HILLS.location(),
				Biomes.MODIFIED_JUNGLE.location(),
				Biomes.MODIFIED_JUNGLE_EDGE.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.JUNGLE_CAMP;
		}
	},
	PLAINS {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.FLOWER_FOREST.location(),
				Biomes.FOREST.location(),
				Biomes.PLAINS.location(),
				Biomes.SUNFLOWER_PLAINS.location(),
				Biomes.WOODED_HILLS.location(),
				Biomes.WOODED_MOUNTAINS.location(),
				ECBiomeKeys.PETUNIA_PLAINS.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.PLAINS_CAMP;
		}
	},
	SAVANNA {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.SAVANNA.location(),
				Biomes.SAVANNA_PLATEAU.location(),
				Biomes.SHATTERED_SAVANNA.location(),
				Biomes.SHATTERED_SAVANNA_PLATEAU.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.SAVANNA_CAMP;
		}
	},
	SNOW {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.ICE_SPIKES.location(),
				Biomes.SNOWY_MOUNTAINS.location(),
				Biomes.SNOWY_TUNDRA.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.SNOW_CAMP;
		}
	},
	STONY {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.GRAVELLY_MOUNTAINS.location(),
				Biomes.MODIFIED_GRAVELLY_MOUNTAINS.location(),
				Biomes.MOUNTAINS.location(),
				Biomes.STONE_SHORE.location(),
				ECBiomeKeys.KARST_HILLS.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.STONY_CAMP;
		}
	},
	SWAMP {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.DARK_FOREST.location(),
				Biomes.DARK_FOREST_HILLS.location(),
				Biomes.SWAMP.location(),
				Biomes.SWAMP_HILLS.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.SWAMP_CAMP;
		}
	},
	TAIGA {
		final List<ResourceLocation> targetBiomes = Arrays.asList(
				Biomes.GIANT_SPRUCE_TAIGA.location(),
				Biomes.GIANT_SPRUCE_TAIGA_HILLS.location(),
				Biomes.GIANT_TREE_TAIGA.location(),
				Biomes.GIANT_TREE_TAIGA_HILLS.location(),
				Biomes.MOUNTAIN_EDGE.location(),
				Biomes.SNOWY_TAIGA.location(),
				Biomes.SNOWY_TAIGA_HILLS.location(),
				Biomes.SNOWY_TAIGA_MOUNTAINS.location(),
				Biomes.TAIGA.location(),
				Biomes.TAIGA_HILLS.location(),
				Biomes.TAIGA_MOUNTAINS.location()
		);
		@Override
		public boolean isTargetBiome(ResourceLocation biome) {
			return this.targetBiomes.contains(biome);
		}

		@Override
		public StructureFeature<?, ?> getCampStructure() {
			return ECConfiguredStructures.TAIGA_CAMP;
		}
	};

	private static final Map<String, CampFeature> CUSTOM_CAMP = new HashMap<>();

	@SuppressWarnings("unused")
	//API for custom camp.
	public static void addCustomCamp(CampType type, CampFeature structure) {
		CUSTOM_CAMP.put(type.toString(), structure);
		ECConfiguredStructures.ALL_CAMPS.add(type);
	}

	public static ResourceLocation getCampWithType(CampType type) {
		return CUSTOM_CAMP.get(type.toString()).getRegistryName();
	}

	public static Collection<CampFeature> getCustomCamps() {
		return CUSTOM_CAMP.values();
	}
}
