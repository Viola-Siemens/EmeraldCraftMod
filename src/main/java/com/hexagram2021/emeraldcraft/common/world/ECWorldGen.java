package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.register.ECStructures;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ECWorldGen {
	public static void biomeModification(final BiomeLoadingEvent event) {
		if(event.getName().getPath().contains("crimson")) {
			event.getGeneration().getStructures().add(() -> ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE));
		}
	}
}
