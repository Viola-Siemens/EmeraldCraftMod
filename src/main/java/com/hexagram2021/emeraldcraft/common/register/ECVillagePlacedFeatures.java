package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECVillagePlacedFeatures {

	public static final Holder<PlacedFeature> DARK_OAK_VILLAGE = register(
			"dark_oak", TreeFeatures.DARK_OAK, List.of(
					BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.DARK_OAK_SAPLING.defaultBlockState(), BlockPos.ZERO))
			)
	);

	public static final Holder<PlacedFeature> FLOWER_SWAMP_VILLAGE = register(
			"flower_swamp", VegetationFeatures.FLOWER_SWAMP, List.of()
	);

	private static Holder<PlacedFeature> register(String id, Holder<? extends ConfiguredFeature<?, ?>> cf, List<PlacementModifier> modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, id), new PlacedFeature(Holder.hackyErase(cf), List.copyOf(modifiers)));
	}
}
