package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.surface.AzureDesertSurfaceBuilder;
import com.hexagram2021.emeraldcraft.common.world.surface.EmeryDesertSurfaceBuilder;
import com.hexagram2021.emeraldcraft.common.world.surface.JadeiteDesertSurfaceBuilder;
import com.hexagram2021.emeraldcraft.common.world.surface.QuartzDesertSurfaceBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredSurfaceBuilder {
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> AZURE_DESERT_BUILDER = register(
			"azure_desert", SurfaceBuilder.DEFAULT.configured(AzureDesertSurfaceBuilder.CONFIG)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> JADEITE_DESERT_BUILDER = register(
			"jadeite_desert", SurfaceBuilder.DEFAULT.configured(JadeiteDesertSurfaceBuilder.CONFIG)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> EMERY_DESERT_BUILDER = register(
			"emery_desert", SurfaceBuilder.DEFAULT.configured(EmeryDesertSurfaceBuilder.CONFIG)
	);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> QUARTZ_DESERT_BUILDER = register(
			"quartz_desert", SurfaceBuilder.DEFAULT.configured(QuartzDesertSurfaceBuilder.CONFIG)
	);

	private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> register(String id, ConfiguredSurfaceBuilder<SC> builder) {
		return Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MODID, id), builder);
	}
}
