package com.hexagram2021.emeraldcraft.api.tradable;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public interface ITradableDataFactory {
	Map<EntityType<?>, ITradableDataFactory> customMobs = Maps.newHashMap();

	static void setTradableMobData(Entity entity, VillagerProfession profession, int level) {
		ITradableDataFactory factory = customMobs.get(entity.getType());
		if(factory != null) {
			factory.setMobData(entity, profession, level);
		}
	}

	static void registerDataFactory(EntityType<?> entityType, ITradableDataFactory factory) {
		customMobs.put(entityType, factory);
	}

	void setMobData(Entity entity, VillagerProfession profession, int level);
}
