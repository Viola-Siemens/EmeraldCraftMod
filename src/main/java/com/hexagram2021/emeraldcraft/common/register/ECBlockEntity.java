package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.blocks.entity.GlassKilnBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.entity.MineralTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECBlockEntity {
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

	public static final RegistryObject<BlockEntityType<GlassKilnBlockEntity>> GLASS_KILN = REGISTER.register(
			"glass_kiln", () -> new BlockEntityType<>(
					GlassKilnBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.GLASS_KILN.get()), null
			)
	);
	public static final RegistryObject<BlockEntityType<MineralTableBlockEntity>> MINERAL_TABLE = REGISTER.register(
			"mineral_table", () -> new BlockEntityType<>(
					MineralTableBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MINERAL_TABLE.get()), null
			)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}