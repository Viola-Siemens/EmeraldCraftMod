package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.register.ECPlacedFeatures;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ECWorldgen {
	public static void biomeModification(final BiomeLoadingEvent event) {
		if(Biomes.SOUL_SAND_VALLEY.location().equals(event.getName())) {
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.FLOWER_HIGAN_BANA);
		}
	}
}
