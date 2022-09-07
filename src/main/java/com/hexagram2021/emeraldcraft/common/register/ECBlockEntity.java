package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.blocks.entity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECBlockEntity {
	public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

	public static final RegistryObject<TileEntityType<GlassKilnBlockEntity>> GLASS_KILN = REGISTER.register(
			"glass_kiln", () -> new TileEntityType<>(
					GlassKilnBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.GLASS_KILN.get()), null
			)
	);
	public static final RegistryObject<TileEntityType<MineralTableBlockEntity>> MINERAL_TABLE = REGISTER.register(
			"mineral_table", () -> new TileEntityType<>(
					MineralTableBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MINERAL_TABLE.get()), null
			)
	);
	public static final RegistryObject<TileEntityType<ContinuousMinerBlockEntity>> CONTINUOUS_MINER = REGISTER.register(
			"continuous_miner", () -> new TileEntityType<>(
					ContinuousMinerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.CONTINUOUS_MINER.get()), null
			)
	);
	public static final RegistryObject<TileEntityType<IceMakerBlockEntity>> ICE_MAKER = REGISTER.register(
			"ice_maker", () -> new TileEntityType<>(
					IceMakerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.ICE_MAKER.get()), null
			)
	);
	public static final RegistryObject<TileEntityType<MelterBlockEntity>> MELTER = REGISTER.register(
			"melter", () -> new TileEntityType<>(
					MelterBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MELTER.get()), null
			)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}