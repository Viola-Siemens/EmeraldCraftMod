package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class HiganBanaFlowerBlock extends FlowerBlock {
	public static final BooleanProperty LEAF = ECProperties.LEAF;

	public HiganBanaFlowerBlock(MobEffect effect, int duration, Properties props) {
		super(effect, duration, props);
		this.registerDefaultState(this.stateDefinition.any().setValue(LEAF, true));
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LEAF);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if(level.dimension() == Level.OVERWORLD) {
			level.setBlock(pos, state.setValue(LEAF, false), Block.UPDATE_ALL);
			level.playSound(null, pos, ECSounds.HIGAN_BANA_DROP_LEAVES, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override @Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if(context.getLevel().dimension() == Level.OVERWORLD) {
			return this.defaultBlockState().setValue(LEAF, false);
		}
		return this.defaultBlockState();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LEAF);
	}

	@Override
	protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos blockPos) {
		return blockState.is(BlockTags.DIRT) || blockState.is(Blocks.SOUL_SOIL);
	}
}
