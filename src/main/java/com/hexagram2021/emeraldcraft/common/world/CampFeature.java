package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;

import java.util.Locale;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CampFeature extends Structure<NoFeatureConfig> {
	final CampType type;

	public CampFeature(Codec<NoFeatureConfig> codec, CampType type) {
		super(codec);
		this.type = type;
	}

	@Override @Nonnull
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override @Nonnull
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return CampFeature.FeatureStart::new;
	}

	@Override @Nonnull
	public String getFeatureName() {
		return new ResourceLocation(MODID, this.type.toString().toLowerCase(Locale.ROOT) + "_camp").toString();
	}

	public static class FeatureStart extends StructureStart<NoFeatureConfig> {
		private final CampType type;
		public FeatureStart(Structure<NoFeatureConfig> feature, int x, int z, MutableBoundingBox boundingBox, int references, long seed) {
			super(feature, x, z, boundingBox, references, seed);
			this.type = ((CampFeature)feature).type;
		}

		@Override
		public void generatePieces(@Nonnull DynamicRegistries registryAccess, @Nonnull ChunkGenerator chunkGenerator, @Nonnull TemplateManager structureManager,
								   int x, int z, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
			BlockPos centerOfChunk = new BlockPos((x << 4) + 7, 0, (z << 4) + 7);
			BlockPos blockpos = new BlockPos(
					x << 4,
					chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG),
					z << 4
			);
			Rotation rotation = Rotation.getRandom(this.random);
			CampPieces.addPieces(structureManager, blockpos, rotation, this.pieces, this.type);
			this.calculateBoundingBox();
		}
	}
}
