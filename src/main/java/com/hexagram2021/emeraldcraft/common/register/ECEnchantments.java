package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.enchantments.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECEnchantments {
	public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

	public static final RegistryObject<Enchantment> VEIN_MINING = REGISTER.register(
			"vein_mining", () -> new VeinMiningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND)
	);

	public static final RegistryObject<Enchantment> BANE_OF_MAMMALS = REGISTER.register(
			"bane_of_mammals", () -> new MammalDamageEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
