package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreeFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterFeature;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ECStructureTypes {
	public static final StructureType<ShelterFeature> SHELTER = Registry.register(Registry.STRUCTURE_TYPES, "shelter", () -> ShelterFeature.CODEC);
	public static final StructureType<EntrenchmentFeature> ENTRENCHMENT = Registry.register(Registry.STRUCTURE_TYPES, "entrenchment", () -> EntrenchmentFeature.CODEC);
	public static final StructureType<CampFeature> CAMP = Registry.register(Registry.STRUCTURE_TYPES, "camp", () -> CampFeature.CODEC);
	public static final StructureType<HollowTreeFeature> HOLLOW_TREE = Registry.register(Registry.STRUCTURE_TYPES, "hollow_tree", () -> HollowTreeFeature.CODEC);

	public static void init() {
	}
}
