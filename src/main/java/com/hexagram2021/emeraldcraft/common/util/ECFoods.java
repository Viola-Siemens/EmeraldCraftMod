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
	
	public static final Food GOLDEN_PEACH =
			new Food.Builder().nutrition(6).saturationMod(0.6F)
					.effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 800, 1), 1.0F)
					.effect(() -> new EffectInstance(Effects.SATURATION, 12, 0), 1.0F)
					.alwaysEat().build();
	
	public static final Food COOKED_TROPICAL_FISH =
			new Food.Builder().nutrition(5).saturationMod(0.6F).build();
	
	public static final Food POTION_COOKIE =
			new Food.Builder().nutrition(2).saturationMod(0.1F)
					.effect(() -> new EffectInstance(Effects.WITHER, 400, 1), 0.8F)
					.effect(() -> new EffectInstance(Effects.HUNGER, 800, 0), 1.0F)
					.effect(() -> new EffectInstance(Effects.CONFUSION, 400, 1), 0.8F)
					.build();
	
	public static final Food BOILED_EGG =
			new Food.Builder().nutrition(2).saturationMod(0.6F).build();
	
	public static final Food CHORUS_FLOWER_EGGDROP_SOUP =
			new Food.Builder().nutrition(3).saturationMod(1.2F).build();
	public static final Food CARAMELIZED_POTATO =
			new Food.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final Food BEEF_AND_POTATO_STEW =
			new Food.Builder().nutrition(10).saturationMod(0.8F).build();
	
	public static final Food HERRING =
			new Food.Builder().nutrition(1).saturationMod(0.1F).build();
	public static final Food PURPLE_SPOTTED_BIGEYE =
			new Food.Builder().nutrition(2).saturationMod(0.1F).build();
	
	public static final Food COOKED_HERRING =
			new Food.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final Food COOKED_PURPLE_SPOTTED_BIGEYE =
			new Food.Builder().nutrition(6).saturationMod(0.6F).build();
	public static final Food SAUSAGE =
			new Food.Builder().nutrition(4).saturationMod(0.6F).build();
	public static final Food COOKED_SAUSAGE =
			new Food.Builder().nutrition(8).saturationMod(1.2F).build();
	public static final Food GLUTEN =
			new Food.Builder().nutrition(4).saturationMod(0.8F).build();
	public static final Food APPLE_JUICE =
			new Food.Builder().nutrition(3).saturationMod(0.6F).build();
	public static final Food BEETROOT_JUICE =
			new Food.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final Food CARROT_JUICE =
			new Food.Builder().nutrition(3).saturationMod(0.6F).build();
	public static final Food MELON_JUICE =
			new Food.Builder().nutrition(2).saturationMod(0.6F).build();
	public static final Food PEACH_JUICE =
			new Food.Builder().nutrition(2).saturationMod(0.6F).build();
	public static final Food PUMPKIN_JUICE =
			new Food.Builder().nutrition(2).saturationMod(0.6F).build();
	
}
