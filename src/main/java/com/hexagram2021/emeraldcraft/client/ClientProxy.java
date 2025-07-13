package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.api.tradable.ITradableDataFactory;
import com.hexagram2021.emeraldcraft.client.screens.*;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyData;
import com.hexagram2021.emeraldcraft.common.register.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClientProxy extends CommonProxy {
    public static void modConstruction() {
        ClientEventHandler handler = new ClientEventHandler();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(handler);
        setup();
    }

    public static void setup() {
        registerContainersAndScreens();
        registerBannerPatterns();
        registerWoodTypes();
        registerTradableMobDataFactories();
        registerFluidRenders();
        registerBlockColors();
        registerItemColors();
        onRegisterRecipeBookTypes();
    }

    private static void registerFluidRenders() {
        registerFluidRender("resin", ECFluids.RESIN, ECFluids.FLOWING_RESIN);
        registerFluidRender("melted_emerald", ECFluids.MELTED_EMERALD, ECFluids.FLOWING_MELTED_EMERALD);
        registerFluidRender("melted_iron", ECFluids.MELTED_IRON, ECFluids.FLOWING_MELTED_IRON);
        registerFluidRender("melted_gold", ECFluids.MELTED_GOLD, ECFluids.FLOWING_MELTED_GOLD);
        registerFluidRender("melted_copper", ECFluids.MELTED_COPPER, ECFluids.FLOWING_MELTED_COPPER);
        registerFluidRender("melted_zinc", ECFluids.MELTED_ZINC, ECFluids.FLOWING_MELTED_ZINC);
        registerFluidRender("melted_aluminum", ECFluids.MELTED_ALUMINUM, ECFluids.FLOWING_MELTED_ALUMINUM);
        registerFluidRender("melted_lead", ECFluids.MELTED_LEAD, ECFluids.FLOWING_MELTED_LEAD);
        registerFluidRender("melted_silver", ECFluids.MELTED_SILVER, ECFluids.FLOWING_MELTED_SILVER);
        registerFluidRender("melted_nickel", ECFluids.MELTED_NICKEL, ECFluids.FLOWING_MELTED_NICKEL);
        registerFluidRender("melted_uranium", ECFluids.MELTED_URANIUM, ECFluids.FLOWING_MELTED_URANIUM);
    }

    private static <T extends Fluid> void registerFluidRender(String name, T still, T flowing) {
        FluidRenderHandlerRegistry.INSTANCE.register(
                still,
                flowing,
                new SimpleFluidRenderHandler(
                        EmeraldCraft.id("block/fluid/" + name + "_still"),
                        EmeraldCraft.id("block/fluid/" + name + "_flowing")
                ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), still, flowing);
    }

    private static void registerContainersAndScreens() {
        MenuScreens.register(ECContainerTypes.CARPENTRY_TABLE_MENU, CarpentryTableScreen::new);
        MenuScreens.register(ECContainerTypes.GLASS_KILN_MENU, GlassKilnScreen::new);
        MenuScreens.register(ECContainerTypes.MINERAL_TABLE_MENU, MineralTableScreen::new);
        MenuScreens.register(ECContainerTypes.CONTINUOUS_MINER_MENU, ContinuousMinerScreen::new);
        MenuScreens.register(ECContainerTypes.ICE_MAKER_MENU, IceMakerScreen::new);
        MenuScreens.register(ECContainerTypes.MELTER_MENU, MelterScreen::new);
        MenuScreens.register(ECContainerTypes.RABBLE_FURNACE_MENU, RabbleFurnaceScreen::new);
        MenuScreens.register(ECContainerTypes.PIGLIN_CUTEY_MERCHANT_MENU, PiglinCuteyMerchantScreen::new);
    }

    private static void registerBannerPatterns() {
        ECBannerPatterns.ALL_BANNERS.forEach(entry -> {
            Optional<ResourceKey<BannerPattern>> pattern = Objects.requireNonNull(BuiltInRegistries.BANNER_PATTERN.getResourceKey(entry.pattern()));
            if (pattern.isPresent()) {
                Sheets.BANNER_MATERIALS.put(pattern.get(), new Material(Sheets.BANNER_SHEET, BannerPattern.location(pattern.get(), true)));
                Sheets.SHIELD_MATERIALS.put(pattern.get(), new Material(Sheets.SHIELD_SHEET, BannerPattern.location(pattern.get(), false)));
            }
        });
    }

    private static void registerWoodTypes() {
        addWoodType(ECWoodType.GINKGO);
        addWoodType(ECWoodType.PALM);
        addWoodType(ECWoodType.PEACH);
        addWoodType(ECWoodType.PURPURACEUS);
    }

    private static void addWoodType(WoodType woodType) {
        Sheets.SIGN_MATERIALS.put(woodType, Sheets.createSignMaterial(woodType));
        Sheets.HANGING_SIGN_MATERIALS.put(woodType, Sheets.createHangingSignMaterial(woodType));
    }

    private static void registerTradableMobDataFactories() {
        ITradableDataFactory.registerDataFactory(
                EntityType.VILLAGER, (villager, profession, level) -> {
                    villager.setNoAi(true);
                    villager.setVillagerData(new VillagerData(VillagerType.PLAINS, Objects.requireNonNull(profession), level));
                }
        );
        ITradableDataFactory.registerDataFactory(
                EntityType.WANDERING_TRADER, (wanderingTrader, profession, level) -> wanderingTrader.setNoAi(true)
        );
        ITradableDataFactory.registerDataFactory(
                ECEntities.PIGLIN_CUTEY, (piglinCutey, profession, level) -> {
                    piglinCutey.setNoAi(true);
                    piglinCutey.setPiglinCuteyData(new PiglinCuteyData(level));
                }
        );
        ITradableDataFactory.registerDataFactory(
                ECEntities.NETHER_LAMBMAN, (netherLambman, profession, level) -> netherLambman.setNoAi(true)
        );
        ITradableDataFactory.registerDataFactory(
                ECEntities.NETHER_PIGMAN, (netherPigman, profession, level) -> netherPigman.setNoAi(true)
        );
    }

    public static void registerBlockColors() {
        //leaves
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.get(0.5D, 1.0D),
                ECBlocks.Plant.GINKGO_LEAVES.get(), ECBlocks.Plant.PALM_LEAVES.get(), ECBlocks.Plant.PEACH_LEAVES.get());
    }

    //after registerBlockColors()
    public static void registerItemColors() {
        //leaves
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    Block block = Block.byItem(stack.getItem());
                    BlockColor blockColor = ColorProviderRegistry.BLOCK.get(block);
                    assert blockColor != null;
                    return blockColor.getColor(block.defaultBlockState(), null, null, tintIndex);
                },
                ECBlocks.Plant.GINKGO_LEAVES, ECBlocks.Plant.PALM_LEAVES, ECBlocks.Plant.PEACH_LEAVES);
    }

    public static void onRegisterRecipeBookTypes() {
        ECRecipeBookTypes.init();
    }
}
