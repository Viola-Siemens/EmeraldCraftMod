package com.hexagram2021.emeraldcraft.common.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.mixin.HeroGiftsTaskAccess;
import com.hexagram2021.emeraldcraft.mixin.SingleJigsawAccess;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.PlainsVillagePools;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

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

	public static void init() {
		PlainsVillagePools.bootstrap();
		addToPool(new ResourceLocation("village/plains/houses"), new ResourceLocation(MODID, "village/plains/houses/plains_carpentry_house_1"), 5);

		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_CARPENTER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/carpenter_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GLAZIER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/glazier_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_MINER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/miner_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_ASTROLOGIST.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/astrologist_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GROWER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/grower_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_BEEKEEPER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/beekeeper_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_GEOLOGIST.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/geologist_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_ICER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/icer_gift"));
		HeroGiftsTaskAccess.getGifts().put(Registers.PROF_CHEMICAL_ENGINEER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/chemical_engineer_gift"));
	}

	private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
		JigsawPattern old = WorldGenRegistries.TEMPLATE_POOL.get(pool);
		int id = WorldGenRegistries.TEMPLATE_POOL.getId(old);

		List<JigsawPiece> shuffled;
		if(old != null) {
			shuffled = old.getShuffledTemplates(new Random(0));
		} else {
			shuffled = ImmutableList.of();
		}
		Object2IntMap<JigsawPiece> newPieces = new Object2IntLinkedOpenHashMap<>();
		for(JigsawPiece p : shuffled)
			newPieces.computeInt(p, (JigsawPiece pTemp, Integer i) -> (i==null ? 0 : i) + 1);
		newPieces.put(SingleJigsawAccess.construct(
				Either.left(toAdd), () -> ProcessorLists.EMPTY, JigsawPattern.PlacementBehaviour.RIGID
		), weight);
		List<Pair<JigsawPiece, Integer>> newPieceList = newPieces.object2IntEntrySet().stream()
				.map(e -> Pair.of(e.getKey(), e.getIntValue()))
				.collect(Collectors.toList());

		ResourceLocation name = old.getName();
		Registry.register(WorldGenRegistries.TEMPLATE_POOL, pool, new JigsawPattern(pool, name, newPieceList));
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registers {
		public static final DeferredRegister<PointOfInterestType> POINTS_OF_INTEREST = DeferredRegister.create(ForgeRegistries.POI_TYPES, MODID);
		public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, MODID);

		public static final RegistryObject<PointOfInterestType> POI_CARPENTRY_TABLE = POINTS_OF_INTEREST.register(
				"carpentry_table", () -> createPOI("carpentry_table", assembleStates(ECBlocks.WorkStation.CARPENTRY_TABLE.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_GLASS_KILN = POINTS_OF_INTEREST.register(
				"glass_kiln", () -> createPOI("glass_kiln", assembleStates(ECBlocks.WorkStation.GLASS_KILN.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_MINERAL_TABLE = POINTS_OF_INTEREST.register(
				"mineral_table", () -> createPOI("mineral_table", assembleStates(ECBlocks.WorkStation.MINERAL_TABLE.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_CRYSTALBALL_TABLE = POINTS_OF_INTEREST.register(
				"crystalball_table", () -> createPOI("crystalball_table", assembleStates(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_FLOWER_POT = POINTS_OF_INTEREST.register(
				"flower_pot", () -> createPOI("flower_pot", assembleStates(Blocks.FLOWER_POT))
		);
		public static final RegistryObject<PointOfInterestType> POI_SQUEEZER = POINTS_OF_INTEREST.register(
				"squeezer", () -> createPOI("squeezer", assembleStates(ECBlocks.WorkStation.SQUEEZER.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_CONTINUOUS_MINER = POINTS_OF_INTEREST.register(
				"continuous_miner", () -> createPOI("continuous_miner", assembleStates(ECBlocks.WorkStation.CONTINUOUS_MINER.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_ICE_MAKER = POINTS_OF_INTEREST.register(
				"ice_maker", () -> createPOI("ice_maker", assembleStates(ECBlocks.WorkStation.ICE_MAKER.get()))
		);
		public static final RegistryObject<PointOfInterestType> POI_MELTER = POINTS_OF_INTEREST.register(
				"melter", () -> createPOI("melter", assembleStates(ECBlocks.WorkStation.MELTER.get()))
		);

		public static final RegistryObject<VillagerProfession> PROF_CARPENTER = PROFESSIONS.register(
				CARPENTER.getPath(), () -> createProf(CARPENTER, POI_CARPENTRY_TABLE.get(), ECSounds.VILLAGER_WORK_CARPENTER)
		);
		public static final RegistryObject<VillagerProfession> PROF_GLAZIER = PROFESSIONS.register(
				GLAZIER.getPath(), () -> createProf(GLAZIER, POI_GLASS_KILN.get(), ECSounds.VILLAGER_WORK_GLAZIER)
		);
		public static final RegistryObject<VillagerProfession> PROF_MINER = PROFESSIONS.register(
				MINER.getPath(), () -> createProf(MINER, POI_MINERAL_TABLE.get(), ECSounds.VILLAGER_WORK_MINER)
		);
		public static final RegistryObject<VillagerProfession> PROF_ASTROLOGIST = PROFESSIONS.register(
				ASTROLOGIST.getPath(), () -> createProf(ASTROLOGIST, POI_CRYSTALBALL_TABLE.get(), ECSounds.VILLAGER_WORK_ASTROLOGIST)
		);
		public static final RegistryObject<VillagerProfession> PROF_GROWER = PROFESSIONS.register(
				GROWER.getPath(), () -> createProf(GROWER, POI_FLOWER_POT.get(), ECSounds.VILLAGER_WORK_GROWER)
		);
		public static final RegistryObject<VillagerProfession> PROF_BEEKEEPER = PROFESSIONS.register(
				BEEKEEPER.getPath(), () -> createProf(GROWER, POI_SQUEEZER.get(), ECSounds.VILLAGER_WORK_BEEKEEPER)
		);
		public static final RegistryObject<VillagerProfession> PROF_GEOLOGIST = PROFESSIONS.register(
				GEOLOGIST.getPath(), () -> createProf(GEOLOGIST, POI_CONTINUOUS_MINER.get(), ECSounds.VILLAGER_WORK_GEOLOGIST)
		);
		public static final RegistryObject<VillagerProfession> PROF_ICER = PROFESSIONS.register(
				ICER.getPath(), () -> createProf(ICER, POI_ICE_MAKER.get(), ECSounds.VILLAGER_WORK_ICER)
		);
		public static final RegistryObject<VillagerProfession> PROF_CHEMICAL_ENGINEER = PROFESSIONS.register(
				CHEMICAL_ENGINEER.getPath(), () -> createProf(CHEMICAL_ENGINEER, POI_MELTER.get(), ECSounds.VILLAGER_WORK_CHEMICAL_ENGINEER)
		);

		private static Collection<BlockState> assembleStates(Block block) {
			return block.getStateDefinition().getPossibleStates();
		}

		private static PointOfInterestType createPOI(String name, Collection<BlockState> block) {
			return new PointOfInterestType(MODID+":"+name, ImmutableSet.copyOf(block), 1, 1);
		}

		private static VillagerProfession createProf(ResourceLocation name, PointOfInterestType poi, SoundEvent sound) {
			return new VillagerProfession(
					name.toString(), poi,
					ImmutableSet.<Item>builder().build(),
					ImmutableSet.<Block>builder().build(),
					sound
			);
		}
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class Events {
		@SubscribeEvent
		public static void registerTrades(VillagerTradesEvent event) {
			Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

			if(CARPENTER.equals(event.getType().getRegistryName())) {
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
								.build()
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
								.build()
				));
				trades.get(2).add(new ECTrades.ItemsAndEmeraldsToItems(Items.NETHER_WART_BLOCK, 1, 4, Items.NETHER_WART, 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.IRON_AXE), 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.BOOKSHELF, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.CRAFTING_TABLE, 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsAndEmeraldsToItems(Items.WARPED_WART_BLOCK, 1, 4, ECItems.WARPED_WART.get(), 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BOWL), 1, 6, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.CHEST, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BEEHIVE), 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.JUKEBOX), 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EmeraldsForVillagerTypeItem(3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_BUY,
						ImmutableMap.<VillagerType, Item>builder()
								.put(VillagerType.PLAINS, Items.OAK_SIGN)
								.put(VillagerType.TAIGA, Items.SPRUCE_SIGN)
								.put(VillagerType.SNOW, Items.SPRUCE_SIGN)
								.put(VillagerType.DESERT, Items.JUNGLE_SIGN)
								.put(VillagerType.JUNGLE, Items.JUNGLE_SIGN)
								.put(VillagerType.SAVANNA, Items.ACACIA_SIGN)
								.put(VillagerType.SWAMP, Items.DARK_OAK_SIGN)
								.build()
				));
				trades.get(5).add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.NOTE_BLOCK), 4, 4, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsAndEmeraldsToItems(Items.SHULKER_SHELL, 1, 8, Items.SHULKER_BOX, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GLAZIER.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.CLAY_BALL, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.SAND, 12, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.GLASS_BOTTLE), 1, 6, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				//trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.TINTED_GLASS), 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.WHITE_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.ORANGE_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.MAGENTA_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.LIGHT_BLUE_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.YELLOW_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.LIME_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.PINK_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.GRAY_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.CYAN_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.PURPLE_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BLUE_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BROWN_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.GREEN_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.RED_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BLACK_STAINED_GLASS), 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.DAYLIGHT_DETECTOR), 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.TERRACOTTA), 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.ENDER_EYE, 1, 7, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.END_CRYSTAL), 11, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(MINER.equals(event.getType().getRegistryName())) {
				//trades.get(1).add(new ECTrades.EmeraldForItems(Items.RAW_COPPER, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.IRON_ORE, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.GOLD_ORE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.MINECART), 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.REDSTONE, 4, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.QUARTZ, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.OBSIDIAN, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsAndEmeraldsToItems(Items.OBSIDIAN, 4, 1, Items.CRYING_OBSIDIAN, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL_BLOCK, 2, 32, Items.DIAMOND, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.NETHERITE_SCRAP, 1, 18, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(ASTROLOGIST.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.AMETHYST_SHARD), 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BONE, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.BLAZE_POWDER, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.NETHER_SPROUTS, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.SOUL_SAND, 8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.CHORUS_FRUIT, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.SPIDER_EYE, 7, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.DRAGON_BREATH), 9, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BROWN_MUSHROOM), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 17, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL, 4, 52, Items.WITHER_SKELETON_SKULL, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.SHULKER_SHELL), 12, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.TraderHeadForEmeralds(40, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.CARVED_PUMPKIN), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.DRAGON_HEAD), 36, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.SOUL_SAND), 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.WEEPING_VINES, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.TWISTING_VINES, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GROWER.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.FLOWER_POT), 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECBlocks.Plant.CYAN_PETUNIA.asItem()), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECBlocks.Plant.MAGENTA_PETUNIA.asItem()), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BONE, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.POPPY, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.ORANGE_TULIP), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.PINK_TULIP), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.RED_TULIP), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.WHITE_TULIP), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 17, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BLUE_ORCHID), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.ALLIUM), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.SUNFLOWER, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.LILAC, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.ROSE_BUSH, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.PEONY, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.LILY_OF_THE_VALLEY), 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(Effects.FIRE_RESISTANCE, 100, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(Effects.WITHER, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(Effects.WATER_BREATHING, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.SuspisciousStewForEmerald(Effects.REGENERATION, 120, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(BEEKEEPER.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.HONEYCOMB, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BEEHIVE), 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.HONEY_BOTTLE), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.HONEY_BLOCK, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.ORANGE_TULIP, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.PINK_TULIP, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.RED_TULIP, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.WHITE_TULIP, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.HONEYCOMB_BLOCK, 1, 1, Items.HONEYCOMB, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(Items.DANDELION, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.BannerPatterns.BEE), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(GEOLOGIST.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.NETHERRACK, 20, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.EmeraldForItems(ECBlocks.Decoration.DEEPSLATE, 16, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 2, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.OBSIDIAN), 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.EmeraldForItems(Items.LAPIS_LAZULI, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.SLIME_BALL), 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.EmeraldForItems(ECItems.IRON_CONCENTRATE, 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(4).add(new ECTrades.EmeraldForItems(ECItems.GOLD_CONCENTRATE, 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.BASALT, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.EmeraldForItems(Items.BLACKSTONE, 12, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(ICER.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.PACKED_ICE, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.ICE), 1, 5, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.SNOWBALL), 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.EmeraldForItems(Items.SNOWBALL, 20, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BLUE_ICE), 3, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(3).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.LANTERN), 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.OBSIDIAN), 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.EnchantedItemForEmeralds(Items.LEATHER_BOOTS, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsAndEmeraldsToItems(Items.SNOWBALL, 4, 1, Items.SNOW, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.BannerPatterns.SNOW), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			} else if(CHEMICAL_ENGINEER.equals(event.getType().getRegistryName())) {
				trades.get(1).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.MELTED_EMERALD_BUCKET), 11, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
				trades.get(1).add(new ECTrades.EmeraldForItems(Items.BUCKET, 4, 3, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.MELTED_IRON_BUCKET), 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(2).add(new ECTrades.ItemsForEmeralds(new ItemStack(Items.BUCKET), 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
				trades.get(3).add(new ECTrades.NetheriteForItems(Items.EMERALD_BLOCK, 10, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
				trades.get(4).add(new ECTrades.ItemsAndEmeraldsToItems(Items.FLINT, 4, 2, Items.GUNPOWDER, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(4).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.MELTED_GOLD_BUCKET), 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.BannerPatterns.BOTTLE), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
				trades.get(5).add(new ECTrades.ItemsForEmeralds(new ItemStack(ECItems.BannerPatterns.POTION), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
			}
		}
	}
}
