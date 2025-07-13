package com.hexagram2021.emeraldcraft.common.items.armors;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;

public class EmeraldArmorItem extends ArmorItem {

    private static final String name = "emerald";
    private static final int durabilityMultiplier = 12;
    private static final int enchantmentValue = 25;
    private static final float toughness = 0.0F;
    private static final float knockbackResistance = 0.0F;

    @SuppressWarnings("deprecation")
    private static final LazyLoadedValue<Ingredient> repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(Items.EMERALD));

    public static final ArmorMaterial mat = new EmeraldArmorMaterial();

    public EmeraldArmorItem(ArmorItem.Type type) {
        super(mat, type, new Properties().stacksTo(1));
    }

    private static class EmeraldArmorMaterial implements ArmorMaterial {
        private static final EnumMap<Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 13);
            map.put(ArmorItem.Type.LEGGINGS, 15);
            map.put(ArmorItem.Type.CHESTPLATE, 16);
            map.put(ArmorItem.Type.HELMET, 11);
        });
        private static final EnumMap<Type, Integer> PROTECTIONS_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 5);
            map.put(ArmorItem.Type.CHESTPLATE, 7);
            map.put(ArmorItem.Type.HELMET, 3);
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
            return EmeraldCraft.MODID + ":" + name;
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
