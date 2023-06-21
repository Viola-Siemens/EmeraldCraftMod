package com.hexagram2021.emeraldcraft.common.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class VineGrowthConfiguration implements FeatureConfiguration {
	@SuppressWarnings("deprecation")
	public static final Codec<VineGrowthConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").flatXmap(VineGrowthConfiguration::apply, DataResult::success).orElse(Blocks.VINE).forGetter(conf -> conf.placeBlock),
			Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter(conf -> conf.searchRange),
			RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter(conf -> conf.canBePlacedOn)).apply(instance, VineGrowthConfiguration::new));
	public final Block placeBlock;
	public final int searchRange;
	public final HolderSet<Block> canBePlacedOn;
	private final ObjectArrayList<Direction> validDirections;

	private static DataResult<Block> apply(Block block) {
		DataResult<Block> result;
		BlockState blockState = block.defaultBlockState();
		if (blockState.hasProperty(PipeBlock.NORTH) && blockState.hasProperty(PipeBlock.SOUTH) &&
				blockState.hasProperty(PipeBlock.EAST) && blockState.hasProperty(PipeBlock.WEST)) {
			result = DataResult.success(block);
		} else {
			result = DataResult.error(() -> "Growth block should have north, south, east and west properties.");
		}

		return result;
	}

	public VineGrowthConfiguration(Block placeBlock, int range, HolderSet<Block> placedOn) {
		this.placeBlock = placeBlock;
		this.searchRange = range;
		this.canBePlacedOn = placedOn;
		this.validDirections = new ObjectArrayList<>(4);
		Direction.Plane.HORIZONTAL.forEach(this.validDirections::add);
	}

	public List<Direction> getShuffledDirectionsExcept(RandomSource random, Direction except) {
		return Util.toShuffledList(this.validDirections.stream().filter(direction -> direction != except), random);
	}

	public List<Direction> getShuffledDirections(RandomSource random) {
		return Util.shuffledCopy(this.validDirections, random);
	}
}