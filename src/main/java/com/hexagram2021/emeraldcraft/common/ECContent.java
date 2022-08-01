package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.world.Villages;
import com.hexagram2021.emeraldcraft.common.world.compat.*;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECContent {
	public static void modConstruction(IEventBus bus, Consumer<Runnable> runLater) {
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
		ECPlacementModifierType.init(bus);

		runLater.accept(ModsLoadedEventSubscriber::SolveCompat);
	}

	public static void init() {
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ECSurfaceRules.overworld());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MODID, ECSurfaceRules.nether());
		Regions.register(new ECOverworldBiomeRegion(40));
		Regions.register(new ECNetherBiomeRegion(40));
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
		ECStructureSets.init();
	}
}
