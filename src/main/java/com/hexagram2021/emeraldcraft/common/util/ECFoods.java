package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ECFoods {
	public static final FoodProperties AGATE_APPLE =
			(new FoodProperties.Builder()).nutrition(2).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0), 1.0F)
					.alwaysEat().build();
	public static final FoodProperties JADE_APPLE =
			(new FoodProperties.Builder()).nutrition(2).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 1200, 1), 1.0F)
					.alwaysEat().build();
}
