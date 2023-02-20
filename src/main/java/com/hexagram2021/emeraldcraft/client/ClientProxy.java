package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.api.tradable.ITradableDataFactory;
import com.hexagram2021.emeraldcraft.client.screens.*;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyData;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import com.hexagram2021.emeraldcraft.common.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Objects;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {
	public static void modConstruction() {
		ReloadableResourceManager reloadableManager = (ReloadableResourceManager)Minecraft.getInstance().getResourceManager();
		ClientEventHandler handler = new ClientEventHandler();
		reloadableManager.registerReloadListener(handler);
	}

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			registerContainersAndScreens();
			registerBannerPatterns();
			registerWoodTypes();
			registerTradableMobDataFactories();
		});
	}

	private static void registerContainersAndScreens() {
		MenuScreens.register(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), CarpentryTableScreen::new);
		MenuScreens.register(ECContainerTypes.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
		MenuScreens.register(ECContainerTypes.MINERAL_TABLE_MENU.get(), MineralTableScreen::new);
		MenuScreens.register(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), ContinuousMinerScreen::new);
		MenuScreens.register(ECContainerTypes.ICE_MAKER_MENU.get(), IceMakerScreen::new);
		MenuScreens.register(ECContainerTypes.MELTER_MENU.get(), MelterScreen::new);
		MenuScreens.register(ECContainerTypes.RABBLE_FURNACE_MENU.get(), RabbleFurnaceScreen::new);
		MenuScreens.register(ECContainerTypes.PIGLIN_CUTEY_MERCHANT_MENU.get(), PiglinCuteyMerchantScreen::new);
	}

	private static void registerBannerPatterns() {
		ECBannerPatterns.ALL_BANNERS.forEach(entry -> {
			ResourceKey<BannerPattern> pattern = Objects.requireNonNull(entry.pattern().getKey());
			Sheets.BANNER_MATERIALS.put(pattern, new Material(Sheets.BANNER_SHEET, BannerPattern.location(pattern, true)));
			Sheets.SHIELD_MATERIALS.put(pattern, new Material(Sheets.SHIELD_SHEET, BannerPattern.location(pattern, false)));
		});
	}

	private static void registerWoodTypes() {
		Sheets.addWoodType(ECWoodType.GINKGO);
		Sheets.addWoodType(ECWoodType.PALM);
		Sheets.addWoodType(ECWoodType.PEACH);
		Sheets.addWoodType(ECWoodType.PURPURACEUS);
	}

	private static void registerTradableMobDataFactories() {
		ITradableDataFactory.registerDataFactory(
				EntityType.VILLAGER, (entity, profession, level) -> {
					Villager villager = (Villager) entity;
					villager.setNoAi(true);
					villager.setVillagerData(new VillagerData(VillagerType.PLAINS, profession, level));
				}
		);
		ITradableDataFactory.registerDataFactory(
				EntityType.WANDERING_TRADER, (entity, profession, level) -> {
					WanderingTrader wanderingTrader = (WanderingTrader) entity;
					wanderingTrader.setNoAi(true);
				}
		);
		ITradableDataFactory.registerDataFactory(
				ECEntities.PIGLIN_CUTEY, (entity, profession, level) -> {
					PiglinCuteyEntity piglinCutey = (PiglinCuteyEntity) entity;
					piglinCutey.setNoAi(true);
					piglinCutey.setPiglinCuteyData(new PiglinCuteyData(level));
				}
		);
		ITradableDataFactory.registerDataFactory(
				ECEntities.NETHER_LAMBMAN, (entity, profession, level) -> {
					NetherLambmanEntity netherLambman = (NetherLambmanEntity) entity;
					netherLambman.setNoAi(true);
				}
		);
		ITradableDataFactory.registerDataFactory(
				ECEntities.NETHER_PIGMAN, (entity, profession, level) -> {
					NetherPigmanEntity netherPigman = (NetherPigmanEntity) entity;
					netherPigman.setNoAi(true);
				}
		);
	}


	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, world, pos, tintIndex) ->
						world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.get(0.5D, 1.0D),
				ECBlocks.Plant.GINKGO_LEAVES.get(), ECBlocks.Plant.PALM_LEAVES.get(), ECBlocks.Plant.PEACH_LEAVES.get());
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) ->
				event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				ECBlocks.Plant.GINKGO_LEAVES, ECBlocks.Plant.PALM_LEAVES, ECBlocks.Plant.PEACH_LEAVES);
	}

	@SubscribeEvent
	public static void registerTextureStitchPre(TextureStitchEvent.Pre event) {
		ResourceLocation sheet = event.getAtlas().location();
		if (sheet.equals(Sheets.BANNER_SHEET) || sheet.equals(Sheets.SHIELD_SHEET)) {
			ECBannerPatterns.ALL_BANNERS.forEach(entry -> {
				ResourceKey<BannerPattern> pattern = Objects.requireNonNull(entry.pattern().getKey());
				event.addSprite(BannerPattern.location(pattern, sheet.equals(Sheets.BANNER_SHEET)));
			});
		}
	}

	@SubscribeEvent
	public static void onRegisterRecipeBookTypes(RegisterRecipeBookCategoriesEvent event) {
		ECRecipeBookTypes.init(event);
	}
}
