package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.screens.*;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {
	public static void modConstruction() {
		IReloadableResourceManager reloadableManager = (IReloadableResourceManager)Minecraft.getInstance().getResourceManager();
		ClientEventHandler handler = new ClientEventHandler();
		reloadableManager.registerReloadListener(handler);
	}

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event) {
		ClientMobEventSubscriber.onRegisterRenderer();
		event.enqueueWork(() -> {
			registerRenderLayers();
			registerContainersAndScreens();
			registerWoodTypes();
		});
	}

	private static void registerRenderLayers() {
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.WARPED_WART.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.CYAN_PETUNIA.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.MAGENTA_PETUNIA.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.GINKGO_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.PALM_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.PEACH_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.POTTED_CYAN_PETUNIA.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.POTTED_MAGENTA_PETUNIA.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.POTTED_GINKGO_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.POTTED_PALM_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.POTTED_PEACH_SAPLING.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.GINKGO_LEAVES.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.PALM_LEAVES.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ECBlocks.Plant.PEACH_LEAVES.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PALM_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PEACH_PLANKS.getId()).get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.WorkStation.CARPENTRY_TABLE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get(), RenderType.translucent());
	}

	private static void registerContainersAndScreens() {
		ScreenManager.register(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), CarpentryTableScreen::new);
		ScreenManager.register(ECContainerTypes.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
		ScreenManager.register(ECContainerTypes.MINERAL_TABLE_MENU.get(), MineralTableScreen::new);
		ScreenManager.register(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), ContinuousMinerScreen::new);
		ScreenManager.register(ECContainerTypes.ICE_MAKER_MENU.get(), IceMakerScreen::new);
		ScreenManager.register(ECContainerTypes.MELTER_MENU.get(), MelterScreen::new);
	}

	private static void registerWoodTypes() {
		Atlases.addWoodType(ECWoodType.GINKGO);
		Atlases.addWoodType(ECWoodType.PALM);
		Atlases.addWoodType(ECWoodType.PEACH);
	}


	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, world, pos, tintIndex) ->
						world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColors.get(0.5D, 1.0D),
				ECBlocks.Plant.GINKGO_LEAVES.get(), ECBlocks.Plant.PALM_LEAVES.get(), ECBlocks.Plant.PEACH_LEAVES.get());
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, tintIndex) ->
				event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				ECBlocks.Plant.GINKGO_LEAVES, ECBlocks.Plant.PALM_LEAVES, ECBlocks.Plant.PEACH_LEAVES);
	}
}
