package com.hexagram2021.emeraldcraft.common.world.surface;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class ECSurfaceBuildersConfigs {
	private static final BlockState BASALT = Blocks.BASALT.defaultBlockState();
	private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
	private static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
	private static final BlockState CRYING_OBSIDIAN = Blocks.CRYING_OBSIDIAN.defaultBlockState();
	private static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
	private static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.defaultBlockState();
	private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
	private static final BlockState RED_SAND = Blocks.RED_SAND.defaultBlockState();
	private static final BlockState SAND = Blocks.SAND.defaultBlockState();
	private static final BlockState STONE = Blocks.STONE.defaultBlockState();

	private static final BlockState AZURE_SAND = ECBlocks.Decoration.AZURE_SAND.defaultBlockState();
	private static final BlockState AZURE_SANDSTONE = ECBlocks.Decoration.AZURE_SANDSTONE.defaultBlockState();
	private static final BlockState JADEITE_SAND = ECBlocks.Decoration.JADEITE_SAND.defaultBlockState();
	private static final BlockState JADEITE_SANDSTONE = ECBlocks.Decoration.JADEITE_SANDSTONE.defaultBlockState();
	private static final BlockState DARK_SAND = ECBlocks.Decoration.DARK_SAND.defaultBlockState();
	private static final BlockState EMERY_SAND = ECBlocks.Decoration.JADEITE_SAND.defaultBlockState();
	private static final BlockState EMERY_SANDSTONE = ECBlocks.Decoration.JADEITE_SANDSTONE.defaultBlockState();
	private static final BlockState QUARTZ_SAND = ECBlocks.Decoration.JADEITE_SAND.defaultBlockState();
	private static final BlockState QUARTZ_SANDSTONE = ECBlocks.Decoration.JADEITE_SANDSTONE.defaultBlockState();
	private static final BlockState MOSS_BLOCK = ECBlocks.Plant.MOSS_BLOCK.defaultBlockState();
	private static final BlockState CRIMSON_STONE = ECBlocks.Decoration.CRIMSON_STONE.defaultBlockState();
	private static final BlockState WARPED_STONE = ECBlocks.Decoration.WARPED_STONE.defaultBlockState();

	private ECSurfaceBuildersConfigs() {}

	public static final SurfaceBuilderConfig CONFIG_AZURE_DESERT = new SurfaceBuilderConfig(AZURE_SAND, AZURE_SANDSTONE, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_COARSE_DESERT = new SurfaceBuilderConfig(COARSE_DIRT, SAND, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_EMERY_DESERT = new SurfaceBuilderConfig(EMERY_SAND, EMERY_SANDSTONE, BLACKSTONE);
	public static final SurfaceBuilderConfig CONFIG_GOLDEN_BEACH = new SurfaceBuilderConfig(DARK_SAND, DARK_SAND, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_GRAVEL_HILL = new SurfaceBuilderConfig(GRAVEL, STONE, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_JADEITE_DESERT = new SurfaceBuilderConfig(JADEITE_SAND, JADEITE_SANDSTONE, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_QUARTZ_DESERT = new SurfaceBuilderConfig(QUARTZ_SAND, QUARTZ_SANDSTONE, NETHERRACK);
	public static final SurfaceBuilderConfig CONFIG_RED_BEACH = new SurfaceBuilderConfig(RED_SAND, SAND, GRAVEL);
	public static final SurfaceBuilderConfig CONFIG_XANADU = new SurfaceBuilderConfig(MOSS_BLOCK, DIRT, STONE);

	public static final SurfaceBuilderConfig CONFIG_CRIMSON_OCEAN = new SurfaceBuilderConfig(CRIMSON_STONE, CRIMSON_STONE, CRIMSON_STONE);
	public static final SurfaceBuilderConfig CONFIG_CRIMSON_OCEAN_CRYING_OBSIDIAN = new SurfaceBuilderConfig(CRIMSON_STONE, CRIMSON_STONE, CRYING_OBSIDIAN);
	public static final SurfaceBuilderConfig CONFIG_CRIMSON_OCEAN_MAGMA = new SurfaceBuilderConfig(CRIMSON_STONE, CRIMSON_STONE, MAGMA_BLOCK);
	public static final SurfaceBuilderConfig CONFIG_WARPED_OCEAN = new SurfaceBuilderConfig(WARPED_STONE, WARPED_STONE, WARPED_STONE);
	public static final SurfaceBuilderConfig CONFIG_WARPED_OCEAN_BASALT = new SurfaceBuilderConfig(CRIMSON_STONE, CRIMSON_STONE, BASALT);
	public static final SurfaceBuilderConfig CONFIG_WARPED_OCEAN_GRAVEL = new SurfaceBuilderConfig(CRIMSON_STONE, CRIMSON_STONE, GRAVEL);
}
