package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.util.loot_function.DumplingsRandomFillingFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public final class ECLootItemFunctions {
    public static final LootItemFunctionType DUMPLINGS_RANDOM_FILLINGS = register("dumplings_random_fillings", new LootItemFunctionType(new DumplingsRandomFillingFunction.Serializer()));

    @SuppressWarnings("SameParameterValue")
    private static LootItemFunctionType register(String name, LootItemFunctionType type) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, EmeraldCraft.id(name), type);
    }

    public static void init() {
    }
}
