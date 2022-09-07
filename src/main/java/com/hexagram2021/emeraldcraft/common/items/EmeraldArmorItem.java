package com.hexagram2021.emeraldcraft.common.items;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class EmeraldArmorItem extends ArmorItem {
	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

	private static final String name = "emerald";
	private static final int durabilityMultiplier = 12;
	private static final int[] slotProtections = new int[]{2, 5, 7, 3};
	private static final int enchantmentValue = 25;
	private static final SoundEvent sound = SoundEvents.ARMOR_EQUIP_GOLD;
	private static final float toughness = 0.0F;
	private static final float knockbackResistance = 0.0F;
	private static final LazyValue<Ingredient> repairIngredient = new LazyValue<>(() -> Ingredient.of(Items.EMERALD));

	public static IArmorMaterial mat = new EmeraldArmorMaterial();

	public EmeraldArmorItem(EquipmentSlotType type) {
		super(mat, type, new Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return MODID+":textures/models/armor_emerald"+(slot==EquipmentSlotType.LEGS?"_legs": "")+".png";
	}

	private static class EmeraldArmorMaterial implements IArmorMaterial {
		public int getDurabilityForSlot(EquipmentSlotType pSlot) {
			return HEALTH_PER_SLOT[pSlot.getIndex()] * durabilityMultiplier;
		}

		public int getDefenseForSlot(EquipmentSlotType pSlot) {
			return slotProtections[pSlot.getIndex()];
		}

		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		@Nonnull
		public SoundEvent getEquipSound() {
			return sound;
		}

		@Nonnull
		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}

		@Nonnull
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
