package com.hexagram2021.emeraldcraft.api.events;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import java.util.List;

public class FarciFoodComputeNutritionEvent {
    private final Item item;
    private final boolean cooked;
    private int nutritionAdder;
    private final List<Pair<MobEffectInstance, Float>> newEffects;

    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class, (callbacks) -> (event) -> {
        for (Callback callback : callbacks) {
            callback.post(event);
        }
    });

    public FarciFoodComputeNutritionEvent(Item item, boolean cooked, int nutritionAdder, List<Pair<MobEffectInstance, Float>> newEffects) {
        this.item = item;
        this.cooked = cooked;
        this.nutritionAdder = nutritionAdder;
        this.newEffects = newEffects;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean isCooked() {
        return this.cooked;
    }

    public void addNutrition(int adder) {
        this.nutritionAdder += adder;
    }

    public int getNutritionAdder() {
        return this.nutritionAdder;
    }

    public void addEffect(MobEffectInstance effect, float possibility) {
        this.newEffects.add(Pair.of(effect, possibility));
    }

    public interface Callback {
        void post(FarciFoodComputeNutritionEvent event);
    }
}
