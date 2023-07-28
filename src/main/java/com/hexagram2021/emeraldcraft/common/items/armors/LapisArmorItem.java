package com.hexagram2021.emeraldcraft.common.items.armors;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class LapisArmorItem extends ArmorItem {
	private static final String name = "lapis";
	private static final int durabilityMultiplier = 6;
	private static final int enchantmentValue = 40;
	private static final float toughness = 0.0F;
	private static final float knockbackResistance = 0.0F;
	
	@SuppressWarnings("deprecation")
	private static final LazyLoadedValue<Ingredient> repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(Items.LAPIS_LAZULI));

	public static final ArmorMaterial mat = new LapisArmorMaterial();

	public LapisArmorItem(ArmorItem.Type type) {
		super(mat, type, new Properties().stacksTo(1));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return MODID+":textures/models/armor_lapis"+(slot==EquipmentSlot.LEGS?"_legs": "")+".png";
	}

	private static class LapisArmorMaterial implements ArmorMaterial {
		private static final EnumMap<Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 13);
			map.put(ArmorItem.Type.LEGGINGS, 15);
			map.put(ArmorItem.Type.CHESTPLATE, 16);
			map.put(ArmorItem.Type.HELMET, 11);
		});
		private static final EnumMap<Type, Integer> PROTECTIONS_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 1);
			map.put(ArmorItem.Type.LEGGINGS, 3);
			map.put(ArmorItem.Type.CHESTPLATE, 4);
			map.put(ArmorItem.Type.HELMET, 1);
		});

		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return HEALTH_FUNCTION_FOR_TYPE.get(type) * durabilityMultiplier;
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return PROTECTIONS_FOR_TYPE.get(type);
		}
		
		@Override
		public int getEnchantmentValue() {
			return enchantmentValue;
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_GOLD;
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public float getToughness() {
			return toughness;
		}
		
		@Override
		public float getKnockbackResistance() {
			return knockbackResistance;
		}
	}
}
