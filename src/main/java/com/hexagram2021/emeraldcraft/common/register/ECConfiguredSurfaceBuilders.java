package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceBuildersConfigs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredSurfaceBuilders {
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> DEAD_CRIMSON_OCEAN_BUILDER = register(
			"dead_crimson_ocean", ECSurfaceBuilders.DEAD_CRIMSON_OCEAN.configured(SurfaceBuilder.CONFIG_GRASS)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> DEAD_WARPED_OCEAN_BUILDER = register(
			"dead_warped_ocean", ECSurfaceBuilders.DEAD_WARPED_OCEAN.configured(SurfaceBuilder.CONFIG_GRASS)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> XANADU_BUILDER = register(
			"xanadu", ECSurfaceBuilders.XANADU.configured(ECSurfaceBuildersConfigs.CONFIG_XANADU)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GINKGO_FOREST_BUILDER = register(
			"ginkgo_forest", ECSurfaceBuilders.GINKGO_FOREST.configured(SurfaceBuilder.CONFIG_GRASS)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> KARST_HILLS_BUILDER = register(
			"karst_hills", ECSurfaceBuilders.KARST_HILLS.configured(ECSurfaceBuildersConfigs.CONFIG_GRAVEL_HILL)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GOLDEN_BEACH_BUILDER = register(
			"golden_beach", ECSurfaceBuilders.GOLDEN_BEACH.configured(ECSurfaceBuildersConfigs.CONFIG_GOLDEN_BEACH)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> PALM_BEACH_BUILDER = register(
			"palm_beach", ECSurfaceBuilders.PALM_BEACH.configured(SurfaceBuilder.CONFIG_DESERT)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> AZURE_DESERT_BUILDER = register(
			"azure_desert", SurfaceBuilder.DEFAULT.configured(ECSurfaceBuildersConfigs.CONFIG_AZURE_DESERT)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> JADEITE_DESERT_BUILDER = register(
			"jadeite_desert", SurfaceBuilder.DEFAULT.configured(ECSurfaceBuildersConfigs.CONFIG_JADEITE_DESERT)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> EMERY_DESERT_BUILDER = register(
			"emery_desert", ECSurfaceBuilders.EMERY_DESERT.configured(ECSurfaceBuildersConfigs.CONFIG_EMERY_DESERT)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> QUARTZ_DESERT_BUILDER = register(
			"quartz_desert", ECSurfaceBuilders.QUARTZ_DESERT.configured(ECSurfaceBuildersConfigs.CONFIG_QUARTZ_DESERT)
	);

	public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> NETHER_GARDEN = register(
			"nether_garden", ECSurfaceBuilders.NETHER_GARDEN.configured(SurfaceBuilder.CONFIG_CRIMSON_FOREST)
	);

	private static <SC extends SurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> register(String id, ConfiguredSurfaceBuilder<SC> builder) {
		return Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MODID, id), builder);
	}
}
