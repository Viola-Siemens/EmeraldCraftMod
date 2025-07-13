package cn.sh1rocu.emeraldcraft.util;

import com.hexagram2021.emeraldcraft.mixin.accessor.AxeItemAccess;
import com.hexagram2021.emeraldcraft.mixin.accessor.ShoveItemAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateUtil {
    public static BlockState getAxeStrippingState(BlockState originalState) {
        Block block = AxeItemAccess.getStrippedBlocks().get(originalState.getBlock());
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    public static BlockState getShovelPathingState(BlockState originalState) {
        return ShoveItemAccess.getFlattenables().get(originalState.getBlock());
    }
}
