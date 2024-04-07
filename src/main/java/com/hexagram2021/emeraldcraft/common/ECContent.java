package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.entities.mobs.WombatEntity;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
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

import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECContent {
	public static void modConstruction(IEventBus bus, Consumer<Runnable> runLater) {
		ModsLoadedEventSubscriber.compatModLoaded();

		ECBlockSetTypes.init();
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
		ECCreativeModeTabs.init(bus);
		ECPlacementModifierType.init(bus);
		ECEnchantments.init(bus);
		ECConfiguredFeatureKeys.init();
		ECPlacedFeatureKeys.init();
		ECVillagePlacedFeatureKeys.init();
		ECStructureTypes.init(bus);
		ECStructureKeys.init();
		ECStructureSetKeys.init();

		runLater.accept(ModsLoadedEventSubscriber::solveCompat);
	}

	public static void init() {
		ModsLoadedEventSubscriber.solveTerraBlender();
	}

	@SubscribeEvent
	public static void onRegister(RegisterEvent event) {
		ECSounds.init(event);
		ECEntities.init(event);
		ECFeatures.init(event);
		ECPotions.init(event);
		ECStructurePieceTypes.init();
	}

	@SubscribeEvent
	public static void registerEntitySpawnPlacement(SpawnPlacementRegisterEvent event) {
		event.register(ECEntities.HERRING, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.PURPLE_SPOTTED_BIGEYE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.SNAKEHEAD, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.WOMBAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				WombatEntity::checkWombatSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(ECEntities.WRAITH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
	}
}
