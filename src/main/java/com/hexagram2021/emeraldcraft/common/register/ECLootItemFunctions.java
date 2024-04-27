package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.util.loot_function.DumplingsRandomFillingFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECLootItemFunctions {
	public static final LootItemFunctionType DUMPLINGS_RANDOM_FILLINGS = new LootItemFunctionType(DumplingsRandomFillingFunction.CODEC);

	public static void init(ECContent.RegisterConsumer<LootItemFunctionType> register) {
		register.accept(new ResourceLocation(MODID, "dumplings_random_fillings"), DUMPLINGS_RANDOM_FILLINGS);
	}
}
