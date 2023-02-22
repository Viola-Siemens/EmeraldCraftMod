package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import com.hexagram2021.emeraldcraft.common.world.pools.SwampVillagePools;
import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreeFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.List;
import java.util.Map;

public class ECStructures {
	public static final Holder<Structure> SHELTER = register(
			ECStructureKeys.SHELTER,
			new ShelterFeature(structure(ECBiomeTags.HAS_SHELTER, TerrainAdjustment.BEARD_THIN))
	);
	public static final Holder<Structure> NETHER_WARFIELD = register(
			ECStructureKeys.NETHER_WARFIELD,
			new JigsawStructure(
					structure(ECBiomeTags.HAS_NETHER_WARFIELD, TerrainAdjustment.BEARD_THIN), NetherWarfieldPools.START, 6,
					ConstantHeight.of(VerticalAnchor.absolute(60)), true
			)
	);
	public static final Holder<Structure> ENTRENCHMENT = register(
			ECStructureKeys.ENTRENCHMENT,
			new EntrenchmentFeature(structure(
					ECBiomeTags.HAS_ENTRENCHMENT,
					Map.of(MobCategory.MONSTER, new StructureSpawnOverride(
							StructureSpawnOverride.BoundingBoxType.STRUCTURE,
							WeightedRandomList.create(new MobSpawnSettings.SpawnerData(ECEntities.WRAITH, 1, 1, 1))
					)),
					TerrainAdjustment.BURY
			))
	);
	public static final Holder<Structure> VILLAGE_SWAMP = register(
			ECStructureKeys.VILLAGE_SWAMP,
			new JigsawStructure(
					structure(ECBiomeTags.HAS_VILLAGE_SWAMP, TerrainAdjustment.BEARD_THIN), SwampVillagePools.START, 6,
					ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG
			)
	);
	public static final Holder<Structure> HOLLOW_TREE = register(
			ECStructureKeys.HOLLOW_TREE,
			new HollowTreeFeature(structure(ECBiomeTags.HAS_HOLLOW_TREE, TerrainAdjustment.BEARD_THIN))
	);

	public static final Holder<Structure> BADLANDS_CAMP = register(
			ECStructureKeys.BADLANDS_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_BADLANDS, TerrainAdjustment.BEARD_THIN), CampTypes.BADLANDS)
	);
	public static final Holder<Structure> BIRCH_CAMP = register(
			ECStructureKeys.BIRCH_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_BIRCH, TerrainAdjustment.BEARD_THIN), CampTypes.BIRCH)
	);
	public static final Holder<Structure> DESERT_CAMP = register(
			ECStructureKeys.DESERT_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_DESERT, TerrainAdjustment.BEARD_THIN), CampTypes.DESERT)
	);
	public static final Holder<Structure> JUNGLE_CAMP = register(
			ECStructureKeys.JUNGLE_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_JUNGLE, TerrainAdjustment.BEARD_THIN), CampTypes.JUNGLE)
	);
	public static final Holder<Structure> PLAINS_CAMP = register(
			ECStructureKeys.PLAINS_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_PLAINS, TerrainAdjustment.BEARD_THIN), CampTypes.PLAINS)
	);
	public static final Holder<Structure> SAVANNA_CAMP = register(
			ECStructureKeys.SAVANNA_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_SAVANNA, TerrainAdjustment.BEARD_THIN), CampTypes.SAVANNA)
	);
	public static final Holder<Structure> SNOW_CAMP = register(
			ECStructureKeys.SNOW_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_SNOW, TerrainAdjustment.BEARD_THIN), CampTypes.SNOW)
	);
	public static final Holder<Structure> STONY_CAMP = register(
			ECStructureKeys.STONY_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_STONY, TerrainAdjustment.BEARD_THIN), CampTypes.STONY)
	);
	public static final Holder<Structure> SWAMP_CAMP = register(
			ECStructureKeys.SWAMP_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_SWAMP, TerrainAdjustment.BEARD_THIN), CampTypes.SWAMP)
	);
	public static final Holder<Structure> TAIGA_CAMP = register(
			ECStructureKeys.TAIGA_CAMP,
			new CampFeature(structure(ECBiomeTags.HAS_CAMP_TAIGA, TerrainAdjustment.BEARD_THIN), CampTypes.TAIGA)
	);

	public static final List<StructureSet.StructureSelectionEntry> ALL_CAMPS = Lists.newArrayList(
			StructureSet.entry(BADLANDS_CAMP), StructureSet.entry(BIRCH_CAMP),
			StructureSet.entry(DESERT_CAMP), StructureSet.entry(JUNGLE_CAMP),
			StructureSet.entry(PLAINS_CAMP), StructureSet.entry(SAVANNA_CAMP),
			StructureSet.entry(SNOW_CAMP), StructureSet.entry(STONY_CAMP),
			StructureSet.entry(SWAMP_CAMP), StructureSet.entry(TAIGA_CAMP)
	);

	public static final List<StructureSet.StructureSelectionEntry> ALL_VILLAGES = Lists.newArrayList(
			StructureSet.entry(ECStructures.VILLAGE_SWAMP)
	);

	private static Holder<Structure> register(ResourceKey<Structure> id, Structure structure) {
		return BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, id, structure);
	}

	@SuppressWarnings("SameParameterValue")
	private static Structure.StructureSettings structure(TagKey<Biome> biome, Map<MobCategory, StructureSpawnOverride> mobs,
														 GenerationStep.Decoration step, TerrainAdjustment adjustment) {
		return new Structure.StructureSettings(biomes(biome), mobs, step, adjustment);
	}

	@SuppressWarnings("SameParameterValue")
	private static Structure.StructureSettings structure(TagKey<Biome> biome, Map<MobCategory, StructureSpawnOverride> mobs, TerrainAdjustment adjustment) {
		return structure(biome, mobs, GenerationStep.Decoration.SURFACE_STRUCTURES, adjustment);
	}

	@SuppressWarnings("SameParameterValue")
	private static Structure.StructureSettings structure(TagKey<Biome> biome, TerrainAdjustment adjustment) {
		return structure(biome, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, adjustment);
	}

	@SuppressWarnings("deprecation")
	private static HolderSet<Biome> biomes(TagKey<Biome> biome) {
		return BuiltinRegistries.BIOME.getOrCreateTag(biome);
	}

	public static void init() {}
}
