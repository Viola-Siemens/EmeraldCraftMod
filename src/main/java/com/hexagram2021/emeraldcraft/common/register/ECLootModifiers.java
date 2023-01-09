package com.hexagram2021.emeraldcraft.common.register;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECLootModifiers {
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTER = DeferredRegister.create(
			ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID
	);
	private static final RegistryObject<Codec<WardenDropModifier>> WARDEN_DROP_HEART = REGISTER.register(
			"warden_drop_heart", WardenDropModifier::factory
	);

	private ECLootModifiers() {}

	private static class WardenDropModifier extends LootModifier {
		protected WardenDropModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn);
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return WARDEN_DROP_HEART.get();
		}

		@Override @NotNull
		protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
			generatedLoot.add(new ItemStack(ECItems.WARDEN_HEART));
			return generatedLoot;
		}

		public static Codec<WardenDropModifier> factory() {
			return RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, WardenDropModifier::new));
		}
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
