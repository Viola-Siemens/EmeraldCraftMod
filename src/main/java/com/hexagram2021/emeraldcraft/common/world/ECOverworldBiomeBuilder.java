package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.util.BiomeUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import terrablender.api.ParameterUtils;

import java.util.function.Consumer;

public class ECOverworldBiomeBuilder {

	public ECOverworldBiomeBuilder() {
	}

	private final ParameterUtils.Temperature[] TEMPERATURES = new ParameterUtils.Temperature[]{
			ParameterUtils.Temperature.ICY,
			ParameterUtils.Temperature.COOL,
			ParameterUtils.Temperature.NEUTRAL,
			ParameterUtils.Temperature.WARM,
			ParameterUtils.Temperature.HOT
	};
	private final ParameterUtils.Humidity[] HUMIDITIES = new ParameterUtils.Humidity[]{
			ParameterUtils.Humidity.ARID,
			ParameterUtils.Humidity.DRY,
			ParameterUtils.Humidity.NEUTRAL,
			ParameterUtils.Humidity.WET,
			ParameterUtils.Humidity.HUMID
	};


	private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][] {
			{Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN},
			{Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN}
	};
	private final ResourceKey<Biome>[][] ISLAND_BIOMES_EC = new ResourceKey[][] {
			{null, null, null, null, ECBiomeKeys.XANADU},
			{null, null, null, null, ECBiomeKeys.XANADU},
			{null, null, null, null, ECBiomeKeys.XANADU},
			{null, null, null, ECBiomeKeys.XANADU, ECBiomeKeys.XANADU},
			{null, null, null, null, ECBiomeKeys.XANADU}
	};
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][] {
			{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA},
			{Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
			{Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST},
			{Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE},
			{Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}
	};
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][] {
			{Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null},
			{null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA},
			{Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null},
			{null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE},
			{null, null, null, null, null}
	};
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES_EC = new ResourceKey[][] {
			{null, null, null, null, null},
			{null, null, null, null, null},
			{null, null, null, ECBiomeKeys.GINKGO_FOREST, ECBiomeKeys.GINKGO_FOREST},
			{ECBiomeKeys.JADEITE_DESERT, null, null, null, null},
			{ECBiomeKeys.JADEITE_DESERT, ECBiomeKeys.JADEITE_DESERT, ECBiomeKeys.AZURE_DESERT, ECBiomeKeys.AZURE_DESERT, ECBiomeKeys.AZURE_DESERT}
	};
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT_EC = new ResourceKey[][] {
			{null, null, null, null, null},
			{null, ECBiomeKeys.PETUNIA_PLAINS, null, null, null},
			{null, ECBiomeKeys.PETUNIA_PLAINS, null, null, null},
			{null, null, null, null, null},
			{null, null, null, null, null}
	};
	private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][] {
			{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
			{Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
			{Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST},
			{Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE},
			{Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}
	};
	private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][] {
			{Biomes.ICE_SPIKES, null, null, null, null},
			{null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA},
			{null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null},
			{null, null, null, null, null},
			{Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null}
	};
	private final ResourceKey<Biome>[][] EXTREME_HILLS = new ResourceKey[][] {
			{Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
			{Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
			{Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
			{null, null, null, null, null},
			{null, null, null, null, null}
	};


	public void addBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addOffCoastBiomes(biomeRegistry, mapper);
		this.addInlandBiomes(biomeRegistry, mapper);
		this.addUndergroundBiomes(biomeRegistry, mapper);
	}

	private void addOffCoastBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addSurfaceBiome(mapper,
				Climate.Parameter.span(ParameterUtils.Temperature.WARM.parameter(), ParameterUtils.Temperature.HOT.parameter()),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.DEEP_OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				ECBiomeKeys.DEEP_DEAD_CRIMSON_OCEAN);
		this.addSurfaceBiome(mapper,
				Climate.Parameter.span(ParameterUtils.Temperature.COOL.parameter(), ParameterUtils.Temperature.NEUTRAL.parameter()),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.DEEP_OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				ECBiomeKeys.DEEP_DEAD_WARPED_OCEAN);
		this.addSurfaceBiome(mapper,
				Climate.Parameter.span(ParameterUtils.Temperature.WARM.parameter(), ParameterUtils.Temperature.HOT.parameter()),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				ECBiomeKeys.DEAD_CRIMSON_OCEAN);
		this.addSurfaceBiome(mapper,
				Climate.Parameter.span(ParameterUtils.Temperature.COOL.parameter(), ParameterUtils.Temperature.NEUTRAL.parameter()),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				ECBiomeKeys.DEAD_WARPED_OCEAN);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.ICY.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.DEEP_OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				Biomes.DEEP_FROZEN_OCEAN);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.ICY.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.OCEAN.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Weirdness.PEAK_VARIANT.parameter(), ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter()),
				0.0F,
				Biomes.FROZEN_OCEAN);

		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> islandBiome = this.pickECIslandBiome(biomeRegistry, i, j);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.MUSHROOM_FIELDS.parameter(),
						ParameterUtils.Erosion.FULL_RANGE.parameter(),
						ParameterUtils.Weirdness.FULL_RANGE.parameter(),
						0.0F,
						islandBiome);
			}

			this.addSurfaceBiome(mapper,
					temperature,
					ParameterUtils.Humidity.FULL_RANGE.parameter(),
					ParameterUtils.Continentalness.DEEP_OCEAN.parameter(),
					ParameterUtils.Erosion.FULL_RANGE.parameter(),
					Climate.Parameter.span(ParameterUtils.Weirdness.MID_SLICE_NORMAL_ASCENDING.parameter(), ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_ASCENDING.parameter()),
					0.0F,
					this.OCEANS[0][i]);
			this.addSurfaceBiome(mapper,
					temperature,
					ParameterUtils.Humidity.FULL_RANGE.parameter(),
					ParameterUtils.Continentalness.OCEAN.parameter(),
					ParameterUtils.Erosion.FULL_RANGE.parameter(),
					Climate.Parameter.span(ParameterUtils.Weirdness.MID_SLICE_NORMAL_ASCENDING.parameter(), ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_ASCENDING.parameter()),
					0.0F,
					this.OCEANS[1][i]);
		}
	}

	private void addInlandBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addMidSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.MID_SLICE_NORMAL_ASCENDING.parameter());
		this.addHighSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.HIGH_SLICE_NORMAL_ASCENDING.parameter());
		this.addPeaks(biomeRegistry, mapper, ParameterUtils.Weirdness.PEAK_NORMAL.parameter());
		this.addHighSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.HIGH_SLICE_NORMAL_DESCENDING.parameter());
		this.addMidSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.MID_SLICE_NORMAL_DESCENDING.parameter());
		this.addLowSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.LOW_SLICE_NORMAL_DESCENDING.parameter());
		this.addValleys(biomeRegistry, mapper, ParameterUtils.Weirdness.VALLEY.parameter());
		this.addLowSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.LOW_SLICE_VARIANT_ASCENDING.parameter());
		this.addMidSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.MID_SLICE_VARIANT_ASCENDING.parameter());
		this.addHighSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_ASCENDING.parameter());
		this.addPeaks(biomeRegistry, mapper, ParameterUtils.Weirdness.PEAK_VARIANT.parameter());
		this.addHighSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_DESCENDING.parameter());
		this.addMidSlice(biomeRegistry, mapper, ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING.parameter());
	}

	protected void addPeaks(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> middleECBiome = this.pickECMiddleBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> middleBadlandsOrSlopeECBiome = this.pickECMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> extremeHillsBiome = this.pickVanillaExtremeHillsBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = this.maybePickShatteredBiome(i, j, weirdness, extremeHillsBiome);
				ResourceKey<Biome> peakBiome = this.pickPeakBiome(i, j, weirdness);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_0.parameter(),
						weirdness, 0.0F, peakBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_1.parameter(),
						weirdness, 0.0F, middleBadlandsOrSlopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_1.parameter(),
						weirdness, 0.0F, peakBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_3.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_2.parameter(),
						weirdness, 0.0F, plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.MID_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.FAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_4.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, extremeHillsBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_6.parameter(),
						weirdness, 0.0F, middleECBiome);
			}
		}

	}

	protected void addHighSlice(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> middleBiomeVanilla = this.pickVanillaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleECBiome = this.pickECMiddleBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> middleBadlandsOrSlopeECBiome = this.pickECMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> extremeHillsBiome = this.pickVanillaExtremeHillsBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = this.maybePickShatteredBiome(i, j, weirdness, middleBiomeVanilla);
				ResourceKey<Biome> slopeECBiome = this.pickECSlopeBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> peakBiome = this.pickPeakBiome(i, j, weirdness);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.COAST.parameter(),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_0.parameter(),
						weirdness, 0.0F, slopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_0.parameter(),
						weirdness, 0.0F, peakBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_1.parameter(),
						weirdness, 0.0F, middleBadlandsOrSlopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_1.parameter(),
						weirdness, 0.0F, slopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_3.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_2.parameter(),
						weirdness, 0.0F, plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.MID_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.FAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(),ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_4.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, extremeHillsBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_6.parameter(),
						weirdness, 0.0F, middleECBiome);
			}
		}

	}

	protected void addMidSlice(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FULL_RANGE.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_2.parameter()),
				weirdness, 0.0F, Biomes.STONY_SHORE);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.SWAMP);

		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> middleBiomeVanilla = this.pickVanillaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleECBiome = this.pickECMiddleBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> middleBadlandsOrSlopeECBiome = this.pickECMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> extremeHillsBiome = this.pickVanillaExtremeHillsBiome(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> beachBiome = this.pickBeachBiome(biomeRegistry, i, j);
				ResourceKey<Biome> shatteredBiome = this.maybePickShatteredBiome(i, j, weirdness, middleBiomeVanilla);
				ResourceKey<Biome> shatteredCoastBiome = this.pickShatteredCoastBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> slopeECBiome = this.pickECSlopeBiome(biomeRegistry, i, j, weirdness);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_0.parameter(), weirdness, 0.0F, slopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.MID_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_1.parameter(), weirdness, 0.0F, middleBadlandsOrSlopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.FAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_1.parameter(),
						weirdness, 0.0F, i == 0 ? slopeECBiome : plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_2.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.MID_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_2.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.FAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_2.parameter(),
						weirdness, 0.0F, plateauBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.NEAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_3.parameter(),
						weirdness, 0.0F, middleECBiome);
				if (weirdness.max() < 0L) {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							ParameterUtils.Continentalness.COAST.parameter(),
							ParameterUtils.Erosion.EROSION_4.parameter(),
							weirdness, 0.0F, beachBiome);
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
							ParameterUtils.Erosion.EROSION_4.parameter(),
							weirdness, 0.0F, middleECBiome);
				} else {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
							ParameterUtils.Erosion.EROSION_4.parameter(),
							weirdness, 0.0F, middleECBiome);
				}

				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.COAST.parameter(),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredCoastBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, extremeHillsBiome);
				if (weirdness.max() < 0L) {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							ParameterUtils.Continentalness.COAST.parameter(),
							ParameterUtils.Erosion.EROSION_6.parameter(),
							weirdness, 0.0F, beachBiome);
				} else {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							ParameterUtils.Continentalness.COAST.parameter(),
							ParameterUtils.Erosion.EROSION_6.parameter(),
							weirdness, 0.0F, middleECBiome);
				}

				if (i == 0) {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
							ParameterUtils.Erosion.EROSION_6.parameter(),
							weirdness, 0.0F, middleECBiome);
				}
			}
		}

	}

	protected void addLowSlice(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FULL_RANGE.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_2.parameter()),
				weirdness, 0.0F, Biomes.STONY_SHORE);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.SWAMP);

		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> middleBiomeVanilla = this.pickVanillaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleECBiome = this.pickECMiddleBiome(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> middleBadlandsOrSlopeECBiome = this.pickECMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(biomeRegistry, i, j, weirdness);
				ResourceKey<Biome> beachBiome = this.pickBeachBiome(biomeRegistry, i, j);
				ResourceKey<Biome> shatteredBiome = this.maybePickShatteredBiome(i, j, weirdness, middleBiomeVanilla);
				ResourceKey<Biome> shatteredCoastBiome = this.pickShatteredCoastBiome(biomeRegistry, i, j, weirdness);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
						weirdness, 0.0F, middleBadlandsOrSlopeECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_3.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_3.parameter()),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.COAST.parameter(),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_3.parameter(), ParameterUtils.Erosion.EROSION_4.parameter()),
						weirdness, 0.0F, beachBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_4.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.COAST.parameter(),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredCoastBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, shatteredBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						ParameterUtils.Erosion.EROSION_5.parameter(),
						weirdness, 0.0F, middleECBiome);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						ParameterUtils.Continentalness.COAST.parameter(),
						ParameterUtils.Erosion.EROSION_6.parameter(),
						weirdness, 0.0F, beachBiome);
				if (i == 0) {
					this.addSurfaceBiome(mapper,
							temperature, humidity,
							Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
							ParameterUtils.Erosion.EROSION_6.parameter(),
							weirdness, 0.0F, middleECBiome);
				}
			}
		}

	}

	protected void addValleys(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
				weirdness, 0.0F, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
				weirdness, 0.0F, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
				weirdness, 0.0F, Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
				weirdness, 0.0F, Biomes.RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_5.parameter()),
				weirdness, 0.0F, Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.COAST.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()), Climate.Parameter.span(ParameterUtils.Erosion.EROSION_2.parameter(), ParameterUtils.Erosion.EROSION_5.parameter()), weirdness, 0.0F, Biomes.RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.COAST.parameter(),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Continentalness.NEAR_INLAND.parameter(), ParameterUtils.Continentalness.FAR_INLAND.parameter()),
				ParameterUtils.Erosion.EROSION_6.parameter(),
				weirdness, 0.0F, Biomes.SWAMP);

		for(int i = 0; i < TEMPERATURES.length; ++i) {
			Climate.Parameter temperature = TEMPERATURES[i].parameter();

			for(int j = 0; j < HUMIDITIES.length; ++j) {
				Climate.Parameter humidity = HUMIDITIES[j].parameter();
				ResourceKey<Biome> middleECBiome = this.pickECMiddleBiome(biomeRegistry, i, j, weirdness);
				this.addSurfaceBiome(mapper,
						temperature, humidity,
						Climate.Parameter.span(ParameterUtils.Continentalness.MID_INLAND.parameter(),ParameterUtils.Continentalness.FAR_INLAND.parameter()),
						Climate.Parameter.span(ParameterUtils.Erosion.EROSION_0.parameter(), ParameterUtils.Erosion.EROSION_1.parameter()),
						weirdness, 0.0F, middleECBiome);
			}
		}

	}

	protected void addUndergroundBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addUndergroundBiome(biomeRegistry, mapper,
				ParameterUtils.Temperature.UNFROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_4.parameter(), ParameterUtils.Erosion.EROSION_6.parameter()),
				Climate.Parameter.span(ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_ASCENDING.parameter(), ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_DESCENDING.parameter()),
				Climate.Parameter.span(0.2F, 0.4F),
				0.75F, ECBiomeKeys.VOLCANIC_CAVES);
		this.addUndergroundBiome(biomeRegistry, mapper,
				ParameterUtils.Temperature.FROZEN.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				ParameterUtils.Continentalness.FULL_RANGE.parameter(),
				Climate.Parameter.span(ParameterUtils.Erosion.EROSION_4.parameter(), ParameterUtils.Erosion.EROSION_6.parameter()),
				Climate.Parameter.span(ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_ASCENDING.parameter(), ParameterUtils.Weirdness.HIGH_SLICE_VARIANT_DESCENDING.parameter()),
				Climate.Parameter.span(0.2F, 0.4F),
				0.25F, ECBiomeKeys.VOLCANIC_CAVES);

		this.addUndergroundBiome(biomeRegistry, mapper,
				ParameterUtils.Temperature.FULL_RANGE.parameter(),
				ParameterUtils.Humidity.FULL_RANGE.parameter(),
				Climate.Parameter.span(0.8F, 1.0F),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				ParameterUtils.Weirdness.FULL_RANGE.parameter(),
				ParameterUtils.Depth.UNDERGROUND.parameter(),
				0.0F,
				Biomes.DRIPSTONE_CAVES
		);
		this.addUndergroundBiome(biomeRegistry, mapper,
				ParameterUtils.Temperature.FULL_RANGE.parameter(),
				Climate.Parameter.span(0.7F, 1.0F),
				ParameterUtils.Continentalness.FULL_RANGE.parameter(),
				ParameterUtils.Erosion.FULL_RANGE.parameter(),
				ParameterUtils.Weirdness.FULL_RANGE.parameter(),
				ParameterUtils.Depth.UNDERGROUND.parameter(),
				0.0F,
				Biomes.LUSH_CAVES
		);
	}

	protected ResourceKey<Biome> pickECIslandBiome(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex) {
		return BiomeUtil.biomeOrFallback(biomeRegistry, this.ISLAND_BIOMES_EC[temperatureIndex][humidityIndex], Biomes.MUSHROOM_FIELDS);
	}

	protected ResourceKey<Biome> pickVanillaMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if (weirdness.max() < 0L) {
			return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex];
		}
		ResourceKey<Biome> variantBiome = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
		return variantBiome == null ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : variantBiome;
	}

	protected ResourceKey<Biome> pickECMiddleBiome(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> middleBiome = BiomeUtil.biomeOrFallback(biomeRegistry, this.MIDDLE_BIOMES_EC[temperatureIndex][humidityIndex], this.MIDDLE_BIOMES[temperatureIndex][humidityIndex]);
		return weirdness.max() < 0L ? middleBiome : BiomeUtil.biomeOrFallback(biomeRegistry,
						this.MIDDLE_BIOMES_VARIANT_EC[temperatureIndex][humidityIndex],
						this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex], middleBiome);
	}

	protected ResourceKey<Biome> pickECMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return temperatureIndex == 0 ? this.pickECSlopeBiome(biomeRegistry, temperatureIndex, humidityIndex, weirdness) : this.pickECMiddleBiome(biomeRegistry, temperatureIndex, humidityIndex, weirdness);
	}

	protected ResourceKey<Biome> maybePickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> extremeHillsBiome) {
		return temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0L ? Biomes.WINDSWEPT_SAVANNA : extremeHillsBiome;
	}

	protected ResourceKey<Biome> pickShatteredCoastBiome(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = weirdness.max() >= 0L ? this.pickVanillaMiddleBiome(temperatureIndex, humidityIndex, weirdness) : this.pickBeachBiome(biomeRegistry, temperatureIndex, humidityIndex);
		return this.maybePickShatteredBiome(temperatureIndex, humidityIndex, weirdness, biome);
	}

	protected ResourceKey<Biome> pickBeachBiome(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex) {
		if (temperatureIndex == 0) {
			return Biomes.SNOWY_BEACH;
		}
		if (temperatureIndex >= 4 && humidityIndex >= 2) {
			return BiomeUtil.biomeOrFallback(biomeRegistry, ECBiomeKeys.GOLDEN_BEACH, Biomes.DESERT);
		}
		return temperatureIndex >= 4 ? Biomes.DESERT : Biomes.BEACH;
	}

	protected ResourceKey<Biome> pickBadlandsBiome(int humidityIndex, Climate.Parameter weirdness) {
		if (humidityIndex < 2) {
			return weirdness.max() < 0L ? Biomes.ERODED_BADLANDS : Biomes.BADLANDS;
		} else {
			return humidityIndex < 3 ? Biomes.BADLANDS : Biomes.WOODED_BADLANDS;
		}
	}

	protected ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if (weirdness.max() < 0L) {
			return this.PLATEAU_BIOMES[temperatureIndex][humidityIndex];
		}
		ResourceKey<Biome> biome = this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
		return biome == null ? this.PLATEAU_BIOMES[temperatureIndex][humidityIndex] : biome;
	}

	protected ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if (temperatureIndex <= 2) {
			return weirdness.max() < 0L ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
		} else {
			return temperatureIndex <= 3 ? Biomes.STONY_PEAKS : this.pickBadlandsBiome(humidityIndex, weirdness);
		}
	}

	protected ResourceKey<Biome> pickECSlopeBiome(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
		if (temperatureIndex >= 1 && weirdness.min() > Climate.quantizeCoord(0.5F)) {
			return BiomeUtil.biomeOrFallback(biomeRegistry, ECBiomeKeys.KARST_HILLS, plateauBiome);
		}
		return plateauBiome;
	}

	protected ResourceKey<Biome> pickVanillaExtremeHillsBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = this.EXTREME_HILLS[temperatureIndex][humidityIndex];
		return biome == null ? this.pickVanillaMiddleBiome(temperatureIndex, humidityIndex, weirdness) : biome;
	}

	protected void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, ParameterUtils.Depth.SURFACE.parameter(), weirdness, offset), biome));
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, ParameterUtils.Depth.FLOOR.parameter(), weirdness, offset), biome));
	}

	protected void addUndergroundBiome(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, Climate.Parameter depth, float offset, ResourceKey<Biome> biome) {
		if (BiomeUtil.isKeyRegistered(biomeRegistry, biome)) {
			mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biome));
		}
	}
}
