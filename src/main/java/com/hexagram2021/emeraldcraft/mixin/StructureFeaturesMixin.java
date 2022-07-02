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
				.add(ECBiomeKeys.EMERY_DESERT).add(ECBiomeKeys.QUARTZ_DESERT).build();
		acceptAll(func, StructureFeatures.MINESHAFT, oceanSetEC);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.GOLDEN_BEACH);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.KARST_HILLS);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.PETUNIA_PLAINS);
		func.accept(StructureFeatures.MINESHAFT, ECBiomeKeys.XANADU);
		acceptAll(func, StructureFeatures.MINESHAFT_MESA, desertSetEC);
		func.accept(StructureFeatures.PILLAGER_OUTPOST, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.PILLAGER_OUTPOST, ECBiomeKeys.PETUNIA_PLAINS);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_DESERT, desertSetEC);
		func.accept(StructureFeatures.RUINED_PORTAL_MOUNTAIN, ECBiomeKeys.KARST_HILLS);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_OCEAN, oceanSetEC);
		func.accept(StructureFeatures.RUINED_PORTAL_STANDARD, ECBiomeKeys.GOLDEN_BEACH);
		func.accept(StructureFeatures.RUINED_PORTAL_STANDARD, ECBiomeKeys.GINKGO_FOREST);
		func.accept(StructureFeatures.RUINED_PORTAL_STANDARD, ECBiomeKeys.XANADU);
		acceptAll(func, StructureFeatures.SHIPWRECK, oceanSetEC);
		func.accept(StructureFeatures.BURIED_TREASURE, ECBiomeKeys.GOLDEN_BEACH);

		func.accept(StructureFeatures.BASTION_REMNANT, ECBiomeKeys.QUARTZ_DESERT);
		acceptAll(func, StructureFeatures.NETHER_BRIDGE, netherSetEC);
		acceptAll(func, StructureFeatures.NETHER_FOSSIL, netherSetEC);
		acceptAll(func, StructureFeatures.RUINED_PORTAL_NETHER, netherSetEC);

		func.accept(ECConfiguredStructures.SHELTER_HOUSE, Biomes.CRIMSON_FOREST);
		func.accept(ECConfiguredStructures.NETHER_WARFIELD, ECBiomeKeys.EMERY_DESERT);
	}
}
