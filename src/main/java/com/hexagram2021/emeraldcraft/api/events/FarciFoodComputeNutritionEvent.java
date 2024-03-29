package com.hexagram2021.emeraldcraft.api.events;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fires in Forge bus, and is not cancelable.
 */
public class FarciFoodComputeNutritionEvent extends Event {
	private final Item item;
	private int nutritionAdder;
	private final List<Pair<Supplier<MobEffectInstance>, Float>> newEffects;


	public FarciFoodComputeNutritionEvent(Item item, int nutritionAdder, List<Pair<Supplier<MobEffectInstance>, Float>> newEffects) {
		this.item = item;
		this.nutritionAdder = nutritionAdder;
		this.newEffects = newEffects;
	}

	public Item getItem() {
		return this.item;
	}
	public void addNutrition(int adder) {
		this.nutritionAdder += adder;
	}
	public int getNutritionAdder() {
		return this.nutritionAdder;
	}
	public void addEffect(Supplier<MobEffectInstance> effect, float possibility) {
		this.newEffects.add(Pair.of(effect, possibility));
	}
}
