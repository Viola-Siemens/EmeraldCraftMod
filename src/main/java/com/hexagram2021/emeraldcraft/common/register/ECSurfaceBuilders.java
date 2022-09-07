package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.surface.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECSurfaceBuilders {
	public static final DeadCrimsonOceanSurfaceBuilder DEAD_CRIMSON_OCEAN = new DeadCrimsonOceanSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final DeadWarpedOceanSurfaceBuilder DEAD_WARPED_OCEAN = new DeadWarpedOceanSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final XanaduSurfaceBuilder XANADU = new XanaduSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final GinkgoForestSurfaceBuilder GINKGO_FOREST = new GinkgoForestSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final KarstHillsSurfaceBuilder KARST_HILLS = new KarstHillsSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final GoldenBeachSurfaceBuilder GOLDEN_BEACH = new GoldenBeachSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final PalmBeachSurfaceBuilder PALM_BEACH = new PalmBeachSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final EmeryDesertSurfaceBuilder EMERY_DESERT = new EmeryDesertSurfaceBuilder(SurfaceBuilderConfig.CODEC);
	public static final QuartzDesertSurfaceBuilder QUARTZ_DESERT = new QuartzDesertSurfaceBuilder(SurfaceBuilderConfig.CODEC);

	public static final NetherGardenSurfaceBuilder NETHER_GARDEN = new NetherGardenSurfaceBuilder(SurfaceBuilderConfig.CODEC);

	public static void init(RegistryEvent.Register<SurfaceBuilder<?>> event) {
		DEAD_CRIMSON_OCEAN.setRegistryName(new ResourceLocation(MODID, "dead_crimson_ocean"));
		DEAD_WARPED_OCEAN.setRegistryName(new ResourceLocation(MODID, "dead_warped_ocean"));
		XANADU.setRegistryName(new ResourceLocation(MODID, "xanadu"));
		GINKGO_FOREST.setRegistryName(new ResourceLocation(MODID, "ginkgo_forest"));
		KARST_HILLS.setRegistryName(new ResourceLocation(MODID, "karst_hills"));
		GOLDEN_BEACH.setRegistryName(new ResourceLocation(MODID, "golden_beach"));
		PALM_BEACH.setRegistryName(new ResourceLocation(MODID, "palm_beach"));
		EMERY_DESERT.setRegistryName(new ResourceLocation(MODID, "emery_desert"));
		QUARTZ_DESERT.setRegistryName(new ResourceLocation(MODID, "quartz_desert"));
		NETHER_GARDEN.setRegistryName(new ResourceLocation(MODID, "nether_garden"));

		event.getRegistry().register(DEAD_CRIMSON_OCEAN);
		event.getRegistry().register(DEAD_WARPED_OCEAN);
		event.getRegistry().register(XANADU);
		event.getRegistry().register(GINKGO_FOREST);
		event.getRegistry().register(KARST_HILLS);
		event.getRegistry().register(GOLDEN_BEACH);
		event.getRegistry().register(PALM_BEACH);
		event.getRegistry().register(EMERY_DESERT);
		event.getRegistry().register(QUARTZ_DESERT);
		event.getRegistry().register(NETHER_GARDEN);
	}
}
