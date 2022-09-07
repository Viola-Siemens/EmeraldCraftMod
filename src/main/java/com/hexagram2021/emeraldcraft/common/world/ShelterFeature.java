package com.hexagram2021.emeraldcraft.common.world;


import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ShelterFeature extends Structure<NoFeatureConfig> {

	public ShelterFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override @Nonnull
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override @Nonnull
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return ShelterFeature.FeatureStart::new;
	}

	@Override @Nonnull
	public String getFeatureName() {
		return new ResourceLocation(MODID, "shelter").toString();
	}

	public static class FeatureStart extends StructureStart<NoFeatureConfig> {
		public FeatureStart(Structure<NoFeatureConfig> feature, int x, int z, MutableBoundingBox boundingBox, int references, long seed) {
			super(feature, x, z, boundingBox, references, seed);
		}

		@Override
		public void generatePieces(@Nonnull DynamicRegistries registryAccess, @Nonnull ChunkGenerator chunkGenerator, @Nonnull TemplateManager structureManager,
								   int x, int z, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
			BlockPos blockpos = new BlockPos(x << 4, 90, z << 4);
			Rotation rotation = Rotation.getRandom(this.random);
			ShelterPieces.addPieces(structureManager, blockpos, rotation, this.pieces);
			this.calculateBoundingBox();
		}
	}
}
