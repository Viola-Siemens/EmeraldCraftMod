package com.hexagram2021.emeraldcraft;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.api.tradable.TradeListingUtils;
import com.hexagram2021.emeraldcraft.client.ClientProxy;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.ECSaveData;
import com.hexagram2021.emeraldcraft.common.ModVanillaCompat;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.mixin.BlockEntityTypeAccess;
import com.hexagram2021.emeraldcraft.network.ClientboundTradeSyncPacket;
import com.hexagram2021.emeraldcraft.network.IECPacket;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(EmeraldCraft.MODID)
public class EmeraldCraft {
	public static final String MODID = "emeraldcraft";
	public static final String MODNAME = "Emerald Craft";
	public static final String VERSION = ModList.get().getModFileById(MODID).versionString();

	public static final CommonProxy proxy = DistExecutor.safeRunForDist(
			bootstrapErrorToXCPInDev(() -> ClientProxy::new),
			bootstrapErrorToXCPInDev(() -> CommonProxy::new)
	);

	public final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder
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

	private int messageId = 0;
	@SuppressWarnings("SameParameterValue")
	private <T extends IECPacket> void registerMessage(Class<T> packetType,
													   Function<FriendlyByteBuf, T> constructor,
													   NetworkDirection direction) {
		this.packetHandler.registerMessage(this.messageId++, packetType, IECPacket::write, constructor, (packet, ctx) -> packet.handle(), Optional.of(direction));
	}

	public EmeraldCraft() {
		ECLogger.logger = LogManager.getLogger(MODID);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.addListener(this::tagsUpdated);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
		MinecraftForge.EVENT_BUS.addListener(this::datapackSync);
		DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
		Consumer<Runnable> runLater = job -> queue.enqueueWork(
				ModLoadingContext.get().getActiveContainer(), job
		);
		ECContent.modConstruction(bus, runLater);
		DistExecutor.safeRunWhenOn(Dist.CLIENT, bootstrapErrorToXCPInDev(() -> ClientProxy::modConstruction));

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECCommonConfig.SPEC);

		bus.addListener(this::setup);
		bus.addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.register(this);
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
			Villages.init();
			ECContent.init();
			ModVanillaCompat.setup();
		});

		TradeListingUtils.registerTradeListing(VillagerTrades.WANDERING_TRADER_TRADES, EntityType.WANDERING_TRADER, null);
		TradeListingUtils.registerTradeListing(ECTrades.PIGLIN_CUTEY_TRADES, ECEntities.PIGLIN_CUTEY, null);
		TradeListingUtils.registerTradeListing(ECTrades.NETHER_LAMBMAN_TRADES, ECEntities.NETHER_LAMBMAN, null);
		TradeListingUtils.registerTradeListing(ECTrades.NETHER_PIGMAN_TRADES, ECEntities.NETHER_PIGMAN, null);

		registerMessage(ClientboundTradeSyncPacket.class, ClientboundTradeSyncPacket::new, NetworkDirection.PLAY_TO_CLIENT);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		if(ModList.get().isLoaded("diet")) {
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
			ECSaveData worldData = world.getDataStorage().computeIfAbsent(ECSaveData::new, ECSaveData::new, ECSaveData.dataName);
			ECSaveData.setInstance(worldData);
		}
	}

	public void datapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		IECPacket packet = new ClientboundTradeSyncPacket(TradeShadowRecipe.getTradeRecipes(event.getPlayerList().getServer().overworld()));
		if(player == null) {
			this.packetHandler.send(PacketDistributor.ALL.noArg(), packet);
		} else {
			this.packetHandler.send(PacketDistributor.PLAYER.with(() -> player), packet);
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
