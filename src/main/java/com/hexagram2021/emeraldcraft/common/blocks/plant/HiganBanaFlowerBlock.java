package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.potion.Effect;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class HiganBanaFlowerBlock extends FlowerBlock {
	public static final BooleanProperty LEAF = ECProperties.LEAF;

	public HiganBanaFlowerBlock(Effect effect, int duration, Properties props) {
		super(effect, duration, props);
		this.registerDefaultState(this.stateDefinition.any().setValue(LEAF, true));
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LEAF);
	}

	@Override
	public void randomTick(@Nonnull BlockState state, @Nonnull ServerWorld level, @Nonnull BlockPos pos, @Nonnull Random random) {
		if(level.dimension() == World.OVERWORLD) {
			level.setBlock(pos, state.setValue(LEAF, false), 3);
			level.playSound(null, pos, ECSounds.HIGAN_BANA_DROP_LEAVES, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override @Nullable
	public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
		if(context.getLevel().dimension() == World.OVERWORLD) {
			return this.defaultBlockState().setValue(LEAF, false);
		}
		return this.defaultBlockState();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LEAF);
	}

	@Override
	protected boolean mayPlaceOn(@Nonnull BlockState blockState, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos) {
		return blockState.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(blockState, level, blockPos);
	}
}
