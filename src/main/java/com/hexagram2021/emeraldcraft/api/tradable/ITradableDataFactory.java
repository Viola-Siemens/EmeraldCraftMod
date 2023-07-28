package com.hexagram2021.emeraldcraft.api.tradable;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public interface ITradableDataFactory {
	Map<EntityType<?>, ITradableDataFactory> CUSTOM_MOBS = Maps.newHashMap();

	static void setTradableMobData(Entity entity, @Nullable VillagerProfession profession, int level) {
		ITradableDataFactory factory = CUSTOM_MOBS.get(entity.getType());
		if(factory != null) {
			factory.setMobData(entity, profession, level);
		}
	}

	static void registerDataFactory(EntityType<?> entityType, ITradableDataFactory factory) {
		CUSTOM_MOBS.put(entityType, factory);
	}

	void setMobData(Entity entity, @Nullable VillagerProfession profession, int level);
}
