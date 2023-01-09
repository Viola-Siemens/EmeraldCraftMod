package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegisterEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPotions {
	public static final Potion HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 1800));
	public static final Potion LONG_HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 4800));
	public static final Potion STRONG_HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 400, 3));
	public static final Potion SATURATION = new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10, 2));
	public static final Potion STRONG_SATURATION = new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10, 7));
	public static final Potion WITHER = new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 900));
	public static final Potion LONG_WITHER = new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 1800));
	public static final Potion STRONG_WITHER = new Potion("wither", new MobEffectInstance(MobEffects.WITHER, 432, 1));
	public static final Potion BLINDNESS = new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 900));
	public static final Potion LONG_BLINDNESS = new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 1800));
	public static final Potion ABSORPTION = new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 3600));
	public static final Potion LONG_ABSORPTION = new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 9600));
	public static final Potion STRONG_ABSORPTION = new Potion("absorption", new MobEffectInstance(MobEffects.ABSORPTION, 800, 3));
	public static final Potion GLOWING = new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 1800));
	public static final Potion LONG_GLOWING = new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 4800));


	private ECPotions() {}

	public static void init(RegisterEvent event) {
		event.register(Registry.POTION_REGISTRY, helper -> {
			helper.register(new ResourceLocation(MODID, "hunger"), HUNGER);
			helper.register(new ResourceLocation(MODID, "long_hunger"), LONG_HUNGER);
			helper.register(new ResourceLocation(MODID, "strong_hunger"), STRONG_HUNGER);
			helper.register(new ResourceLocation(MODID, "saturation"), SATURATION);
			helper.register(new ResourceLocation(MODID, "strong_saturation"), STRONG_SATURATION);
			helper.register(new ResourceLocation(MODID, "wither"), WITHER);
			helper.register(new ResourceLocation(MODID, "long_wither"), LONG_WITHER);
			helper.register(new ResourceLocation(MODID, "strong_wither"), STRONG_WITHER);
			helper.register(new ResourceLocation(MODID, "blindness"), BLINDNESS);
			helper.register(new ResourceLocation(MODID, "long_blindness"), LONG_BLINDNESS);
			helper.register(new ResourceLocation(MODID, "absorption"), ABSORPTION);
			helper.register(new ResourceLocation(MODID, "long_absorption"), LONG_ABSORPTION);
			helper.register(new ResourceLocation(MODID, "strong_absorption"), STRONG_ABSORPTION);
			helper.register(new ResourceLocation(MODID, "glowing"), GLOWING);
			helper.register(new ResourceLocation(MODID, "long_glowing"), LONG_GLOWING);
		});
	}
}
