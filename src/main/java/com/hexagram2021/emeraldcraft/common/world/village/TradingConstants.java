package com.hexagram2021.emeraldcraft.common.world.village;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class TradingConstants {
    // API for trading. Don't call before FMLCommonSetupEvent is fired.

    public static final ImmutableMap.Builder<VillagerType, Item> CARPENTER_LEVEL_1_SAPLINGS = ImmutableMap.<VillagerType, Item>builder()
            .put(VillagerType.PLAINS, Items.OAK_SAPLING)
            .put(VillagerType.TAIGA, Items.SPRUCE_SAPLING)
            .put(VillagerType.SNOW, Items.SPRUCE_SAPLING)
            .put(VillagerType.DESERT, Items.JUNGLE_SAPLING)
            .put(VillagerType.JUNGLE, Items.JUNGLE_SAPLING)
            .put(VillagerType.SAVANNA, Items.ACACIA_SAPLING)
            .put(VillagerType.SWAMP, Items.DARK_OAK_SAPLING);
    public static final ImmutableMap.Builder<VillagerType, Item> CARPENTER_LEVEL_1_LOGS = ImmutableMap.<VillagerType, Item>builder()
            .put(VillagerType.PLAINS, Items.OAK_LOG)
            .put(VillagerType.TAIGA, Items.SPRUCE_LOG)
            .put(VillagerType.SNOW, Items.SPRUCE_LOG)
            .put(VillagerType.DESERT, Items.JUNGLE_LOG)
            .put(VillagerType.JUNGLE, Items.JUNGLE_LOG)
            .put(VillagerType.SAVANNA, Items.ACACIA_LOG)
            .put(VillagerType.SWAMP, Items.DARK_OAK_LOG);
    public static final ImmutableMap.Builder<VillagerType, Item> CARPENTER_LEVEL_4_SIGNS = ImmutableMap.<VillagerType, Item>builder()
            .put(VillagerType.PLAINS, Items.OAK_SIGN)
            .put(VillagerType.TAIGA, Items.SPRUCE_SIGN)
            .put(VillagerType.SNOW, Items.SPRUCE_SIGN)
            .put(VillagerType.DESERT, Items.JUNGLE_SIGN)
            .put(VillagerType.JUNGLE, Items.JUNGLE_SIGN)
            .put(VillagerType.SAVANNA, Items.ACACIA_SIGN)
            .put(VillagerType.SWAMP, Items.DARK_OAK_SIGN);
    public static final ImmutableMap.Builder<VillagerType, Item> PAPERHANGER_LEVEL_4_PLANKS = ImmutableMap.<VillagerType, Item>builder()
            .put(VillagerType.PLAINS, Items.OAK_PLANKS)
            .put(VillagerType.TAIGA, Items.SPRUCE_PLANKS)
            .put(VillagerType.SNOW, Items.SPRUCE_PLANKS)
            .put(VillagerType.DESERT, Items.JUNGLE_PLANKS)
            .put(VillagerType.JUNGLE, Items.JUNGLE_PLANKS)
            .put(VillagerType.SAVANNA, Items.ACACIA_PLANKS)
            .put(VillagerType.SWAMP, Items.DARK_OAK_PLANKS);
    public static final ImmutableList.Builder<Item> CHEF_LEVEL_2_MUSHROOMS = ImmutableList.<Item>builder()
            .add(Items.RED_MUSHROOM)
            .add(Items.BROWN_MUSHROOM);
    public static final ImmutableList.Builder<Item> CHEF_LEVEL_5_MINCES = ImmutableList.<Item>builder()
            .add(ECItems.MINCED_BEEF.get())
            .add(ECItems.MINCED_CHICKEN.get())
            .add(ECItems.MINCED_MUTTON.get())
            .add(ECItems.MINCED_PORK.get())
            .add(ECItems.MINCED_RABBIT.get());
}
