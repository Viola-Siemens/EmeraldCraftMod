package com.hexagram2021.emeraldcraft.common.util;


import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ECFoods {
	public static final Food AGATE_APPLE =
			(new Food.Builder()).nutrition(2).saturationMod(1.2F)
					.effect(() -> new EffectInstance(Effects.DAMAGE_BOOST, 1200, 0), 1.0F)
					.effect(() -> new EffectInstance(Effects.DIG_SPEED, 1200, 0), 1.0F)
					.alwaysEat().build();
	public static final Food JADE_APPLE =
			(new Food.Builder()).nutrition(2).saturationMod(1.2F)
					.effect(() -> new EffectInstance(Effects.HERO_OF_THE_VILLAGE, 1200, 1), 1.0F)
					.alwaysEat().build();

	public static final Food GINKGO_NUT =
			(new Food.Builder()).nutrition(2).saturationMod(1.2F)
					.effect(() -> new EffectInstance(Effects.POISON, 40, 0), 0.05F)
					.build();

	public static final Food PEACH =
			(new Food.Builder()).nutrition(2).saturationMod(1.2F).build();
}
