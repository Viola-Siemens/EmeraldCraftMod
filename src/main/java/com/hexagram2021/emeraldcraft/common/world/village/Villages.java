package com.hexagram2021.emeraldcraft.common.world.village;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.mixin.accessor.HeroGiftsTaskAccess;
import com.hexagram2021.emeraldcraft.mixin.accessor.StructureTemplatePoolAccess;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerTypeHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.world.village.TradingConstants.*;

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
    public static final ResourceLocation HUNTER = new ResourceLocation(MODID, "hunter");
    public static final ResourceLocation CHEF = new ResourceLocation(MODID, "chef");

    public static void setup() {
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_CARPENTER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/carpenter_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_GLAZIER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/glazier_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_MINER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/miner_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_ASTROLOGIST, new ResourceLocation(MODID, "gameplay/hero_of_the_village/astrologist_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_GROWER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/grower_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_BEEKEEPER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/beekeeper_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_GEOLOGIST, new ResourceLocation(MODID, "gameplay/hero_of_the_village/geologist_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_ICER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/icer_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_CHEMICAL_ENGINEER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/chemical_engineer_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_PAPERHANGER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/paperhanger_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_HUNTER, new ResourceLocation(MODID, "gameplay/hero_of_the_village/hunter_gift"));
        HeroGiftsTaskAccess.emeraldcraft$getGifts().put(Registers.PROF_CHEF, new ResourceLocation(MODID, "gameplay/hero_of_the_village/chef_gift"));
    }

    public static void addAllStructuresToPool(RegistryAccess registryAccess) {
        addToPool(new ResourceLocation("village/plains/houses"), registryAccess, builder -> {
            builder.add(new ResourceLocation(MODID, "village/plains/houses/plains_beekeeper_1"), 4);
            builder.add(new ResourceLocation(MODID, "village/plains/houses/plains_carpenter_1"), 4);
            builder.add(new ResourceLocation(MODID, "village/plains/houses/plains_paperhanger_1"), 2);
        });
        addToPool(new ResourceLocation("village/snowy/houses"), registryAccess, builder -> {
            builder.add(new ResourceLocation(MODID, "village/snowy/houses/snowy_astrologist_1"), 3);
            builder.add(new ResourceLocation(MODID, "village/snowy/houses/snowy_icer_1"), 4);
        });
        addToPool(new ResourceLocation("village/savanna/houses"), registryAccess, builder -> {
            builder.add(new ResourceLocation(MODID, "village/savanna/houses/savanna_glazier_1"), 4);
            builder.add(new ResourceLocation(MODID, "village/savanna/houses/savanna_miner_1"), 3);
        });
    }

    private static void addToPool(ResourceLocation poolName, RegistryAccess registryAccess, Consumer<PoolBuilder> consumer) {
        Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool structureTemplatePool = registry.get(poolName);
        if (structureTemplatePool == null) {
            ECLogger.error("Ignored empty structure template pool: " + poolName);
            return;
        }
        StructureTemplatePoolAccess pool = (StructureTemplatePoolAccess) structureTemplatePool;
        List<Pair<StructurePoolElement, Integer>> rawTemplates = pool.emeraldcraft$getRawTemplates() instanceof ArrayList ?
                pool.emeraldcraft$getRawTemplates() : new ArrayList<>(pool.emeraldcraft$getRawTemplates());

        PoolBuilder poolBuilder = new PoolBuilder(pool, rawTemplates);
        consumer.accept(poolBuilder);

        pool.emeraldcraft$setRawTemplates(rawTemplates);
    }

    private static final class PoolBuilder {
        StructureTemplatePoolAccess pool;
        List<Pair<StructurePoolElement, Integer>> rawTemplates;

        public PoolBuilder(StructureTemplatePoolAccess pool, List<Pair<StructurePoolElement, Integer>> rawTemplates) {
            this.pool = pool;
            this.rawTemplates = rawTemplates;
        }

        public void add(ResourceLocation toAdd, int weight) {
            SinglePoolElement addedElement = SinglePoolElement.single(toAdd.toString()).apply(StructureTemplatePool.Projection.RIGID);
            this.rawTemplates.add(Pair.of(addedElement, weight));
            this.pool.emeraldcraft$getTemplates().add(addedElement);
        }
    }

    public static class Registers {
        public static void init() {

        }

        public static final PoiType POI_CARPENTRY_TABLE = registerPoiType(
                "carpentry_table", 1, 1, assembleStates(ECBlocks.WorkStation.CARPENTRY_TABLE.get())
        );
        public static final PoiType POI_GLASS_KILN = registerPoiType(
                "glass_kiln", 1, 1, assembleStates(ECBlocks.WorkStation.GLASS_KILN.get())
        );
        public static final PoiType POI_MINERAL_TABLE = registerPoiType(
                "mineral_table", 1, 1, assembleStates(ECBlocks.WorkStation.MINERAL_TABLE.get())
        );
        public static final PoiType POI_CRYSTALBALL_TABLE = registerPoiType(
                "crystalball_table", 1, 1, assembleStates(ECBlocks.WorkStation.CRYSTALBALL_TABLE.get())
        );
        public static final PoiType POI_FLOWER_POT = registerPoiType(
                "flower_pot", 1, 1, assembleStates(Blocks.FLOWER_POT)
        );
        public static final PoiType POI_SQUEEZER = registerPoiType(
                "squeezer", 1, 1, assembleStates(ECBlocks.WorkStation.SQUEEZER.get())
        );
        public static final PoiType POI_CONTINUOUS_MINER = registerPoiType(
                "continuous_miner", 1, 1, assembleStates(ECBlocks.WorkStation.CONTINUOUS_MINER.get())
        );
        public static final PoiType POI_ICE_MAKER = registerPoiType(
                "ice_maker", 1, 1, assembleStates(ECBlocks.WorkStation.ICE_MAKER.get())
        );
        public static final PoiType POI_MELTER = registerPoiType(
                "melter", 1, 1, assembleStates(ECBlocks.WorkStation.MELTER.get())
        );
        public static final PoiType POI_RABBLE_FURNACE = registerPoiType(
                "rabble_furnace", 1, 1, assembleStates(ECBlocks.WorkStation.RABBLE_FURNACE.get())
        );
        public static final PoiType POI_MEAT_GRINDER = registerPoiType(
                "meat_grinder", 1, 1, assembleStates(ECBlocks.WorkStation.MEAT_GRINDER.get())
        );
        public static final PoiType POI_COOKSTOVE = registerPoiType(
                "cookstove", 1, 1, assembleStates(ECBlocks.WorkStation.COOKSTOVE.get())
        );

        public static final VillagerType VILLAGER_TYPE_CARPENTRY = registerVillagerType(CARPENTER);
        public static final VillagerType VILLAGER_TYPE_GLASS_KILN = registerVillagerType(GLAZIER);
        public static final VillagerType VILLAGER_TYPE_MINER = registerVillagerType(MINER);
        public static final VillagerType VILLAGER_TYPE_ASTROLOGIST = registerVillagerType(ASTROLOGIST);
        public static final VillagerType VILLAGER_TYPE_GROWER = registerVillagerType(GROWER);
        public static final VillagerType VILLAGER_TYPE_BEEKEEPER = registerVillagerType(BEEKEEPER);
        public static final VillagerType VILLAGER_TYPE_GEOLOGIST = registerVillagerType(GEOLOGIST);
        public static final VillagerType VILLAGER_TYPE_ICER = registerVillagerType(ICER);
        public static final VillagerType VILLAGER_TYPE_CHEMICAL_ENGINEER = registerVillagerType(CHEMICAL_ENGINEER);
        public static final VillagerType VILLAGER_TYPE_PAPERHANGER = registerVillagerType(PAPERHANGER);
        public static final VillagerType VILLAGER_TYPE_HUNTER = registerVillagerType(HUNTER);
        public static final VillagerType VILLAGER_TYPE_CHEF = registerVillagerType(CHEF);

        public static final VillagerProfession PROF_CARPENTER = registerVillagerProfession(
                CARPENTER, createProf(CARPENTER, POI_CARPENTRY_TABLE, ECSounds.VILLAGER_WORK_CARPENTER)
        );
        public static final VillagerProfession PROF_GLAZIER = registerVillagerProfession(
                GLAZIER, createProf(GLAZIER, POI_GLASS_KILN, ECSounds.VILLAGER_WORK_GLAZIER)
        );
        public static final VillagerProfession PROF_MINER = registerVillagerProfession(
                MINER, createProf(MINER, POI_MINERAL_TABLE, ECSounds.VILLAGER_WORK_MINER)
        );
        public static final VillagerProfession PROF_ASTROLOGIST = registerVillagerProfession(
                ASTROLOGIST, createProf(ASTROLOGIST, POI_CRYSTALBALL_TABLE, ECSounds.VILLAGER_WORK_ASTROLOGIST)
        );
        public static final VillagerProfession PROF_GROWER = registerVillagerProfession(
                GROWER, createProf(GROWER, POI_FLOWER_POT, ECSounds.VILLAGER_WORK_GROWER)
        );
        public static final VillagerProfession PROF_BEEKEEPER = registerVillagerProfession(
                BEEKEEPER, createProf(GROWER, POI_SQUEEZER, ECSounds.VILLAGER_WORK_BEEKEEPER)
        );
        public static final VillagerProfession PROF_GEOLOGIST = registerVillagerProfession(
                GEOLOGIST, createProf(GEOLOGIST, POI_CONTINUOUS_MINER, ECSounds.VILLAGER_WORK_GEOLOGIST)
        );
        public static final VillagerProfession PROF_ICER = registerVillagerProfession(
                ICER, createProf(ICER, POI_ICE_MAKER, ECSounds.VILLAGER_WORK_ICER)
        );
        public static final VillagerProfession PROF_CHEMICAL_ENGINEER = registerVillagerProfession(
                CHEMICAL_ENGINEER, createProf(CHEMICAL_ENGINEER, POI_MELTER, ECSounds.VILLAGER_WORK_CHEMICAL_ENGINEER)
        );
        public static final VillagerProfession PROF_PAPERHANGER = registerVillagerProfession(
                PAPERHANGER, createProf(PAPERHANGER, POI_RABBLE_FURNACE, ECSounds.VILLAGER_WORK_PAPERHANGER)
        );
        public static final VillagerProfession PROF_HUNTER = registerVillagerProfession(
                HUNTER, createProf(HUNTER, POI_MEAT_GRINDER, ECSounds.VILLAGER_WORK_HUNTER)
        );
        public static final VillagerProfession PROF_CHEF = registerVillagerProfession(
                CHEF, createProf(CHEF, POI_COOKSTOVE, ECSounds.VILLAGER_WORK_CHEF)
        );

        @SuppressWarnings("SameParameterValue")
        private static PoiType registerPoiType(String name, int ticketCount, int searchDistance, Set<BlockState> states) {
            return PointOfInterestHelper.register(EmeraldCraft.id(name), ticketCount, searchDistance, states);
        }

        @SuppressWarnings("deprecation")
        private static VillagerType registerVillagerType(ResourceLocation id) {
            return VillagerTypeHelper.register(id);
        }

        private static VillagerProfession registerVillagerProfession(ResourceLocation id, VillagerProfession profession) {
            return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, id, profession);
        }

        private static Set<BlockState> assembleStates(Block block) {
            return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
        }

        private static VillagerProfession createProf(ResourceLocation id, PoiType poi, SoundEvent sound) {
            ResourceKey<PoiType> resourceKey = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poi).orElseThrow();
            return new VillagerProfession(
                    id.toString(),
                    (p) -> p.is(resourceKey),
                    (p) -> p.is(resourceKey),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    sound
            );
        }
    }

    public static class Events {
        @SafeVarargs
        private static void registerTrade(VillagerProfession profession, Consumer<List<VillagerTrades.ItemListing>>... multiTrades) {
            int level = 1;
            for (Consumer<List<VillagerTrades.ItemListing>> trades : multiTrades) {
                TradeOfferHelper.registerVillagerOffers(profession, level, trades);
                level++;
            }
        }

        private static void registerTrade(VillagerProfession profession, int level, Consumer<List<VillagerTrades.ItemListing>> trades) {
            TradeOfferHelper.registerVillagerOffers(profession, level, trades);
        }

        public static void registerTrades() {
            //CARPENTER
            registerTrade(Registers.PROF_CARPENTER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.STICK, 32, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.EmeraldsForVillagerTypeItem(8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL,
                                CARPENTER_LEVEL_1_SAPLINGS.build(), Items.OAK_SAPLING));
                        trades.add(new ECTrades.VillagerTypeItemForEmeralds(2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY,
                                CARPENTER_LEVEL_1_LOGS.build(), Items.OAK_LOG));

                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.NETHER_WART_BLOCK, 1, 4, Items.NETHER_WART, 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.IRON_AXE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.BOOKSHELF, 1, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.CRAFTING_TABLE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));

                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.WARPED_WART_BLOCK, 1, 4, ECItems.WARPED_WART.get(), 8, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BOWL, 1, 6, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.CHEST, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BEEHIVE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.JUKEBOX, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.EmeraldsForVillagerTypeItem(4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_BUY, CARPENTER_LEVEL_4_SIGNS.build(), Items.OAK_SIGN));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.NOTE_BLOCK, 4, 4, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.SHULKER_SHELL, 1, 12, Items.SHULKER_BOX, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //GLAZIER
            registerTrade(Registers.PROF_GLAZIER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CLAY_BALL, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.SAND, 12, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));

                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.GLASS_BOTTLE, 1, 6, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.TINTED_GLASS, 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.WHITE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.ORANGE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.MAGENTA_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.LIGHT_BLUE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.YELLOW_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.LIME_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.PINK_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.GRAY_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.LIGHT_GRAY_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.CYAN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.PURPLE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BLUE_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BROWN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.GREEN_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.RED_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BLACK_STAINED_GLASS, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.DAYLIGHT_DETECTOR, 1, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.TERRACOTTA, 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.ENDER_EYE, 1, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.END_CRYSTAL, 11, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //MINER
            registerTrade(Registers.PROF_MINER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.RAW_COPPER, 14, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.RAW_IRON, 10, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.RAW_GOLD, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.MINECART, 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));

                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.REDSTONE, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.QUARTZ, 8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.POINTED_DRIPSTONE, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.AMETHYST_BLOCK, 1, 4, Items.AMETHYST_SHARD, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 12, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL_BLOCK, 2, 32, Items.DIAMOND, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.NETHERITE_SCRAP, 1, 10, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //ASTROLOGIST
            registerTrade(Registers.PROF_ASTROLOGIST,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.SPYGLASS, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.BONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.BLAZE_POWDER, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.NETHER_SPROUTS, 10, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.SOUL_SAND, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CHORUS_FRUIT, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.SPIDER_EYE, 13, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.DRAGON_BREATH, 9, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 6, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.COAL, 4, 56, Items.WITHER_SKELETON_SKULL, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.SHULKER_SHELL, 12, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.TraderHeadForEmeralds(40, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.CARVED_PUMPKIN, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.DRAGON_HEAD, 36, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.SOUL_SAND, 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.WEEPING_VINES, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.TWISTING_VINES, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //GROWER
            registerTrade(Registers.PROF_GROWER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.FLOWER_POT, 1, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.CYAN_PETUNIA, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.MAGENTA_PETUNIA, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.BONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.GLASS_PANE, 11, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.POPPY, 9, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBlocks.Plant.HIGAN_BANA, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.PINK_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.RED_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.WITHER_ROSE, 1, 6, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BLUE_ORCHID, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.ALLIUM, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.SUNFLOWER, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.LILAC, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.ROSE_BUSH, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.PEONY, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.SuspisciousStewForEmerald(MobEffects.FIRE_RESISTANCE, 100, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.SuspisciousStewForEmerald(MobEffects.WITHER, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.SuspisciousStewForEmerald(MobEffects.WATER_BREATHING, 160, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.SuspisciousStewForEmerald(MobEffects.REGENERATION, 120, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));

                    }
            );
            //BEEKEEPER
            registerTrade(Registers.PROF_BEEKEEPER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.HONEYCOMB, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BEEHIVE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.HONEY_BOTTLE, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.HONEY_BLOCK, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.ORANGE_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.PINK_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.RED_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.WHITE_TULIP, 6, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.HONEYCOMB_BLOCK, 1, 1, Items.HONEYCOMB, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.DANDELION, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.BEE, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //GEOLOGIST
            registerTrade(Registers.PROF_GEOLOGIST,
                    //level1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.NETHERRACK, 24, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.DEEPSLATE, 18, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CRYING_OBSIDIAN, 2, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.OBSIDIAN, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.LAPIS_LAZULI, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(ECItems.IRON_CONCENTRATE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.EmeraldForItems(ECItems.GOLD_CONCENTRATE, 2, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.EmeraldForItems(ECItems.COPPER_CONCENTRATE, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.CALCITE, 1, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.SMOOTH_BASALT, 14, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForItems(Items.BLACKSTONE, 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //ICER
            registerTrade(Registers.PROF_ICER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.PACKED_ICE, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.ICE, 1, 5, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.POWDER_SNOW_BUCKET, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.SNOW_BLOCK, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BLUE_ICE, 3, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.LANTERN, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(Items.OBSIDIAN, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.EnchantedItemForEmeralds(Items.LEATHER_BOOTS, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.SNOWBALL, 4, 1, Items.SNOW, 4, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.SNOW, 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //CHEMICAL_ENGINEER
            registerTrade(Registers.PROF_CHEMICAL_ENGINEER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECFluids.MELTED_EMERALD.getBucket(), 12, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.EmeraldForItems(Items.BUCKET, 4, 2, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECFluids.MELTED_IRON.getBucket(), 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(ECFluids.MELTED_COPPER.getBucket(), 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.NetheriteScrapForItems(Items.EMERALD_BLOCK, 10, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.ItemsAndEmeraldsToItems(Items.FLINT, 4, 2, Items.GUNPOWDER, 4, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(ECFluids.MELTED_GOLD.getBucket(), 4, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.BOTTLE.item(), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBannerPatterns.POTION.item(), 8, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //PAPERHANGER
            registerTrade(Registers.PROF_PAPERHANGER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.PAPER, 24, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBlocks.Decoration.RESIN_BLOCK, 2, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(ECItems.WINDOW_FILM, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(ECBlocks.Decoration.REINFORCED_RESIN_BLOCK, 1, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(ECBlocks.Decoration.PAPER_BLOCK, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldsForVillagerTypeItem(15, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY,
                                PAPERHANGER_LEVEL_4_PLANKS.build(), Items.OAK_PLANKS));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.PAINTING, 2, 3, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.GLOW_INK_SAC, 5, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.COBWEB, 6, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //HUNTER
            registerTrade(Registers.PROF_HUNTER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.IRON_INGOT, 4, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.PORKCHOP, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BEEF, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.ARROW, 56, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.STRING, 20, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.TRIPWIRE_HOOK, 8, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.MUTTON, 1, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.LEATHER, 6, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.CHICKEN, 1, 2, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.SPYGLASS, 1, 1, ECTrades.ONLY_SUPPLY_ONCE, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.RABBIT, 2, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(ECItems.WARDEN_HEART, 1, 5, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 3, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //CHEF
            registerTrade(Registers.PROF_CHEF,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CHARCOAL, 12, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BREAD, 1, 6, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.BEETROOT_SOUP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.BAKED_POTATO, 14, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.EmeraldForRandomItems(CHEF_LEVEL_2_MUSHROOMS.build(), 16, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(Items.MUSHROOM_STEW, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    },
                    //level 3
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.CHORUS_FRUIT, 22, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(ECItems.BEEF_AND_POTATO_STEW, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                        trades.add(new ECTrades.ItemsForEmeralds(ECItems.BRAISED_CHICKEN, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_3_SELL));
                    },
                    //level 4
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(Items.COOKED_BEEF, 7, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.EmeraldForItems(Items.COOKED_CHICKEN, 10, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_4_BUY));
                        trades.add(new ECTrades.ItemsForEmeralds(ECItems.CHORUS_FLOWER_EGGDROP_SOUP, 1, 1, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_4_SELL));
                    },
                    //level 5
                    trades -> {
                        trades.add(new ECTrades.EmeraldForItems(ECItems.WHEAT_DOUGH, 8, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.EmeraldForRandomItems(CHEF_LEVEL_5_MINCES.build(), 15, 1, ECTrades.COMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.DumplingsForEmeralds(ECItems.COOKED_DUMPLING, 20, 30, 45, 15, 1, 9, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.DumplingsForEmeralds(ECItems.COOKED_DUMPLING, 45, 45, 15, 30, 1, 9, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
            //FARMER
            registerTrade(VillagerProfession.FARMER,
                    //level 1
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECItems.CHILI_SEED, 1, 2, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_1_SELL));
                    },
                    //level 2
                    trades -> {
                        trades.add(new ECTrades.ItemsForEmeralds(ECItems.PEACH, 3, 1, ECTrades.UNCOMMON_ITEMS_SUPPLY, ECTrades.XP_LEVEL_2_SELL));
                    }
            );
            //CARTOGRAPHER
            registerTrade(VillagerProfession.CARTOGRAPHER, 5,
                    trades -> {
                        trades.add(new ECTrades.NetherStructureMapForEmeralds(12, 2, ECStructureTags.ON_SAR_EXPLORER_MAPS, "filled_map.shelter", ECMapDecorationTypes.SHELTER, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                        trades.add(new ECTrades.NetherStructureMapForEmeralds(14, 2, ECStructureTags.ON_GEOCENTER_EXPLORER_MAPS, "filled_map.entrenchment", ECMapDecorationTypes.ENTRENCHMENT, ECTrades.DEFAULT_SUPPLY, ECTrades.XP_LEVEL_5_TRADE));
                    }
            );
        }

        public static void registerWandererTrades() {
            //generic
            TradeOfferHelper.registerWanderingTraderOffers(1,
                    genericTrades -> {
                        genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.CHILI, 3, 1, 5, ECTrades.XP_LEVEL_1_SELL));
                        genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.CABBAGE, 3, 1, 5, ECTrades.XP_LEVEL_1_SELL));
                        genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.PEACH, 4, 1, 5, ECTrades.XP_LEVEL_1_SELL));
                        genericTrades.add(new ECTrades.ItemsForEmeralds(ECItems.GINKGO_NUT, 2, 1, 4, ECTrades.XP_LEVEL_1_SELL));
                    }
            );
            //rare
            TradeOfferHelper.registerWanderingTraderOffers(2,
                    rareTrades -> {
                        rareTrades.add(new ECTrades.ItemsForEmeralds(ECItems.GLUTEN, 3, 1, 4, ECTrades.XP_LEVEL_1_SELL));
                    }
            );
        }
    }
}
