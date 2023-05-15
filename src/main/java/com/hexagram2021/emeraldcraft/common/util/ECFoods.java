package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class ECFoods {
	public static final FoodProperties CHILI =
			new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build();
	public static final FoodProperties CABBAGE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build();
	public static final FoodProperties AGATE_APPLE =
			new FoodProperties.Builder().nutrition(2).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0), 1.0F)
					.alwaysEat().build();
	public static final FoodProperties JADE_APPLE =
			new FoodProperties.Builder().nutrition(2).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 1200, 1), 1.0F)
					.alwaysEat().build();

	public static final FoodProperties GINKGO_NUT =
			new FoodProperties.Builder().nutrition(2).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.POISON, 40, 0), 0.05F)
					.build();

	public static final FoodProperties PEACH =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build();

	public static final FoodProperties GOLDEN_PEACH =
			new FoodProperties.Builder().nutrition(6).saturationMod(0.6F)
					.effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 800, 1), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.SATURATION, 12, 0), 1.0F)
					.alwaysEat().build();

	public static final FoodProperties COOKED_TROPICAL_FISH =
			new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();

	public static final FoodProperties POTION_COOKIE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.1F)
					.effect(() -> new MobEffectInstance(MobEffects.WITHER, 400, 1), 0.8F)
					.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 800, 0), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 400, 1), 0.8F)
					.build();

	public static final FoodProperties COOKED_PURPURACEUS_FUNGUS =
			new FoodProperties.Builder().nutrition(4).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.SATURATION, 7, 0), 0.05F)
					.build();

	public static final FoodProperties BOILED_EGG =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();

	public static final FoodProperties CHORUS_FLOWER_EGGDROP_SOUP =
			new FoodProperties.Builder().nutrition(3).saturationMod(1.2F).build();
	public static final FoodProperties CARAMELIZED_POTATO =
			new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final FoodProperties ROUGAMO =
			new FoodProperties.Builder().nutrition(12).saturationMod(1.2F).build();
	public static final FoodProperties BEEF_AND_POTATO_STEW =
			new FoodProperties.Builder().nutrition(10).saturationMod(0.8F).build();
	public static final FoodProperties BRAISED_CHICKEN =
			new FoodProperties.Builder().nutrition(10).saturationMod(0.8F).build();

	public static final FoodProperties HERRING =
			new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build();
	public static final FoodProperties PURPLE_SPOTTED_BIGEYE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build();

	public static final FoodProperties COOKED_HERRING =
			new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final FoodProperties COOKED_PURPLE_SPOTTED_BIGEYE =
			new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
	public static final FoodProperties SAUSAGE =
			new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build();
	public static final FoodProperties COOKED_SAUSAGE =
			new FoodProperties.Builder().nutrition(8).saturationMod(1.2F).build();
	public static final FoodProperties GLUTEN =
			new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).build();
	public static final FoodProperties WARDEN_HEART =
			new FoodProperties.Builder().nutrition(8).saturationMod(0.8F)
					.effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 800, 0), 1.0F)
					.build();
	public static final FoodProperties STIR_FRIED_WARDEN_HEART =
			new FoodProperties.Builder().nutrition(12).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 6000, 3), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 1), 1.0F)
					.build();
	public static final FoodProperties APPLE_JUICE =
			new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
	public static final FoodProperties BEETROOT_JUICE =
			new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
	public static final FoodProperties CARROT_JUICE =
			new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
	public static final FoodProperties MELON_JUICE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
	public static final FoodProperties PEACH_JUICE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
	public static final FoodProperties PUMPKIN_JUICE =
			new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();

	public static void compatDiet(Item foodItem, FoodProperties food) {
		InterModComms.sendTo("diet", "item",
				() -> new Tuple<Item, BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>>>(
						foodItem,
						(player, stack) -> new ImmutableTriple<>(Collections.singletonList(stack), food.getNutrition(), food.getSaturationModifier())
				)
		);
	}
}
