package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPotions {
	public static final Potion HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 1800));
	public static final Potion LONG_HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 4800));
	public static final Potion STRONG_HUNGER = new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, 400, 3));
	public static final Potion SATURATION = new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10));
	public static final Potion STRONG_SATURATION = new Potion("saturation", new MobEffectInstance(MobEffects.SATURATION, 10, 2));
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

	public static void init(RegistryEvent.Register<Potion> event) {
		HUNGER.setRegistryName(new ResourceLocation(MODID, "hunger"));
		LONG_HUNGER.setRegistryName(new ResourceLocation(MODID, "long_hunger"));
		STRONG_HUNGER.setRegistryName(new ResourceLocation(MODID, "strong_hunger"));
		SATURATION.setRegistryName(new ResourceLocation(MODID, "saturation"));
		STRONG_SATURATION.setRegistryName(new ResourceLocation(MODID, "strong_saturation"));
		WITHER.setRegistryName(new ResourceLocation(MODID, "wither"));
		LONG_WITHER.setRegistryName(new ResourceLocation(MODID, "long_wither"));
		STRONG_WITHER.setRegistryName(new ResourceLocation(MODID, "strong_wither"));
		BLINDNESS.setRegistryName(new ResourceLocation(MODID, "blindness"));
		LONG_BLINDNESS.setRegistryName(new ResourceLocation(MODID, "long_blindness"));
		ABSORPTION.setRegistryName(new ResourceLocation(MODID, "absorption"));
		LONG_ABSORPTION.setRegistryName(new ResourceLocation(MODID, "long_absorption"));
		STRONG_ABSORPTION.setRegistryName(new ResourceLocation(MODID, "strong_absorption"));
		GLOWING.setRegistryName(new ResourceLocation(MODID, "glowing"));
		LONG_GLOWING.setRegistryName(new ResourceLocation(MODID, "long_glowing"));

		event.getRegistry().register(HUNGER);
		event.getRegistry().register(LONG_HUNGER);
		event.getRegistry().register(STRONG_HUNGER);
		event.getRegistry().register(SATURATION);
		event.getRegistry().register(STRONG_SATURATION);
		event.getRegistry().register(WITHER);
		event.getRegistry().register(LONG_WITHER);
		event.getRegistry().register(STRONG_WITHER);
		event.getRegistry().register(BLINDNESS);
		event.getRegistry().register(LONG_BLINDNESS);
		event.getRegistry().register(ABSORPTION);
		event.getRegistry().register(LONG_ABSORPTION);
		event.getRegistry().register(STRONG_ABSORPTION);
		event.getRegistry().register(GLOWING);
		event.getRegistry().register(LONG_GLOWING);
	}
}
