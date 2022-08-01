package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.register.ECProperty;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;
import java.util.function.Supplier;

public class CrystalballTableBlock extends Block {
	public static final IntegerProperty EXP_COUNT = ECProperty.EXP_COUNT;

	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.AMETHYST).sound(SoundType.AMETHYST).strength(3.5F).randomTicks().lightLevel((bs) -> 7);

	public CrystalballTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(EXP_COUNT, 0));
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		int i = blockState.getValue(EXP_COUNT);
		if(i != 0) {
			level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY() + 0.5D, player.getZ(), i * 20));
			level.setBlock(blockPos, blockState.setValue(EXP_COUNT, 0), Block.UPDATE_CLIENTS);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, Random random) {
		if(level.random.nextInt(250) == 0) {
			int i = blockState.getValue(EXP_COUNT);
			if(i < 15) {
				level.playSound(null, blockPos, ECSounds.VILLAGER_WORK_ASTROLOGIST, SoundSource.BLOCKS, 0.6F, 0.9F + random.nextFloat() * 0.2F);
				level.setBlock(blockPos, blockState.setValue(EXP_COUNT, i + 1), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState blockState) {
		return true;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
		definition.add(EXP_COUNT);
	}
}
