package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.enchantments.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECEnchantments {
	public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(Registries.ENCHANTMENT, MODID);

	public static final DeferredHolder<Enchantment, Enchantment> VEIN_MINING = REGISTER.register(
			"vein_mining", () -> new VeinMiningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND)
	);

	public static final DeferredHolder<Enchantment, Enchantment> BANE_OF_MAMMALS = REGISTER.register(
			"bane_of_mammals", () -> new MammalDamageEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
