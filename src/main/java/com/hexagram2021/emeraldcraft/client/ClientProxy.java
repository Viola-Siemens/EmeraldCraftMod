package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.*;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECWoodType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
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
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.WARPED_WART.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.CYAN_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.MAGENTA_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.GINKGO_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_CYAN_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_MAGENTA_PETUNIA.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.POTTED_GINKGO_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.Plant.GINKGO_LEAVES.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_DOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.GINKGO_PLANKS.getId()).get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CARPENTRY_TABLE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get(), RenderType.translucent());

		registerContainersAndScreens();

		registerWoodTypes();
	}

	private static void registerContainersAndScreens() {
		MenuScreens.register(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), CarpentryTableScreen::new);
		MenuScreens.register(ECContainerTypes.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
		MenuScreens.register(ECContainerTypes.MINERAL_TABLE_MENU.get(), MineralTableScreen::new);
		MenuScreens.register(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), ContinuousMinerScreen::new);
		MenuScreens.register(ECContainerTypes.ICE_MAKER_MENU.get(), IceMakerScreen::new);
		MenuScreens.register(ECContainerTypes.MELTER_MENU.get(), MelterScreen::new);
	}

	private static void registerWoodTypes() {
		Sheets.addWoodType(ECWoodType.GINKGO);
	}


	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, world, pos, tintIndex) ->
						world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.get(0.5D, 1.0D),
				ECBlocks.Plant.GINKGO_LEAVES.get());
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, tintIndex) ->
				event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				ECBlocks.Plant.GINKGO_LEAVES);
	}
}
