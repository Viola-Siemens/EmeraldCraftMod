package com.hexagram2021.emeraldcraft.common.world.compat;

import com.hexagram2021.emeraldcraft.common.world.ECOverworldBiomeBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECOverworldBiomeRegion extends Region {
	public static final ResourceLocation LOCATION = new ResourceLocation(MODID, "overworld_biome_provider");

	public ECOverworldBiomeRegion(int weight) {
		super(LOCATION, RegionType.OVERWORLD, weight);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		(new ECOverworldBiomeBuilder()).addBiomes(registry, mapper);
	}
}
