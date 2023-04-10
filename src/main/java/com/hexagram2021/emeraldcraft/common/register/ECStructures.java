package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ListAppendable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class ECStructures {
	public static final List<ResourceKey<Structure>> ALL_CAMPS = Lists.newArrayList(
			ECStructureKeys.BADLANDS_CAMP, ECStructureKeys.BIRCH_CAMP,
			ECStructureKeys.DESERT_CAMP, ECStructureKeys.JUNGLE_CAMP,
			ECStructureKeys.PLAINS_CAMP, ECStructureKeys.SAVANNA_CAMP,
			ECStructureKeys.SNOW_CAMP, ECStructureKeys.STONY_CAMP,
			ECStructureKeys.SWAMP_CAMP, ECStructureKeys.TAIGA_CAMP
	);

	public static final List<ResourceKey<Structure>> ALL_VILLAGES = Lists.newArrayList(
			ECStructureKeys.VILLAGE_SWAMP
	);

	@SuppressWarnings("unchecked")
	public static void init(RegistryAccess registryAccess) {
		((ListAppendable<StructureSet.StructureSelectionEntry>)(Object)
				registryAccess.lookupOrThrow(Registries.STRUCTURE_SET).get(BuiltinStructureSets.VILLAGES).orElseThrow().get())
				.appendAll(
						ALL_VILLAGES.stream().map(rk ->
								StructureSet.entry(
										registryAccess.lookupOrThrow(Registries.STRUCTURE).get(rk).orElseThrow()
								)
						).collect(Collectors.toList())
				).forEach(v -> ECLogger.debug("{} - {}", getRegistryName(v.structure().value().type()), v.weight()));
		((ListAppendable<StructureSet.StructureSelectionEntry>)(Object)
				registryAccess.lookupOrThrow(Registries.STRUCTURE_SET).get(ECStructureSetKeys.CAMP).orElseThrow().get())
				.appendAll(
						ALL_CAMPS.stream().map(rk ->
								StructureSet.entry(
										registryAccess.lookupOrThrow(Registries.STRUCTURE).get(rk).orElseThrow()
								)
						).collect(Collectors.toList())
				).forEach(v -> ECLogger.debug("{} - {}", getRegistryName(v.structure().value().type()), v.weight()));
	}
}
