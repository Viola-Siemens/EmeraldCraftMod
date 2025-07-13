package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.blocks.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("DataFlowIssue")
public final class ECBlockEntity {
    public static final BlockEntityType<GlassKilnBlockEntity> GLASS_KILN = register(
            "glass_kiln", new BlockEntityType<>(
                    GlassKilnBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.GLASS_KILN.get()), null
            )
    );
    public static final BlockEntityType<MineralTableBlockEntity> MINERAL_TABLE = register(
            "mineral_table", new BlockEntityType<>(
                    MineralTableBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MINERAL_TABLE.get()), null
            )
    );
    public static final BlockEntityType<ContinuousMinerBlockEntity> CONTINUOUS_MINER = register(
            "continuous_miner", new BlockEntityType<>(
                    ContinuousMinerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.CONTINUOUS_MINER.get()), null
            )
    );
    public static final BlockEntityType<IceMakerBlockEntity> ICE_MAKER = register(
            "ice_maker", new BlockEntityType<>(
                    IceMakerBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.ICE_MAKER.get()), null
            )
    );
    public static final BlockEntityType<MelterBlockEntity> MELTER = register(
            "melter", new BlockEntityType<>(
                    MelterBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MELTER.get()), null
            )
    );
    public static final BlockEntityType<RabbleFurnaceBlockEntity> RABBLE_FURNACE = register(
            "rabble_furnace", new BlockEntityType<>(
                    RabbleFurnaceBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.RABBLE_FURNACE.get()), null
            )
    );
    public static final BlockEntityType<MeatGrinderBlockEntity> MEAT_GRINDER = register(
            "meat_grinder", new BlockEntityType<>(
                    MeatGrinderBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.MEAT_GRINDER.get()), null
            )
    );
    public static final BlockEntityType<CookstoveBlockEntity> COOKSTOVE = register(
            "cookstove", new BlockEntityType<>(
                    CookstoveBlockEntity::new, ImmutableSet.of(ECBlocks.WorkStation.COOKSTOVE.get()), null
            )
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, EmeraldCraft.id(name), type);
    }

    public static void init() {
    }
}