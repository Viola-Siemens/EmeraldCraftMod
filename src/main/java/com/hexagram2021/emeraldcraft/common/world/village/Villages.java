package com.hexagram2021.emeraldcraft.common.world.village;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.mixin.HeroGiftsTaskAccess;
import com.hexagram2021.emeraldcraft.mixin.StructureTemplatePoolAccess;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class Villages {
	public static final ResourceLocation CARPENTER = new ResourceLocation(MODID, "carpenter");
	public static final ResourceLocation GLAZIER = new ResourceLocation(MODID, "glazier");
	public static final ResourceLocation MINER = new ResourceLocation(MODID, "miner");
	public static final ResourceLocation ASTROLOGIST = new ResourceLocation(MODID, "astrologist");
	public static final ResourceLocation GROWER = new ResourceLocation(MODID, "grower");
	public static final ResourceLocation BEEKEEPER = new ResourceLocation(MODID, "beekeeper");
	public static final ResourceLocation GEOLOGIST = new ResourceLocation(MODID, "geologist");
	public static final ResourceLocation ICER = new ResourceLocation(MODID, "icer");
	public static final ResourceLocation CHEMICAL_ENGINEER = new ResourceLocation(MODID, "chemical_engineer");
	public static final ResourceLocation PAPERHANGER = new ResourceLocation(MODID, "paperhanger");

	public static void init() {
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_CARPENTER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/carpenter_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GLAZIER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/glazier_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_MINER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/miner_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_ASTROLOGIST.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/astrologist_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GROWER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/grower_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_BEEKEEPER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/beekeeper_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GEOLOGIST.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/geologist_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_ICER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/icer_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_CHEMICAL_ENGINEER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/chemical_engineer_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_PAPERHANGER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/paperhanger_gift"));
	}

	public static void addAllStructuresToPool(RegistryAccess registryAccess) {
		addToPool(new ResourceLocation("village/plains/houses"), new ResourceLocation(MODID, "village/plains/houses/plains_beekeeper_1"), 4, registryAccess);
		addToPool(new ResourceLocation("village/plains/houses"), new ResourceLocation(MODID, "village/plains/houses/plains_carpenter_1"), 4, registryAccess);
		addToPool(new ResourceLocation("village/plains/houses"), new ResourceLocation(MODID, "village/plains/houses/plains_paperhanger_1"), 2, registryAccess);
		addToPool(new ResourceLocation("village/snowy/houses"), new ResourceLocation(MODID, "village/snowy/houses/snowy_astrologist_1"), 3, registryAccess);
		addToPool(new ResourceLocation("village/snowy/houses"), new ResourceLocation(MODID, "village/snowy/houses/snowy_icer_1"), 4, registryAccess);
	}

	private static void addToPool(ResourceLocation poolName, ResourceLocation toAdd, int weight, RegistryAccess registryAccess) {
		Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
		StructureTemplatePool structureTemplatePool = registry.get(poolName);
		if(structureTemplatePool == null) {
			ECLogger.error("Ignored empty structure template pool: " + poolName);
			return;
		}
		StructureTemplatePoolAccess pool = (StructureTemplatePoolAccess)structureTemplatePool;
		List<Pair<StructurePoolElement, Integer>> rawTemplates = pool.getRawTemplates() instanceof ArrayList ?
				pool.getRawTemplates() : new ArrayList<>(pool.getRawTemplates());

		SinglePoolElement addedElement = SinglePoolElement.single(toAdd.toString()).apply(StructureTemplatePool.Projection.RIGID);
		rawTemplates.add(Pair.of(addedElement, weight));
		pool.getTemplates().add(addedElement);

		pool.setRawTemplates(rawTemplates);
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registers {
		public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister.create(ForgeRegistries.POI_TYPES, MODID);
		public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MODID);

		public static final RegistryObject<PoiType> POI_CARPENTRY_TABLE = POINTS_OF_INTEREST.register(
				"carpentry_table", () -> createPOI(assembleStates(ECBlocks.WorkStation.CARPENTRY_TABLE.get()))
		);
		public static final RegistryObject<PoiType> POI_GLASS_KILN = POINTS_OF_INTEREST.register(
				"glass_kiln", () -> createPOI(assembleStates(ECBlocks.WorkStation.GLASS_KILN.get()))
		);
		public static final RegistryObject<PoiType> POI_MINERAL_TABLE = POINTS_OF_INTEREST.register(
				"mineral_table", () -> createPOI(assembleStates(ECBlocks.WorkStation.MINERAL_TABLE.get()))
		);
		public static final RegistryObject<PoiType> POI_CRYSTALBALL_TABLE = POINTS_OF_INTEREST.register(
				"crystalball_table", () -> createPOI(assembleStates(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get()))
		);
		public static final RegistryObject<PoiType> POI_FLOWER_POT = POINTS_OF_INTEREST.register(
				"flower_pot", () -> createPOI(assembleStates(Blocks.FLOWER_POT))
		);
		public static final RegistryObject<PoiType> POI_SQUEEZER = POINTS_OF_INTEREST.register(
				"squeezer", () -> createPOI(assembleStates(ECBlocks.WorkStation.SQUEEZER.get()))
		);
		public static final RegistryObject<PoiType> POI_CONTINUOUS_MINER = POINTS_OF_INTEREST.register(
				"continuous_miner", () -> createPOI(assembleStates(ECBlocks.WorkStation.CONTINUOUS_MINER.get()))
		);
		public static final RegistryObject<PoiType> POI_ICE_MAKER = POINTS_OF_INTEREST.register(
				"ice_maker", () -> createPOI(assembleStates(ECBlocks.WorkStation.ICE_MAKER.get()))
		);
		public static final RegistryObject<PoiType> POI_MELTER = POINTS_OF_INTEREST.register(
				"melter", () -> createPOI(assembleStates(ECBlocks.WorkStation.MELTER.get()))
		);
		public static final RegistryObject<PoiType> POI_RABBLE_FURNACE = POINTS_OF_INTEREST.register(
				"rabble_furnace", () -> createPOI(assembleStates(ECBlocks.WorkStation.RABBLE_FURNACE.get()))
		);

		public static final RegistryObject<VillagerProfession> PROF_CARPENTER = PROFESSIONS.register(
				CARPENTER.getPath(), () -> createProf(CARPENTER, POI_CARPENTRY_TABLE::getKey, ECSounds.VILLAGER_WORK_CARPENTER)
		);
		public static final RegistryObject<VillagerProfession> PROF_GLAZIER = PROFESSIONS.register(
				GLAZIER.getPath(), () -> createProf(GLAZIER, POI_GLASS_KILN::getKey, ECSounds.VILLAGER_WORK_GLAZIER)
		);
		public static final RegistryObject<VillagerProfession> PROF_MINER = PROFESSIONS.register(
				MINER.getPath(), () -> createProf(MINER, POI_MINERAL_TABLE::getKey, ECSounds.VILLAGER_WORK_MINER)
		);
		public static final RegistryObject<VillagerProfession> PROF_ASTROLOGIST = PROFESSIONS.register(
				ASTROLOGIST.getPath(), () -> createProf(ASTROLOGIST, POI_CRYSTALBALL_TABLE::getKey, ECSounds.VILLAGER_WORK_ASTROLOGIST)
		);
		public static final RegistryObject<VillagerProfession> PROF_GROWER = PROFESSIONS.register(
				GROWER.getPath(), () -> createProf(GROWER, POI_FLOWER_POT::getKey, ECSounds.VILLAGER_WORK_GROWER)
		);
		public static final RegistryObject<VillagerProfession> PROF_BEEKEEPER = PROFESSIONS.register(
				BEEKEEPER.getPath(), () -> createProf(GROWER, POI_SQUEEZER::getKey, ECSounds.VILLAGER_WORK_BEEKEEPER)
		);
		public static final RegistryObject<VillagerProfession> PROF_GEOLOGIST = PROFESSIONS.register(
				GEOLOGIST.getPath(), () -> createProf(GEOLOGIST, POI_CONTINUOUS_MINER::getKey, ECSounds.VILLAGER_WORK_GEOLOGIST)
		);
		public static final RegistryObject<VillagerProfession> PROF_ICER = PROFESSIONS.register(
				ICER.getPath(), () -> createProf(ICER, POI_ICE_MAKER::getKey, ECSounds.VILLAGER_WORK_ICER)
		);
		public static final RegistryObject<VillagerProfession> PROF_CHEMICAL_ENGINEER = PROFESSIONS.register(
				CHEMICAL_ENGINEER.getPath(), () -> createProf(CHEMICAL_ENGINEER, POI_MELTER::getKey, ECSounds.VILLAGER_WORK_CHEMICAL_ENGINEER)
		);
		public static final RegistryObject<VillagerProfession> PROF_PAPERHANGER = PROFESSIONS.register(
				PAPERHANGER.getPath(), () -> createProf(PAPERHANGER, POI_RABBLE_FURNACE::getKey, ECSounds.VILLAGER_WORK_PAPERHANGER)
		);

		private static Collection<BlockState> assembleStates(Block block) {
			return block.getStateDefinition().getPossibleStates();
		}

		private static PoiType createPOI(Collection<BlockState> block) {
			return new PoiType(ImmutableSet.copyOf(block), 1, 1);
		}

		private static VillagerProfession createProf(ResourceLocation name, Supplier<ResourceKey<PoiType>> poi, SoundEvent sound) {
			ResourceKey<PoiType> poiName = poi.get();
			return new VillagerProfession(
					name.toString(),
					(p) -> p.is(poiName),
					(p) -> p.is(poiName),
					ImmutableSet.of(),
					ImmutableSet.of(),
					sound
			);
		}
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class Events {
		@SubscribeEvent
		public static void registerTrades(VillagerTradesEvent event) {
			Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

			ResourceLocation currentVillagerProfession = getRegistryName(event.getType());
			if(CARPENTER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.STICK, 32, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldsForVillagerTypeItem(8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL,
						ImmutableMap.<VillagerType, Item>builder()
								.put(VillagerType.PLAINS, Items.OAK_SAPLING)
								.put(VillagerType.TAIGA, Items.SPRUCE_SAPLING)
								.put(VillagerType.SNOW, Items.SPRUCE_SAPLING)
								.put(VillagerType.DESERT, Items.JUNGLE_SAPLING)
								.put(VillagerType.JUNGLE, Items.JUNGLE_SAPLING)
								.put(VillagerType.SAVANNA, Items.ACACIA_SAPLING)
								.put(VillagerType.SWAMP, Items.DARK_OAK_SAPLING)
								.build(),
						Items.OAK_SAPLING
				));
				trades.get(1).add(new ECTrades.VillagerTypeItemForEmeralds(2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY,
						ImmutableMap.<VillagerType, Item>builder()
								.put(VillagerType.PLAINS, Items.OAK_LOG)
								.put(VillagerType.TAIGA, Items.SPRUCE_LOG)
								.put(VillagerType.SNOW, Items.SPRUCE_LOG)
								.put(VillagerType.DESERT, Items.JUNGLE_LOG)
								.put(VillagerType.JUNGLE, Items.JUNGLE_LOG)
								.put(VillagerType.SAVANNA, Items.ACACIA_LOG)
								.put(VillagerType.SWAMP, Items.DARK_OAK_LOG)
								.build(),
						Items.OAK_LOG
				));
				trades.get(2).add(new ECTrades.ItemsAndEmeraldsToItems(Items.NETHER_WART_BLOCK, 1, 4, Items.NETHER_WART, 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.IRON_AXE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.BOOKSHELF, 1, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.CRAFTING_TABLE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsAndEmeraldsToItems(Items.WARPED_WART_BLOCK, 1, 4, ECItems.WARPED_WART.get(), 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BOWL, 1, 6, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.CHEST, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.BEEHIVE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.JUKEBOX, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EmeraldsForVillagerTypeItem(4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_BUY,
						ImmutableMap.<VillagerType, Item>builder()
								.put(VillagerType.PLAINS, Items.OAK_SIGN)
								.put(VillagerType.TAIGA, Items.SPRUCE_SIGN)
								.put(VillagerType.SNOW, Items.SPRUCE_SIGN)
								.put(VillagerType.DESERT, Items.JUNGLE_SIGN)
								.put(VillagerType.JUNGLE, Items.JUNGLE_SIGN)
								.put(VillagerType.SAVANNA, Items.ACACIA_SIGN)
								.put(VillagerType.SWAMP, Items.DARK_OAK_SIGN)
								.build(),
						Items.OAK_SIGN
				));
				trades.get(5).add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.NOTE_BLOCK, 4, 4, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsAndEmeraldsToItems(Items.SHULKER_SHELL, 1, 12, Items.SHULKER_BOX, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GLAZIER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.CLAY_BALL, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.SAND, 12, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.GLASS_BOTTLE, 1, 6, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.TINTED_GLASS, 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.WHITE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.ORANGE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.MAGENTA_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.LIGHT_BLUE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.YELLOW_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.LIME_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.PINK_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.GRAY_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.LIGHT_GRAY_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.CYAN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.PURPLE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BLUE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BROWN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.GREEN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.RED_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BLACK_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.DAYLIGHT_DETECTOR, 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.TERRACOTTA, 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.ENDER_EYE, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.END_CRYSTAL, 11, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(MINER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.RAW_COPPER, 14, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.RAW_IRON, 10, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.RAW_GOLD, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.MINECART, 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.REDSTONE, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.QUARTZ, 8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.POINTED_DRIPSTONE, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsAndEmeraldsToItems(Items.AMETHYST_BLOCK, 1, 4, Items.AMETHYST_SHARD, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL_BLOCK, 2, 32, Items.DIAMOND, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.NETHERITE_SCRAP, 1, 10, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(ASTROLOGIST.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.SPYGLASS, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.BLAZE_POWDER, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.NETHER_SPROUTS, 10, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.SOUL_SAND, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.CHORUS_FRUIT, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.SPIDER_EYE, 13, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.DRAGON_BREATH, 9, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 6, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL, 4, 56, Items.WITHER_SKELETON_SKULL, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.SHULKER_SHELL, 12, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.TraderHeadForEmeralds(40, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.CARVED_PUMPKIN, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.DRAGON_HEAD, 36, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.SOUL_SAND, 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.WEEPING_VINES, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.TWISTING_VINES, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GROWER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.FLOWER_POT, 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.CYAN_PETUNIA, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.MAGENTA_PETUNIA, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.POPPY, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.HIGAN_BANA, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.PINK_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.RED_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 6, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.BLUE_ORCHID, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.ALLIUM, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.SUNFLOWER, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.LILAC, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.ROSE_BUSH, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.PEONY, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(MobEffects.FIRE_RESISTANCE, 100, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(MobEffects.WITHER, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(MobEffects.WATER_BREATHING, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(MobEffects.REGENERATION, 120, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(BEEKEEPER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.HONEYCOMB, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.BEEHIVE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.HONEY_BOTTLE, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.HONEY_BLOCK, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.ORANGE_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.PINK_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.RED_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.WHITE_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.HONEYCOMB_BLOCK, 1, 1, Items.HONEYCOMB, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.DANDELION, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.BEE, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GEOLOGIST.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.NETHERRACK, 24, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.DEEPSLATE, 18, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 2, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.OBSIDIAN, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.LAPIS_LAZULI, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(ECItems.IRON_CONCENTRATE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.EmeraldForItems(ECItems.GOLD_CONCENTRATE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.EmeraldForItems(ECItems.COPPER_CONCENTRATE, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.CALCITE, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.SMOOTH_BASALT, 14, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.BLACKSTONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(ICER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.PACKED_ICE, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(Items.ICE, 1, 5, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.POWDER_SNOW_BUCKET, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.SNOW_BLOCK, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.BLUE_ICE, 3, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(Items.LANTERN, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.OBSIDIAN, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EnchantedItemForEmeralds(Items.LEATHER_BOOTS, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsAndEmeraldsToItems(Items.SNOWBALL, 4, 1, Items.SNOW, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.SNOW, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(CHEMICAL_ENGINEER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(ECItems.MELTED_EMERALD_BUCKET, 12, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BUCKET, 4, 3, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(ECItems.MELTED_IRON_BUCKET, 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(ECItems.MELTED_COPPER_BUCKET, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.NetheriteScrapForItems(Items.EMERALD_BLOCK, 10, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.FLINT, 4, 2, Items.GUNPOWDER, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(ECItems.MELTED_GOLD_BUCKET, 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.BOTTLE.item(), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.POTION.item(), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(PAPERHANGER.equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.PAPER, 24, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(ECBlocks.Decoration.RESIN_BLOCK, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(ECItems.WINDOW_FILM, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.EmeraldForItems(ECBlocks.Decoration.REINFORCED_RESIN_BLOCK, 1, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(ECBlocks.Decoration.PAPER_BLOCK, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldsForVillagerTypeItem(15, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY,
						ImmutableMap.<VillagerType, Item>builder()
								.put(VillagerType.PLAINS, Items.OAK_PLANKS)
								.put(VillagerType.TAIGA, Items.SPRUCE_PLANKS)
								.put(VillagerType.SNOW, Items.SPRUCE_PLANKS)
								.put(VillagerType.DESERT, Items.JUNGLE_PLANKS)
								.put(VillagerType.JUNGLE, Items.JUNGLE_PLANKS)
								.put(VillagerType.SAVANNA, Items.ACACIA_PLANKS)
								.put(VillagerType.SWAMP, Items.DARK_OAK_PLANKS)
								.build(),
						Items.OAK_PLANKS
				));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(Items.PAINTING, 2, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.GLOW_INK_SAC, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(Items.COBWEB, 6, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(new ResourceLocation(VillagerProfession.FARMER.name()).equals(currentVillagerProfession)) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(ECItems.CHILI_SEED, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(ECItems.PEACH, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
			} else if(new ResourceLocation(VillagerProfession.CARTOGRAPHER.name()).equals(currentVillagerProfession)) {
				trades.get(5).add(new ECTrades.NetherStructureMapForEmeralds(12, 2, ECStructureTags.ON_SAR_EXPLORER_MAPS, "filled_map.shelter", ECMapDecorationTypes.SHELTER, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.NetherStructureMapForEmeralds(14, 2, ECStructureTags.ON_GEOCENTER_EXPLORER_MAPS, "filled_map.entrenchment", ECMapDecorationTypes.ENTRENCHMENT, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			}
		}


		@SubscribeEvent
		public static void registerWandererTrades(WandererTradesEvent event) {
			List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
			List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();
			genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.CHILI, 3, 1, 5, ECTrades.XP_LEVEL_1_SELL));
			genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.PEACH, 4, 1, 5, ECTrades.XP_LEVEL_1_SELL));
			genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.GINKGO_NUT, 2, 1, 4, ECTrades.XP_LEVEL_1_SELL));
			rareTrades.add(new ECTrades.ItemsForEmeralds(ECItems.GLUTEN, 3, 1, 4, ECTrades.XP_LEVEL_1_SELL));
		}
	}
}
