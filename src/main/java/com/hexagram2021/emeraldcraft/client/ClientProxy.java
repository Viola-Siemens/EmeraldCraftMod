package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.*;
import com.hexagram2021.emeraldcraft.common.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
			registerRenderLayers();
			registerContainersAndScreens();
			registerBannerPatterns();
			registerWoodTypes();
		});
	}

	private static void registerRenderLayers() {
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.WARPED_WART.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.CYAN_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.MAGENTA_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.HIGAN_BANA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.GINKGO_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PALM_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PEACH_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PURPURACEUS_FUNGUS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_CYAN_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_MAGENTA_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_HIGAN_BANA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_GINKGO_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_PALM_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_PEACH_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_PURPURACEUS_FUNGUS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.GINKGO_LEAVES.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PALM_LEAVES.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PEACH_LEAVES.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.PURPURACEUS_ROOTS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PURPURACEUS_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CARPENTRY_TABLE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get(), RenderType.translucent());
	}

	private static void registerContainersAndScreens() {
		MenuScreens.register(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), CarpentryTableScreen::new);
		MenuScreens.register(ECContainerTypes.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
		MenuScreens.register(ECContainerTypes.MINERAL_TABLE_MENU.get(), MineralTableScreen::new);
		MenuScreens.register(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), ContinuousMinerScreen::new);
		MenuScreens.register(ECContainerTypes.ICE_MAKER_MENU.get(), IceMakerScreen::new);
		MenuScreens.register(ECContainerTypes.MELTER_MENU.get(), MelterScreen::new);
	}

	private static void registerBannerPatterns() {
		registerBannerPattern(ECItems.BannerPatterns.BEE.get().getBannerPattern());
		registerBannerPattern(ECItems.BannerPatterns.SNOW.get().getBannerPattern());
		registerBannerPattern(ECItems.BannerPatterns.BOTTLE.get().getBannerPattern());
		registerBannerPattern(ECItems.BannerPatterns.POTION.get().getBannerPattern());
	}

	private static void registerBannerPattern(BannerPattern pattern) {
		Sheets.BANNER_MATERIALS.put(pattern, new Material(Sheets.BANNER_SHEET, pattern.location(true)));
		Sheets.SHIELD_MATERIALS.put(pattern, new Material(Sheets.SHIELD_SHEET, pattern.location(false)));
	}

	private static void registerWoodTypes() {
		Sheets.addWoodType(ECWoodType.GINKGO);
		Sheets.addWoodType(ECWoodType.PALM);
		Sheets.addWoodType(ECWoodType.PEACH);
		Sheets.addWoodType(ECWoodType.PURPURACEUS);
	}


	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, world, pos, tintIndex) ->
						world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.get(0.5D, 1.0D),
				ECBlocks.Plant.GINKGO_LEAVES.get(), ECBlocks.Plant.PALM_LEAVES.get(), ECBlocks.Plant.PEACH_LEAVES.get());
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, tintIndex) ->
				event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				ECBlocks.Plant.GINKGO_LEAVES, ECBlocks.Plant.PALM_LEAVES, ECBlocks.Plant.PEACH_LEAVES);
	}

	@SubscribeEvent
	public static void registerTextureStitchPre(TextureStitchEvent.Pre event) {
		ResourceLocation sheet = event.getAtlas().location();
		if (sheet.equals(Sheets.BANNER_SHEET) || sheet.equals(Sheets.SHIELD_SHEET)) {
			event.addSprite(ECItems.BannerPatterns.BEE.get().getBannerPattern().location(sheet.equals(Sheets.BANNER_SHEET)));
			event.addSprite(ECItems.BannerPatterns.SNOW.get().getBannerPattern().location(sheet.equals(Sheets.BANNER_SHEET)));
			event.addSprite(ECItems.BannerPatterns.BOTTLE.get().getBannerPattern().location(sheet.equals(Sheets.BANNER_SHEET)));
			event.addSprite(ECItems.BannerPatterns.POTION.get().getBannerPattern().location(sheet.equals(Sheets.BANNER_SHEET)));
		}
	}
}
