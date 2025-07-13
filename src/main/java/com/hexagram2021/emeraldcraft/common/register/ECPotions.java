package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class ECPotions {
    public static final Potion HUNGER = register("hunger", new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 1800)));
    public static final Potion LONG_HUNGER = register("long_hunger", new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 4800)));
    public static final Potion STRONG_HUNGER = register("strong_hunger", new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 400, 3)));
    public static final Potion SATURATION = register("saturation", new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10, 2)));
    public static final Potion STRONG_SATURATION = register("strong_saturation", new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10, 7)));
    public static final Potion WITHER = register("wither", new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 900)));
    public static final Potion LONG_WITHER = register("long_wither", new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 1800)));
    public static final Potion STRONG_WITHER = register("strong_wither", new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 432, 1)));
    public static final Potion BLINDNESS = register("blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 900)));
    public static final Potion LONG_BLINDNESS = register("long_blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 1800)));
    public static final Potion ABSORPTION = register("absorption", new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 3600)));
    public static final Potion LONG_ABSORPTION = register("long_absorption", new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 9600)));
    public static final Potion STRONG_ABSORPTION = register("strong_absorption", new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 800, 3)));
    public static final Potion GLOWING = register("glowing", new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 1800)));
    public static final Potion LONG_GLOWING = register("long_glowing", new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 4800)));

    private static Potion register(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, EmeraldCraft.id(name), potion);
    }

    private ECPotions() {
    }

    public static void init() {
    }
}
