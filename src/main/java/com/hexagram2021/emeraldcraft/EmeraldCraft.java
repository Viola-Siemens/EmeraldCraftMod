package com.hexagram2021.emeraldcraft;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.client.ClientProxy;
import com.hexagram2021.emeraldcraft.common.CommonProxy;
import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.ECSaveData;
import com.hexagram2021.emeraldcraft.common.ModVanillaCompat;
import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.world.ECWorldGen;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import com.hexagram2021.emeraldcraft.mixin.BlockEntityTypeAccess;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(EmeraldCraft.MODID)
public class EmeraldCraft {
	public static final String MODID = "emeraldcraft";
	public static final String MODNAME = "Emerald Craft";
	public static final String VERSION = "${version}";

	public static final CommonProxy proxy = DistExecutor.safeRunForDist(
			bootstrapErrorToXCPInDev(() -> ClientProxy::new),
			bootstrapErrorToXCPInDev(() -> CommonProxy::new)
	);

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

	public EmeraldCraft() {
		ECLogger.logger = LogManager.getLogger(MODID);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
		DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
		Consumer<Runnable> runLater = job -> queue.enqueueWork(
				ModLoadingContext.get().getActiveContainer(), job
		);
		ECContent.modConstruction(bus, runLater);
		DistExecutor.safeRunWhenOn(Dist.CLIENT, bootstrapErrorToXCPInDev(() -> ClientProxy::modConstruction));

		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECWorldGen::biomeModification);
		bus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void setup(FMLCommonSetupEvent event) {
		ECTriggers.init();

		event.enqueueWork(() -> {
			VillagerType.BY_BIOME.putAll(ImmutableMap.of(
					ECBiomeKeys.AZURE_DESERT, VillagerType.DESERT,
					ECBiomeKeys.JADEITE_DESERT, VillagerType.DESERT,
					ECBiomeKeys.XANADU, VillagerType.SWAMP
			));
			appendBlocksToBlockEntities();
			Villages.init();
			ECContent.init();
			ModVanillaCompat.setup();

			ECRecipeBookTypes.init();
		});
	}

	public void serverStarted(ServerStartedEvent event) {
		ServerLevel world = event.getServer().getLevel(Level.OVERWORLD);
		assert world != null;
		if(!world.isClientSide) {
			ECSaveData worldData = world.getDataStorage().computeIfAbsent(ECSaveData::new, ECSaveData::new, ECSaveData.dataName);
			ECSaveData.setInstance(worldData);
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

	public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MODID) {
		@Override
		@Nonnull
		public ItemStack makeIcon() {
			return new ItemStack(ECBlocks.TO_STAIRS.get(new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "emerald_block")));
		}
	};
}
