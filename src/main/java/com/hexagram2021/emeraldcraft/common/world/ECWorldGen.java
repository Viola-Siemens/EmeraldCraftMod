package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.hexagram2021.emeraldcraft.common.register.ECBiomeTags;
import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatures;
import com.hexagram2021.emeraldcraft.common.register.ECConfiguredStructures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ECWorldGen {
	public static void biomeModification(final BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		if(ECBiomeTags.hasShelter(biome)) {
			event.getGeneration().getStructures().add(() -> ECConfiguredStructures.SHELTER);
		} else if(ECBiomeTags.hasNetherWarfield(biome)) {
			event.getGeneration().getStructures().add(() -> ECConfiguredStructures.NETHER_WARFIELD);
		} else if(ECBiomeTags.hasSwampVillage(biome)) {
			event.getGeneration().getStructures().add(() -> ECConfiguredStructures.VILLAGE_SWAMP);
		} else if(Biomes.SOUL_SAND_VALLEY.location().equals(biome)) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ECConfiguredFeatures.VegetationFeatures.FLOWER_HIGAN_BANA);
		}
		
		for (CampType type: ECConfiguredStructures.ALL_CAMPS) {
			if(ECBiomeTags.hasCampWithType(biome, type)) {
				event.getGeneration().getStructures().add(type::getCampStructure);
			}
		}

		if(event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND) {
			event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.OreConfiguredFeatures.ORE_DEEPSLATE);
		}
	}
}
