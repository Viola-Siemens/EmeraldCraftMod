package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.common.world.compat.*;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import terrablender.api.BiomeProviders;
import terrablender.api.GenerationSettings;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECContent {
	public static void modConstruction(IEventBus bus, Consumer<Runnable> runLater) {
		ModsLoadedEventSubscriber.compatModLoaded();

		ECWoodType.init();
		ECBlocks.init(bus);
		ECItems.init(bus);
		ECMemoryModuleTypes.init(bus);
		Villages.Registers.POINTS_OF_INTEREST.register(bus);
		Villages.Registers.PROFESSIONS.register(bus);
		ECContainerTypes.init(bus);
		ECRecipeSerializer.init(bus);
		ECBlockEntity.init(bus);
		ECEntities.init(bus);
		ECPlacementModifierType.init();

		runLater.accept(ModsLoadedEventSubscriber::SolveCompat);
	}

	public static void init() {
		GenerationSettings.addBeforeBedrockOverworldSurfaceRules(ECSurfaceRules.overworld());
		GenerationSettings.addBeforeBedrockNetherSurfaceRules(ECSurfaceRules.nether());
		BiomeProviders.register(new ECBiomeProvider(40));
	}

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		ECBiomes.init(event);
	}

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		ECFeatures.init(event);
	}

	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		ECStructures.init(event);
		ECConfiguredStructures.init();
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		ECPotions.init(event);
		ECBrewingRecipes.init();
	}
}
