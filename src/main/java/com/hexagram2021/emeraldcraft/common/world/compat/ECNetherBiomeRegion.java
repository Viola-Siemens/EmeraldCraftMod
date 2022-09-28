package com.hexagram2021.emeraldcraft.common.world.compat;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.util.BiomeUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECNetherBiomeRegion extends Region {
	public static final ResourceLocation LOCATION = new ResourceLocation(MODID, "nether_biome_provider");

	public ECNetherBiomeRegion(int weight) {
		super(LOCATION, RegionType.NETHER, weight);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.NETHER_WASTES);
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(-0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.SOUL_SAND_VALLEY);
		this.addBiome(mapper, Climate.Parameter.point(0.4F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, Biomes.CRIMSON_FOREST);
		this.addBiome(mapper, Climate.Parameter.point(0.0F), Climate.Parameter.point(0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.375F, Biomes.WARPED_FOREST);
		this.addBiome(mapper, Climate.Parameter.point(-0.5F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.175F, Biomes.BASALT_DELTAS);
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.EMERY_DESERT)) {
			this.addBiome(mapper, Climate.Parameter.point(-0.8F), Climate.Parameter.point(-0.8F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, ECBiomeKeys.EMERY_DESERT);
		}
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.PURPURACEUS_SWAMP)) {
			this.addBiome(mapper, Climate.Parameter.point(0.7F), Climate.Parameter.point(0.7F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.125F, ECBiomeKeys.PURPURACEUS_SWAMP);
		}
		if (BiomeUtil.isKeyRegistered(registry, ECBiomeKeys.QUARTZ_DESERT)) {
			this.addBiome(mapper, Climate.Parameter.point(0.75F), Climate.Parameter.point(-0.7F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), Climate.Parameter.point(0.0F), 0.0F, ECBiomeKeys.QUARTZ_DESERT);
		}
	}
}
