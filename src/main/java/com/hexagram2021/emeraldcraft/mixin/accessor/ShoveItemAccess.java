package com.hexagram2021.emeraldcraft.mixin.accessor;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShovelItem.class)
public interface ShoveItemAccess {
    @Accessor("FLATTENABLES")
    static Map<Block, BlockState> getFlattenables() {
        throw new AssertionError();
    }
}
