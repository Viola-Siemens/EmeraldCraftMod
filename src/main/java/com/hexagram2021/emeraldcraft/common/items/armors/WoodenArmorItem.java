package com.hexagram2021.emeraldcraft.common.items.armors;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class WoodenArmorItem extends ArmorItem {
	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

	private static final String name = "wooden";
	private static final int durabilityMultiplier = 4;
	private static final int[] slotProtections = new int[]{0, 1, 2, 1};
	private static final int enchantmentValue = 5;
	private static final float toughness = 0.5F;
	private static final float knockbackResistance = 0.0F;

	@SuppressWarnings("deprecation")
	private static final LazyLoadedValue<Ingredient> repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(ItemTags.PLANKS));

	public static final ArmorMaterial mat = new WoodenArmorMaterial();

	public WoodenArmorItem(EquipmentSlot type) {
		super(mat, type, new Properties().stacksTo(1));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return MODID+":textures/models/armor_wooden"+(slot==EquipmentSlot.LEGS?"_legs": "")+".png";
	}

	private static class WoodenArmorMaterial implements ArmorMaterial {
		@Override
		public int getDurabilityForSlot(EquipmentSlot pSlot) {
			return HEALTH_PER_SLOT[pSlot.getIndex()] * durabilityMultiplier;
		}
		
		@Override
		public int getDefenseForSlot(EquipmentSlot pSlot) {
			return slotProtections[pSlot.getIndex()];
		}
		
		@Override
		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		@Override @NotNull
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_LEATHER;
		}
		
		@Override @NotNull
		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}
		
		@Override @NotNull
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
