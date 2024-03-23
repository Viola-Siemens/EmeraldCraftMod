package com.hexagram2021.emeraldcraft.api.tradable;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ITradableDataFactory<T extends Entity> {
	Map<EntityType<?>, ITradableDataFactory<?>> CUSTOM_MOBS = Maps.newHashMap();

	@ApiStatus.Internal
	@SuppressWarnings("unchecked")
	static <T extends Entity> void setTradableMobData(T entity, @Nullable VillagerProfession profession, int level) {
		ITradableDataFactory<T> factory = (ITradableDataFactory<T>) CUSTOM_MOBS.get(entity.getType());
		if(factory != null) {
			factory.setMobData(entity, profession, level);
		}
	}

	/**
	 * Register your own tradable entity.
	 * @param entityType	Type of the tradable entity.
	 * @param factory		TradableDataFactory of the tradable entity.
	 */
	static <T extends Entity> void registerDataFactory(EntityType<T> entityType, ITradableDataFactory<T> factory) {
		CUSTOM_MOBS.put(entityType, factory);
	}

	/**
	 * This function is only used in client side in JEI, to set your own tradable entity in corresponding profession,
	 * level, or any other data to render.
	 * @param entity		The entity to be set data.
	 * @param profession	Profession(if it is villager-like mobs).
	 * @param level			Trading level of the entity.
	 */
	void setMobData(T entity, @Nullable VillagerProfession profession, int level);
}
