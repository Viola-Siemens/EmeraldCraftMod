package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.blocks.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("DataFlowIssue")
public final class ECBlockEntity {
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GlassKilnBlockEntity>> GLASS_KILN = REGISTER.register(
			"glass_kiln", () -> new BlockEntityType<>(
					GlassKilnBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.GLASS_KILN.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MineralTableBlockEntity>> MINERAL_TABLE = REGISTER.register(
			"mineral_table", () -> new BlockEntityType<>(
					MineralTableBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MINERAL_TABLE.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContinuousMinerBlockEntity>> CONTINUOUS_MINER = REGISTER.register(
			"continuous_miner", () -> new BlockEntityType<>(
					ContinuousMinerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.CONTINUOUS_MINER.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IceMakerBlockEntity>> ICE_MAKER = REGISTER.register(
			"ice_maker", () -> new BlockEntityType<>(
					IceMakerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.ICE_MAKER.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MelterBlockEntity>> MELTER = REGISTER.register(
			"melter", () -> new BlockEntityType<>(
					MelterBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MELTER.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RabbleFurnaceBlockEntity>> RABBLE_FURNACE = REGISTER.register(
			"rabble_furnace", () -> new BlockEntityType<>(
					RabbleFurnaceBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.RABBLE_FURNACE.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MeatGrinderBlockEntity>> MEAT_GRINDER = REGISTER.register(
			"meat_grinder", () -> new BlockEntityType<>(
					MeatGrinderBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MEAT_GRINDER.get()), null
			)
	);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CookstoveBlockEntity>> COOKSTOVE = REGISTER.register(
			"cookstove", () -> new BlockEntityType<>(
					CookstoveBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.COOKSTOVE.get()), null
			)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}