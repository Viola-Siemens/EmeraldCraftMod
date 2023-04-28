package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.world.compat.ECNetherBiomeRegion;
import com.hexagram2021.emeraldcraft.common.world.compat.ECOverworldBiomeRegion;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECContent {
	public static void modConstruction(IEventBus bus, Consumer<Runnable> runLater) {
		ModsLoadedEventSubscriber.compatModLoaded();

		ECWoodType.init();
		ECBlocks.init(bus);
		ECItems.init(bus);
		ECMemoryModuleTypes.init(bus);
		Villages.Registers.POINTS_OF_INTEREST.register(bus);
		Villages.Registers.PROFESSIONS.register(bus);
		ECRecipes.init(bus);
		ECRecipeSerializer.init(bus);
		ECContainerTypes.init(bus);
		ECBlockEntity.init(bus);
		ECEntities.init(bus);
		ECPlacementModifierType.init(bus);
		ECBiomes.init(bus);

		runLater.accept(ModsLoadedEventSubscriber::SolveCompat);
	}

	public static void init() {
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ECSurfaceRules.overworld());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MODID, ECSurfaceRules.nether());
		Regions.register(new ECOverworldBiomeRegion(ECCommonConfig.EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT.get()));
		Regions.register(new ECNetherBiomeRegion(ECCommonConfig.EMERALD_CRAFT_NETHER_BIOMES_WEIGHT.get()));
	}

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		ECFeatures.init(event);
	}

	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		ECStructures.init(event);
		ECStructurePieceTypes.init();
		ECConfiguredStructures.init();
		ECStructureSets.init();
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		ECPotions.init(event);
		ECBrewingRecipes.init();
	}

	public static void registerEntitySpawnPlacement() {
		SpawnPlacements.register(
				ECEntities.HERRING.get(), SpawnPlacements.Type.IN_WATER,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules
		);
		SpawnPlacements.register(
				ECEntities.PURPLE_SPOTTED_BIGEYE.get(), SpawnPlacements.Type.IN_WATER,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules
		);
		SpawnPlacements.register(
				ECEntities.WRAITH.get(), SpawnPlacements.Type.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules
		);
	}
}
