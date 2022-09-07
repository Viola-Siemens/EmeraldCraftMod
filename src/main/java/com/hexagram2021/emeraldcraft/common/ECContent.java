package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.world.Villages;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECContent {
	public static void modConstruction(IEventBus bus) {
		ModsLoadedEventSubscriber.compatModLoaded();

		ECWoodType.init();
		ECBlocks.init(bus);
		ECItems.init(bus);
		Villages.Registers.POINTS_OF_INTEREST.register(bus);
		Villages.Registers.PROFESSIONS.register(bus);
		ECContainerTypes.init(bus);
		ECRecipeSerializer.init(bus);
		ECBlockEntity.init(bus);
		ECEntities.init(bus);
	}

	public static void init() {
	}

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		ECBiomes.init(event);
	}

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		ECFeatures.init(event);
		ECConfiguredFeatures.init();
	}

	@SubscribeEvent
	public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event) {
		ECSurfaceBuilders.init(event);
	}

	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {
		ECStructures.init(event);
		ECConfiguredStructures.init();
	}
}
