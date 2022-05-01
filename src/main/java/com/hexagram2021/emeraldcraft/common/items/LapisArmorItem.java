package com.hexagram2021.emeraldcraft.common.items;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
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

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class LapisArmorItem extends ArmorItem {
	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

	private static final String name = "lapis";
	private static final int durabilityMultiplier = 6;
	private static final int[] slotProtections = new int[]{1, 3, 4, 1};
	private static final int enchantmentValue = 40;
	private static final SoundEvent sound = SoundEvents.ARMOR_EQUIP_GOLD;
	private static final float toughness = 0.0F;
	private static final float knockbackResistance = 0.0F;
	private static final LazyLoadedValue<Ingredient> repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(Items.LAPIS_LAZULI));

	public static ArmorMaterial mat = new LapisArmorMaterial();

	public LapisArmorItem(EquipmentSlot type) {
		super(mat, type, new Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return MODID+":textures/models/armor_lapis"+(slot==EquipmentSlot.LEGS?"_legs": "")+".png";
	}

	private static class LapisArmorMaterial implements ArmorMaterial {
		public int getDurabilityForSlot(EquipmentSlot pSlot) {
			return HEALTH_PER_SLOT[pSlot.getIndex()] * durabilityMultiplier;
		}

		public int getDefenseForSlot(EquipmentSlot pSlot) {
			return slotProtections[pSlot.getIndex()];
		}

		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		public SoundEvent getEquipSound() {
			return sound;
		}

		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}

		public String getName() {
			return name;
		}

		public float getToughness() {
			return toughness;
		}

		public float getKnockbackResistance() {
			return knockbackResistance;
		}
	}
}
