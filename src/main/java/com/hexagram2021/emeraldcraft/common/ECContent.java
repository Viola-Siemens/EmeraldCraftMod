package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.ECBiomeModifiers;
import com.hexagram2021.emeraldcraft.common.world.compat.ECNetherBiomeRegion;
import com.hexagram2021.emeraldcraft.common.world.compat.ECOverworldBiomeRegion;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegisterEvent;
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
		ECMemoryModuleTypes.init(bus);
		ECBannerPatterns.init(bus);
		Villages.Registers.POINTS_OF_INTEREST.register(bus);
		Villages.Registers.PROFESSIONS.register(bus);
		ECLootModifiers.init(bus);
		ECRecipes.init(bus);
		ECRecipeSerializer.init(bus);
		ECContainerTypes.init(bus);
		ECBlockEntity.init(bus);
		ECPlacementModifierType.init(bus);
		ECBiomes.init(bus);
		ECBiomeModifiers.init(bus);
		ECEnchantments.init(bus);

		runLater.accept(ModsLoadedEventSubscriber::SolveCompat);
	}

	public static void init() {
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ECSurfaceRules.overworld());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MODID, ECSurfaceRules.nether());
		Regions.register(new ECOverworldBiomeRegion(ECCommonConfig.EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT.get()));
		Regions.register(new ECNetherBiomeRegion(ECCommonConfig.EMERALD_CRAFT_NETHER_BIOMES_WEIGHT.get()));
	}

	@SubscribeEvent
	public static void onRegister(RegisterEvent event) {
		ECSounds.init(event);

		ECEntities.init(event);

		ECFeatures.init(event);

		ECPotions.init(event);

		ECStructureTypes.init();
		ECStructurePieceTypes.init();
		ECStructures.init();
		ECStructureSets.init();
	}

	@SubscribeEvent
	public static void registerEntitySpawnPlacement(SpawnPlacementRegisterEvent event) {
		event.register(ECEntities.HERRING, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.PURPLE_SPOTTED_BIGEYE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.SNAKEHEAD, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.WRAITH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
	}
}
