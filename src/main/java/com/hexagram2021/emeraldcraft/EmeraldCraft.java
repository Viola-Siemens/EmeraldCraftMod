package com.hexagram2021.emeraldcraft;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.api.tradable.TradeListingUtils;
import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.ECSaveData;
import com.hexagram2021.emeraldcraft.common.ModVanillaCompat;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.mixin.accessor.BlockEntityTypeAccess;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.ClientboundTradeSyncPacket;
import com.hexagram2021.emeraldcraft.network.IECPacket;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.MessageFunctions;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(EmeraldCraft.MODID)
public class EmeraldCraft {
	public static final String MODID = "emeraldcraft";
	public static final String MODNAME = "Emerald Craft";
	public static final String VERSION = ModList.get().getModFileById(MODID).versionString();

	public static final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MODID, "main"))
			.networkProtocolVersion(() -> VERSION)
			.serverAcceptedVersions(VERSION::equals)
			.clientAcceptedVersions(VERSION::equals)
			.simpleChannel();

	public static <T>
	Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in) {
		if(FMLLoader.isProduction()) {
			return in;
		}
		return () -> {
			try {
				return in.get();
			} catch(BootstrapMethodError e) {
				throw new RuntimeException(e);
			}
		};
	}

	private static int messageId = 0;
	@SuppressWarnings("SameParameterValue")
	private static <T extends IECPacket> void registerMessage(Class<T> packetType,
															  MessageFunctions.MessageDecoder<T> constructor,
															  PlayNetworkDirection direction) {
		packetHandler.messageBuilder(packetType, messageId++, direction)
				.decoder(constructor)
				.encoder(IECPacket::write)
				.consumerMainThread((packet, ctx) -> packet.handle())
				.add();
	}

	public static void sendMessageToPlayer(IECPacket packet, ServerPlayer serverPlayer) {
		packetHandler.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
	}

	public EmeraldCraft() {
		NeoForgeMod.enableMilkFluid();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		NeoForge.EVENT_BUS.addListener(this::tagsUpdated);
		NeoForge.EVENT_BUS.addListener(this::serverStarted);
		NeoForge.EVENT_BUS.addListener(this::datapackSync);
		DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
		Consumer<Runnable> runLater = job -> queue.enqueueWork(
				ModLoadingContext.get().getActiveContainer(), job
		);
		ECContent.modConstruction(bus, runLater);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECCommonConfig.SPEC);

		bus.addListener(this::setup);
		bus.addListener(this::enqueueIMC);
	}

	public void setup(FMLCommonSetupEvent event) {
		ECTriggers.init();
		ECBrewingRecipes.init();

		event.enqueueWork(() -> {
			VillagerType.BY_BIOME.putAll(ImmutableMap.of(
					ECBiomeKeys.AZURE_DESERT.key(), VillagerType.DESERT,
					ECBiomeKeys.JADEITE_DESERT.key(), VillagerType.DESERT,
					ECBiomeKeys.XANADU.key(), VillagerType.SWAMP
			));
			appendBlocksToBlockEntities();
			ECContent.init();
			ModVanillaCompat.setup();
		});

		TradeListingUtils.registerTradeListing(VillagerTrades.WANDERING_TRADER_TRADES, EntityType.WANDERING_TRADER, null);
		TradeListingUtils.registerTradeListing(ECTrades.PIGLIN_CUTEY_TRADES, ECEntities.PIGLIN_CUTEY, null);
		TradeListingUtils.registerTradeListing(ECTrades.NETHER_LAMBMAN_TRADES, ECEntities.NETHER_LAMBMAN, null);
		TradeListingUtils.registerTradeListing(ECTrades.NETHER_PIGMAN_TRADES, ECEntities.NETHER_PIGMAN, null);

		registerMessage(ClientboundTradeSyncPacket.class, ClientboundTradeSyncPacket::new, PlayNetworkDirection.PLAY_TO_CLIENT);
		registerMessage(ClientboundFluidSyncPacket.class, ClientboundFluidSyncPacket::new, PlayNetworkDirection.PLAY_TO_CLIENT);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		//Do we still need this?
	}

	public void tagsUpdated(TagsUpdatedEvent event) {
		if(event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
			return;
		}

		ECStructures.init(event.getRegistryAccess());
		Villages.addAllStructuresToPool(event.getRegistryAccess());
	}

	public void serverStarted(ServerStartedEvent event) {
		ServerLevel world = event.getServer().getLevel(Level.OVERWORLD);
		assert world != null;
		if(!world.isClientSide) {
			ECSaveData worldData = world.getDataStorage().computeIfAbsent(ECSaveData.factory(), ECSaveData.dataName);
			ECSaveData.setInstance(worldData);
		}
		// // Run these checks when debugging:
		// com.hexagram2021.emeraldcraft.common.util.RegistryChecker.registryCheck(event.getServer().getLootData());
		// com.hexagram2021.emeraldcraft.common.util.RegistryChecker.recipeCheck(event.getServer().getLootData(), event.getServer().getRecipeManager(), event.getServer().registryAccess());
	}

	public void datapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		IECPacket packet = new ClientboundTradeSyncPacket(TradeShadowRecipe.getAllJobsites(), TradeShadowRecipe.getTradeRecipes(event.getPlayerList().getServer().overworld()));
		if(player == null) {
			packetHandler.send(PacketDistributor.ALL.noArg(), packet);
		} else {
			packetHandler.send(PacketDistributor.PLAYER.with(() -> player), packet);
		}
	}

	private static void appendBlocksToBlockEntities() {
		BlockEntityTypeAccess signBuilderAccess = (BlockEntityTypeAccess)BlockEntityType.SIGN;
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
