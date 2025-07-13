package com.hexagram2021.emeraldcraft.common.world.biome;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeTags;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.register.ECPlacedFeatureKeys;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BiomeModifiers {
    public static void init() {
        //ec_higan_bana
        BiomeModifications.addFeature(
                selectionContext -> selectionContext.getBiomeKey().equals(Biomes.SOUL_SAND_VALLEY),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ECPlacedFeatureKeys.FLOWER_HIGAN_BANA
        );

        //ec_snakehead
        BiomeModifications.addSpawn(
                selectionContext -> selectionContext.hasTag(BiomeTags.IS_RIVER),
                MobCategory.WATER_AMBIENT,
                ECEntities.SNAKEHEAD,
                5, 2, 5
        );

        //ec_wild_crops
        BiomeModifications.addFeature(
                selectionContext -> selectionContext.hasTag(ConventionalBiomeTags.PLAINS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ECPlacedFeatureKeys.WILD_CABBAGE
        );
        BiomeModifications.addFeature(
                selectionContext -> selectionContext.hasTag(ConventionalBiomeTags.PLAINS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ECPlacedFeatureKeys.WILD_CHILI
        );

        //ec_wombat
        BiomeModifications.addSpawn(
                selectionContext -> selectionContext.hasTag(ECBiomeTags.WOMBAT_SPAWN),
                MobCategory.CREATURE,
                ECEntities.WOMBAT,
                2, 1, 2
        );
    }
}
