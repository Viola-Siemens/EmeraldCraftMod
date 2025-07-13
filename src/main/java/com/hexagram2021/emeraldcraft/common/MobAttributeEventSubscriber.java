package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.animal.AbstractFish;

public class MobAttributeEventSubscriber {
    public static void onAttributeCreate() {
        FabricDefaultAttributeRegistry.register(ECEntities.PIGLIN_CUTEY, PiglinCuteyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.NETHER_PIGMAN, NetherPigmanEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.NETHER_LAMBMAN, NetherLambmanEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.HERRING, AbstractFish.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.PURPLE_SPOTTED_BIGEYE, AbstractFish.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.SNAKEHEAD, AbstractFish.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.WRAITH, WraithEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.MANTA, MantaEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.LUMINE, LumineEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ECEntities.WOMBAT, WombatEntity.createAttributes());
    }
}
