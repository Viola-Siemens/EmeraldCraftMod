package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.ECContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

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

	public static void init(ECContent.RegisterConsumer<Potion> register) {
		register.accept(new ResourceLocation(MODID, "hunger"), HUNGER);
		register.accept(new ResourceLocation(MODID, "long_hunger"), LONG_HUNGER);
		register.accept(new ResourceLocation(MODID, "strong_hunger"), STRONG_HUNGER);
		register.accept(new ResourceLocation(MODID, "saturation"), SATURATION);
		register.accept(new ResourceLocation(MODID, "strong_saturation"), STRONG_SATURATION);
		register.accept(new ResourceLocation(MODID, "wither"), WITHER);
		register.accept(new ResourceLocation(MODID, "long_wither"), LONG_WITHER);
		register.accept(new ResourceLocation(MODID, "strong_wither"), STRONG_WITHER);
		register.accept(new ResourceLocation(MODID, "blindness"), BLINDNESS);
		register.accept(new ResourceLocation(MODID, "long_blindness"), LONG_BLINDNESS);
		register.accept(new ResourceLocation(MODID, "absorption"), ABSORPTION);
		register.accept(new ResourceLocation(MODID, "long_absorption"), LONG_ABSORPTION);
		register.accept(new ResourceLocation(MODID, "strong_absorption"), STRONG_ABSORPTION);
		register.accept(new ResourceLocation(MODID, "glowing"), GLOWING);
		register.accept(new ResourceLocation(MODID, "long_glowing"), LONG_GLOWING);
	}
}
