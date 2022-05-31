package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.*;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {
	public static void modConstruction() {

	}

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.WARPED_WART.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CARPENTRY_TABLE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get(), RenderType.translucent());
		registerContainersAndScreens();
	}

	private static void registerContainersAndScreens() {
		MenuScreens.register(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), CarpentryTableScreen::new);
		MenuScreens.register(ECContainerTypes.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
		MenuScreens.register(ECContainerTypes.MINERAL_TABLE_MENU.get(), MineralTableScreen::new);
		MenuScreens.register(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), ContinuousMinerScreen::new);
		MenuScreens.register(ECContainerTypes.ICE_MAKER_MENU.get(), IceMakerScreen::new);
		MenuScreens.register(ECContainerTypes.MELTER_MENU.get(), MelterScreen::new);
	}
}
