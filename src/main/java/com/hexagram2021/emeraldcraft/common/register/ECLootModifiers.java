package com.hexagram2021.emeraldcraft.common.register;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;

public class ECLootModifiers {
    public static void init() {
        LootTableEvents.MODIFY.register(
                (resourceManager, lootManager, id, tableBuilder, source) -> {
                    if (id.toString().equals("minecraft:entities/warden")) {
                        LootPool pool = LootPool.lootPool()
                                .with(LootItem.lootTableItem(ECItems.WARDEN_HEART).build())
                                .conditionally(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity().of(EntityType.WARDEN).build()
                                        ).build())
                                .conditionally(LootItemKilledByPlayerCondition.killedByPlayer().build())
                                .build();
                        tableBuilder.pool(pool);
                    }
                });
    }
}
