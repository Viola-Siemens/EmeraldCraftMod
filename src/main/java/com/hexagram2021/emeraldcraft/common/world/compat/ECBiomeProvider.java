package com.hexagram2021.emeraldcraft.common.world.compat;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.util.BiomeUtil;
import com.hexagram2021.emeraldcraft.common.world.ECOverworldBiomeBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.BiomeProvider;
import terrablender.worldgen.TBClimate;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeProvider extends BiomeProvider {
	public static final ResourceLocation LOCATION = new ResourceLocation(MODID, "biome_provider");

	public ECBiomeProvider(int weight) {
		super(LOCATION, weight, weight);
	}
	
	@Override
	public void addOverworldBiomes(Registry<Biome> registry, Consumer<Pair<TBClimate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		(new ECOverworldBiomeBuilder(getUniquenessParameter())).addBiomes(registry, mapper);
	}
	
	@Override
	public void addNetherBiomes(Registry<Biome> registry, Consumer<Pair<TBClimate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.NETHER_WASTES);
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(-0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.SOUL_SAND_VALLEY);
		this.addBiome(mapper, Climate.Parameter.point(0.4F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.CRIMSON_FOREST);
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.375F, Biomes.WARPED_FOREST);
		this.addBiome(mapper, Climate.Parameter.point(-0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.175F, Biomes.BASALT_DELTAS);
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.EMERY_DESERT)) {
			this.addBiome(mapper, Climate.Parameter.point(-0.8F), Climate.Parameter.point(-0.8F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.125F, ECBiomeKeys.EMERY_DESERT);
		}
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.QUARTZ_DESERT)) {
			this.addBiome(mapper, Climate.Parameter.point(0.75F), Climate.Parameter.point(0.7F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.125F, ECBiomeKeys.QUARTZ_DESERT);
		}
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.PURPURACEUS_SWAMP)) {
			this.addBiome(mapper, Climate.Parameter.point(0.7F), Climate.Parameter.point(-0.7F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.375F, ECBiomeKeys.PURPURACEUS_SWAMP);
		}
	}
}
