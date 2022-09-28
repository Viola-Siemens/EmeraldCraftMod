package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.ECBiomeModifiers;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.common.world.compat.*;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
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
		ECBannerPatterns.init(bus);
		Villages.Registers.POINTS_OF_INTEREST.register(bus);
		Villages.Registers.PROFESSIONS.register(bus);
		ECContainerTypes.init(bus);
		ECRecipeSerializer.init(bus);
		ECBlockEntity.init(bus);
		ECPlacementModifierType.init(bus);
		ECBiomes.init(bus);
		ECBiomeModifiers.init(bus);

		runLater.accept(ECBrewingRecipes::init);
		runLater.accept(ModsLoadedEventSubscriber::SolveCompat);
	}

	public static void init() {
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ECSurfaceRules.overworld());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MODID, ECSurfaceRules.nether());
		Regions.register(new ECOverworldBiomeRegion(40));
		Regions.register(new ECNetherBiomeRegion(40));
	}

	@SubscribeEvent
	public static void onRegister(RegisterEvent event) {
		ECSounds.init(event);

		ECEntities.init(event);

		ECFeatures.init(event);
		ECRecipes.init(event);

		ECPotions.init(event);

		ECStructureTypes.init();
		ECStructurePieceTypes.init();
		ECStructures.init();
		ECStructureSets.init();
	}

	@SubscribeEvent
	public static void onRegisterRecipeBookTypes(RegisterRecipeBookCategoriesEvent event) {
		ECRecipeBookTypes.init(event);
	}
}
