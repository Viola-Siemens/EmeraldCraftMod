package com.hexagram2021.emeraldcraft;

import com.hexagram2021.emeraldcraft.client.ClientProxy;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.ECSaveData;
import com.hexagram2021.emeraldcraft.common.crafting.RecipeCachingReloadListener;
import com.hexagram2021.emeraldcraft.common.crafting.RecipeReloadListener;
import com.hexagram2021.emeraldcraft.common.register.ECBiomes;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.world.ECWorldGen;
import com.hexagram2021.emeraldcraft.common.world.Villages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(EmeraldCraft.MODID)
public class EmeraldCraft {
	public static final String MODID = "emeraldcraft";
	public static final String MODNAME = "Emerald Craft";
	public static final String VERSION = "${version}";

	public static final CommonProxy proxy = DistExecutor.safeRunForDist(bootstrapErrorToXCPInDev(() -> ClientProxy::new), bootstrapErrorToXCPInDev(() -> CommonProxy::new));

	public static final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MODID, "main"))
			.networkProtocolVersion(() -> VERSION)
			.serverAcceptedVersions(VERSION::equals)
			.clientAcceptedVersions(VERSION::equals)
			.simpleChannel();

	// Complete hack: DistExecutor::safeRunForDist intentionally tries to access the "wrong" supplier in dev, which
	// throws an error (rather than an exception) on J16 due to trying to load a client-only class. So we need to
	// replace the error with an exception in dev.
	//*
	public static <T>
	Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in)
	{
		if(FMLLoader.isProduction())
			return in;
		return () -> {
			try
			{
				return in.get();
			} catch(BootstrapMethodError e)
			{
				throw new RuntimeException(e);
			}
		};
	}
	//*/

	public EmeraldCraft() {
		ECLogger.logger = LogManager.getLogger(MODID);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
	//	bus.addListener(this::enqueueIMCs);
	//	MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
		MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::addReloadListenersLowest);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
	//	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECCommonConfig.CONFIG_SPEC.getBaseSpec());
	//	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECClientConfig.CONFIG_SPEC.getBaseSpec());
	//	ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ECServerConfig.CONFIG_SPEC.getBaseSpec());
		DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
		Consumer<Runnable> runLater = job -> queue.enqueueWork(
				ModLoadingContext.get().getActiveContainer(), job
		);
		ECContent.modConstruction(bus, runLater);
		DistExecutor.safeRunWhenOn(Dist.CLIENT, bootstrapErrorToXCPInDev(() -> ClientProxy::modConstruction));

		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECWorldGen::biomeModification);
		runLater.accept(ECRecipes::registerRecipeTypes);
		bus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void setup(FMLCommonSetupEvent event) {
		ECContent.init(event);
		event.enqueueWork(Villages::init);
		event.enqueueWork(ECBiomes::registerBiomes);
	}

	public void addReloadListeners(AddReloadListenerEvent event) {
		ServerResources dataPackRegistries = event.getDataPackRegistries();
		event.addListener(new RecipeReloadListener(dataPackRegistries));
	}

	public void addReloadListenersLowest(AddReloadListenerEvent event) {
		event.addListener(new RecipeCachingReloadListener(event.getDataPackRegistries()));
	}

	public void serverStarted(FMLServerStartedEvent event) {
		ServerLevel world = event.getServer().getLevel(Level.OVERWORLD);
		if(!world.isClientSide) {
			ECSaveData worldData = world.getDataStorage().computeIfAbsent(ECSaveData::new, ECSaveData::new, ECSaveData.dataName);
			ECSaveData.setInstance(worldData);
		}
	}

	public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MODID) {
		@Override
		@Nonnull
		public ItemStack makeIcon() {
			return new ItemStack(ECBlocks.TO_STAIRS.get(new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "emerald_block")));
		}
	};
}
