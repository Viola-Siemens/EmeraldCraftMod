package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.enchantments.MammalDamageEnchantment;
import com.hexagram2021.emeraldcraft.common.enchantments.VeinMiningEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

@SuppressWarnings("unused")
public class ECEnchantments {
    public static final Enchantment VEIN_MINING = register(
            "vein_mining", new VeinMiningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND)
    );

    public static final Enchantment BANE_OF_MAMMALS = register(
            "bane_of_mammals", new MammalDamageEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND)
    );

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT, EmeraldCraft.id(name), enchantment);
    }

    public static void init() {
    }
}
