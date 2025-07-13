package com.hexagram2021.emeraldcraft;

import cn.sh1rocu.emeraldcraft.util.forge.DistExecutor;
import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.api.tradable.TradeListingUtils;
import com.hexagram2021.emeraldcraft.client.ClientProxy;
import com.hexagram2021.emeraldcraft.common.*;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.BiomeUtil;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.mixin.accessor.BlockEntityTypeAccess;
import com.hexagram2021.emeraldcraft.network.*;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class EmeraldCraft implements ModInitializer {
    public static final String MODID = "emeraldcraft";
    public static final String MODNAME = "Emerald Craft";
    private static MinecraftServer currentServer = null;

    public static final CommonProxy proxy = DistExecutor.safeRunForDist(
            bootstrapErrorToXCPInDev(() -> ClientProxy::new),
            bootstrapErrorToXCPInDev(() -> CommonProxy::new)
    );

    public static <T> Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return in;
        }
        return () -> {
            try {
                return in.get();
            } catch (BootstrapMethodError e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }

    @Override
    public void onInitialize() {
        tagsUpdated();
        serverStarted();
        datapackSync();
        ServerWorldEvents.LOAD.register(BiomeUtil::onWorldLoad);
        ServerWorldEvents.UNLOAD.register(BiomeUtil::onWorldUnload);
        ECContent.modConstruction();

        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, ECCommonConfig.SPEC);
        ForgeServerEventHandler.init();

        setup();
        enqueueIMC();
    }

    public void setup() {
        ECTriggers.init();
        ECBrewingRecipes.init();

        VillagerType.BY_BIOME.putAll(ImmutableMap.of(
                ECBiomeKeys.AZURE_DESERT.key(), VillagerType.DESERT,
                ECBiomeKeys.JADEITE_DESERT.key(), VillagerType.DESERT,
                ECBiomeKeys.XANADU.key(), VillagerType.SWAMP
        ));
        ECContent.init();
        appendBlocksToBlockEntities();
        ModVanillaCompat.setup();

        TradeListingUtils.registerTradeListing(VillagerTrades.WANDERING_TRADER_TRADES, EntityType.WANDERING_TRADER, null);
        TradeListingUtils.registerTradeListing(ECTrades.PIGLIN_CUTEY_TRADES, ECEntities.PIGLIN_CUTEY, null);
        TradeListingUtils.registerTradeListing(ECTrades.NETHER_LAMBMAN_TRADES, ECEntities.NETHER_LAMBMAN, null);
        TradeListingUtils.registerTradeListing(ECTrades.NETHER_PIGMAN_TRADES, ECEntities.NETHER_PIGMAN, null);

        ClientPlayNetworking.registerGlobalReceiver(ClientboundTradeSyncPacket.TYPE, (packet, localPlayer, packetSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(ClientboundIceMakerFluidSyncPacket.TYPE, (packet, localPlayer, packetSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(ClientboundContinuousMinerFluidSyncPacket.TYPE, (packet, localPlayer, packetSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(ClientboundMelterFluidSyncPacket.TYPE, (packet, localPlayer, packetSender) -> packet.handle());
    }

    private void enqueueIMC() {
        if (FabricLoader.getInstance().isModLoaded("diet")) {
            //I'm waiting for your migration!
            ECFoods.compatDiet(ECItems.CHILI.get(), ECFoods.CHILI);
            ECFoods.compatDiet(ECItems.CABBAGE.get(), ECFoods.CABBAGE);
            ECFoods.compatDiet(ECItems.AGATE_APPLE.get(), ECFoods.AGATE_APPLE);
            ECFoods.compatDiet(ECItems.JADE_APPLE.get(), ECFoods.JADE_APPLE);
            ECFoods.compatDiet(ECItems.GINKGO_NUT.get(), ECFoods.GINKGO_NUT);
            ECFoods.compatDiet(ECItems.PEACH.get(), ECFoods.PEACH);
            ECFoods.compatDiet(ECItems.GOLDEN_PEACH.get(), ECFoods.GOLDEN_PEACH);
            ECFoods.compatDiet(ECItems.COOKED_TROPICAL_FISH.get(), ECFoods.COOKED_TROPICAL_FISH);
            ECFoods.compatDiet(ECItems.POTION_COOKIE.get(), ECFoods.POTION_COOKIE);
            ECFoods.compatDiet(ECItems.COOKED_PURPURACEUS_FUNGUS.get(), ECFoods.COOKED_PURPURACEUS_FUNGUS);
            ECFoods.compatDiet(ECItems.BOILED_EGG.get(), ECFoods.BOILED_EGG);
            ECFoods.compatDiet(ECItems.CHORUS_FLOWER_EGGDROP_SOUP.get(), ECFoods.CHORUS_FLOWER_EGGDROP_SOUP);
            ECFoods.compatDiet(ECItems.CARAMELIZED_POTATO.get(), ECFoods.CARAMELIZED_POTATO);
            ECFoods.compatDiet(ECItems.ROUGAMO.get(), ECFoods.ROUGAMO);
            ECFoods.compatDiet(ECItems.BEEF_AND_POTATO_STEW.get(), ECFoods.BEEF_AND_POTATO_STEW);
            ECFoods.compatDiet(ECItems.BRAISED_CHICKEN.get(), ECFoods.BRAISED_CHICKEN);
            ECFoods.compatDiet(ECItems.SAUERKRAUT_FISH.get(), ECFoods.SAUERKRAUT_FISH);
            ECFoods.compatDiet(ECItems.HERRING.get(), ECFoods.HERRING);
            ECFoods.compatDiet(ECItems.PURPLE_SPOTTED_BIGEYE.get(), ECFoods.PURPLE_SPOTTED_BIGEYE);
            ECFoods.compatDiet(ECItems.SNAKEHEAD.get(), ECFoods.SNAKEHEAD);
            ECFoods.compatDiet(ECItems.COOKED_HERRING.get(), ECFoods.COOKED_HERRING);
            ECFoods.compatDiet(ECItems.COOKED_PURPLE_SPOTTED_BIGEYE.get(), ECFoods.COOKED_PURPLE_SPOTTED_BIGEYE);
            ECFoods.compatDiet(ECItems.COOKED_SNAKEHEAD.get(), ECFoods.COOKED_SNAKEHEAD);
            ECFoods.compatDiet(ECItems.SAUSAGE.get(), ECFoods.SAUSAGE);
            ECFoods.compatDiet(ECItems.COOKED_SAUSAGE.get(), ECFoods.COOKED_SAUSAGE);
            ECFoods.compatDiet(ECItems.GLUTEN.get(), ECFoods.GLUTEN);
            ECFoods.compatDiet(ECItems.WARDEN_HEART.get(), ECFoods.WARDEN_HEART);
            ECFoods.compatDiet(ECItems.STIR_FRIED_WARDEN_HEART.get(), ECFoods.STIR_FRIED_WARDEN_HEART);
            ECFoods.compatDiet(ECItems.APPLE_JUICE.get(), ECFoods.APPLE_JUICE);
            ECFoods.compatDiet(ECItems.BEETROOT_JUICE.get(), ECFoods.BEETROOT_JUICE);
            ECFoods.compatDiet(ECItems.CARROT_JUICE.get(), ECFoods.CARROT_JUICE);
            ECFoods.compatDiet(ECItems.MELON_JUICE.get(), ECFoods.MELON_JUICE);
            ECFoods.compatDiet(ECItems.PEACH_JUICE.get(), ECFoods.PEACH_JUICE);
            ECFoods.compatDiet(ECItems.PUMPKIN_JUICE.get(), ECFoods.PUMPKIN_JUICE);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void tagsUpdated() {
        CommonLifecycleEvents.TAGS_LOADED.register((registryAccess, b) -> {
            CachedRecipeList.onTagsUpdated();
            ECStructures.init(registryAccess);
            Villages.addAllStructuresToPool(registryAccess);
        });
    }

    public void serverStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            currentServer = server;
            ServerLevel world = server.getLevel(Level.OVERWORLD);
            assert world != null;
            if (!world.isClientSide) {
                ECSaveData worldData = world.getDataStorage().computeIfAbsent(ECSaveData::new, ECSaveData::new, ECSaveData.dataName);
                ECSaveData.setInstance(worldData);
            }
            // // Run these checks when debugging:
            // com.hexagram2021.emeraldcraft.common.util.RegistryChecker.registryCheck(event.getServer().getLootData());
            // com.hexagram2021.emeraldcraft.common.util.RegistryChecker.recipeCheck(event.getServer().getLootData(), event.getServer().getRecipeManager(), event.getServer().registryAccess());

        });
    }

    public void datapackSync() {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, playerConnected) -> {
            IECPacket packet = new ClientboundTradeSyncPacket(TradeShadowRecipe.getAllJobsites(), TradeShadowRecipe.getTradeRecipes(player.getServer().overworld()));
            NetworkHandler.sendMessageToPlayer(packet, player);
        });
    }

    private static void appendBlocksToBlockEntities() {
        BlockEntityTypeAccess signBuilderAccess = (BlockEntityTypeAccess) BlockEntityType.SIGN;
        Set<Block> signValidBlocks = new ObjectOpenHashSet<>(signBuilderAccess.ec_getValidBlocks());

        Tuple<ECBlocks.BlockEntry<StandingSignBlock>, ECBlocks.BlockEntry<WallSignBlock>> tuple_ginkgo = ECBlocks.TO_SIGN.get(ECBlocks.Plant.GINKGO_PLANKS.getId());
        Tuple<ECBlocks.BlockEntry<StandingSignBlock>, ECBlocks.BlockEntry<WallSignBlock>> tuple_palm = ECBlocks.TO_SIGN.get(ECBlocks.Plant.PALM_PLANKS.getId());
        Tuple<ECBlocks.BlockEntry<StandingSignBlock>, ECBlocks.BlockEntry<WallSignBlock>> tuple_peach = ECBlocks.TO_SIGN.get(ECBlocks.Plant.PEACH_PLANKS.getId());
        Tuple<ECBlocks.BlockEntry<StandingSignBlock>, ECBlocks.BlockEntry<WallSignBlock>> tuple_purpuraceus = ECBlocks.TO_SIGN.get(ECBlocks.Plant.PURPURACEUS_PLANKS.getId());
        signValidBlocks.add(tuple_ginkgo.getA().get());
        signValidBlocks.add(tuple_ginkgo.getB().get());
        signValidBlocks.add(tuple_palm.getA().get());
        signValidBlocks.add(tuple_palm.getB().get());
        signValidBlocks.add(tuple_peach.getA().get());
        signValidBlocks.add(tuple_peach.getB().get());
        signValidBlocks.add(tuple_purpuraceus.getA().get());
        signValidBlocks.add(tuple_purpuraceus.getB().get());

        signBuilderAccess.ec_setValidBlocks(signValidBlocks);
    }
}
