package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.register.ECConfiguredStructures;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(StructureFeatures.class)
public class StructureFeaturesMixin {
	private static void acceptAll(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> func,
								  ConfiguredStructureFeature<?, ?> configuredStructureFeature,
								  Set<ResourceKey<Biome>> set) {
		set.forEach((biome) -> func.accept(configuredStructureFeature, biome));
	}

	@Inject(method = "registerStructures", at = @At(value = "TAIL"))
	private static void registerECStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> func, CallbackInfo ci) {
		final Set<ResourceKey<Biome>> desertSetEC = ImmutableSet.<ResourceKey<Biome>>builder()
				.add(ECBiomeKeys.AZURE_DESERT).add(ECBiomeKeys.JADEITE_DESERT).build();
		final Set<ResourceKey<Biome>> oceanSetEC = ImmutableSet.<ResourceKey<Biome>>builder()
				.add(ECBiomeKeys.DEAD_CRIMSON_OCEAN).add(ECBiomeKeys.DEAD_WARPED_OCEAN)
				.add(ECBiomeKeys.DEEP_DEAD_CRIMSON_OCEAN).add(ECBiomeKeys.DEEP_DEAD_WARPED_OCEAN).build();
		final Set<ResourceKey<Biome>> netherSetEC = ImmutableSet.<ResourceKey<Biome>>builder()
				.add(ECBiomeKeys.EMERY_DESERT).add(ECBiomeKeys.QUARTZ_DESERT).add(ECBiomeKeys.PURPURACEUS_SWAMP).build();
		final Set<ResourceKey<Biome>> beachSetEC = ImmutableSet.<ResourceKey<Biome>>builder()
				.add(ECBiomeKeys.GOLDEN_BEACH).add(ECBiomeKeys.PALM_BEACH).build();
		acceptAll(func, StructureFeatures.MINESHAFT, oceanSetEC);
		acceptAll(func, StructureFeatures.MINESHAFT, beachSetEC);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.KARST_HILLS);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.PETUNIA_PLAINS);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.VOLCANIC_CAVES);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.XANADU);
		acceptAll(func, StructureFeatures.MINESHAFT_MESA, desertSetEC);
		func.accept(StructureFeatures.PILLAGER_OUTPOST, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.PILLAGER_OUTPOST, ECBiomeKeys.PETUNIA_PLAINS);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_DESERT, desertSetEC);
		func.accept(StructureFeatures.RUINED_PORTAL_MOUNTAIN, ECBiomeKeys.KARST_HILLS);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_OCEAN, oceanSetEC);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_STANDARD, beachSetEC);
		func.accept(StructureFeatures.RUINED_PORTAL_STANDARD, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.RUINED_PORTAL_STANDARD, ECBiomeKeys.XANADU);
		acceptAll(func, StructureFeatures.SHIPWRECK, oceanSetEC);
		acceptAll(func, StructureFeatures.BURIED_TREASURE, beachSetEC);

		func.accept(StructureFeatures.BASTION_REMNANT, ECBiomeKeys.QUARTZ_DESERT);
		acceptAll(func, StructureFeatures.NETHER_BRIDGE, netherSetEC);
		acceptAll(func, StructureFeatures.NETHER_FOSSIL, netherSetEC);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_NETHER, netherSetEC);

		func.accept(ECConfiguredStructures.SHELTER, Biomes.CRIMSON_FOREST);
		func.accept(ECConfiguredStructures.NETHER_WARFIELD, ECBiomeKeys.EMERY_DESERT);
		func.accept(ECConfiguredStructures.ENTRENCHMENT, ECBiomeKeys.PURPURACEUS_SWAMP);
		
		func.accept(ECConfiguredStructures.VILLAGE_SWAMP, Biomes.SWAMP);
		func.accept(ECConfiguredStructures.VILLAGE_SWAMP, ECBiomeKeys.XANADU);
		
		//Camps
		func.accept(ECConfiguredStructures.BADLANDS_CAMP, Biomes.BADLANDS);
		func.accept(ECConfiguredStructures.BADLANDS_CAMP, Biomes.ERODED_BADLANDS);
		func.accept(ECConfiguredStructures.BADLANDS_CAMP, Biomes.WOODED_BADLANDS);
		
		func.accept(ECConfiguredStructures.BIRCH_CAMP, Biomes.BIRCH_FOREST);
		func.accept(ECConfiguredStructures.BIRCH_CAMP, Biomes.OLD_GROWTH_BIRCH_FOREST);
		
		func.accept(ECConfiguredStructures.DESERT_CAMP, Biomes.BEACH);
		func.accept(ECConfiguredStructures.DESERT_CAMP, Biomes.DESERT);
		
		func.accept(ECConfiguredStructures.JUNGLE_CAMP, Biomes.BAMBOO_JUNGLE);
		func.accept(ECConfiguredStructures.JUNGLE_CAMP, Biomes.JUNGLE);
		func.accept(ECConfiguredStructures.JUNGLE_CAMP, Biomes.SPARSE_JUNGLE);
		
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.FLOWER_FOREST);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.FOREST);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.MEADOW);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.PLAINS);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.SUNFLOWER_PLAINS);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, Biomes.WINDSWEPT_FOREST);
		func.accept(ECConfiguredStructures.PLAINS_CAMP, ECBiomeKeys.PETUNIA_PLAINS);
		
		func.accept(ECConfiguredStructures.SAVANNA_CAMP, Biomes.SAVANNA);
		func.accept(ECConfiguredStructures.SAVANNA_CAMP, Biomes.SAVANNA_PLATEAU);
		func.accept(ECConfiguredStructures.SAVANNA_CAMP, Biomes.WINDSWEPT_SAVANNA);
		
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.FROZEN_PEAKS);
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.GROVE);
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.ICE_SPIKES);
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.JAGGED_PEAKS);
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.SNOWY_PLAINS);
		func.accept(ECConfiguredStructures.SNOW_CAMP, Biomes.SNOWY_SLOPES);
		
		func.accept(ECConfiguredStructures.STONY_CAMP, Biomes.STONY_SHORE);
		func.accept(ECConfiguredStructures.STONY_CAMP, Biomes.STONY_PEAKS);
		func.accept(ECConfiguredStructures.STONY_CAMP, Biomes.WINDSWEPT_GRAVELLY_HILLS);
		func.accept(ECConfiguredStructures.STONY_CAMP, Biomes.WINDSWEPT_HILLS);
		func.accept(ECConfiguredStructures.STONY_CAMP, ECBiomeKeys.KARST_HILLS);
		
		func.accept(ECConfiguredStructures.SWAMP_CAMP, Biomes.DARK_FOREST);
		func.accept(ECConfiguredStructures.SWAMP_CAMP, Biomes.SWAMP);
		
		func.accept(ECConfiguredStructures.TAIGA_CAMP, Biomes.TAIGA);
		func.accept(ECConfiguredStructures.TAIGA_CAMP, Biomes.OLD_GROWTH_PINE_TAIGA);
		func.accept(ECConfiguredStructures.TAIGA_CAMP, Biomes.OLD_GROWTH_SPRUCE_TAIGA);
		func.accept(ECConfiguredStructures.TAIGA_CAMP, Biomes.SNOWY_TAIGA);
	}
}
