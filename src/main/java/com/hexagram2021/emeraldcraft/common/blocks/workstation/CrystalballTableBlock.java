package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Supplier;

public class CrystalballTableBlock extends Block {
	public static final IntegerProperty EXP_COUNT = ECProperties.EXP_COUNT;

	private static final Material AMETHYST = (new Material.Builder(MaterialColor.COLOR_PURPLE)).build();

	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(AMETHYST).sound(SoundType.GLASS).strength(3.5F).randomTicks().lightLevel((bs) -> 7);

	public CrystalballTableBlock(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(EXP_COUNT, 0));
	}

	@Override @Nonnull
	public ActionResultType use(@Nonnull BlockState blockState, World level, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity player,
								@Nonnull Hand interactionHand, @Nonnull BlockRayTraceResult blockHitResult) {
		if (level.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		int i = blockState.getValue(EXP_COUNT);
		if(i != 0) {
			level.addFreshEntity(new ExperienceOrbEntity(level, player.getX(), player.getY() + 0.5D, player.getZ(), i * 20));
			level.setBlock(blockPos, blockState.setValue(EXP_COUNT, 0), 2);
		}
		return ActionResultType.CONSUME;
	}

	@Override
	public void randomTick(@Nonnull BlockState blockState, ServerWorld level, @Nonnull BlockPos blockPos, @Nonnull Random random) {
		if(level.random.nextInt(250) == 0) {
			int i = blockState.getValue(EXP_COUNT);
			if(i < 15) {
				level.playSound(null, blockPos, ECSounds.VILLAGER_WORK_ASTROLOGIST, SoundCategory.BLOCKS, 0.6F, 0.9F + random.nextFloat() * 0.2F);
				level.setBlock(blockPos, blockState.setValue(EXP_COUNT, i + 1), 2);
			}
		}
	}

	@Override @Nonnull
	public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean useShapeForLightOcclusion(@Nonnull BlockState blockState) {
		return true;
	}

	@Override @Nonnull
	public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> definition) {
		definition.add(EXP_COUNT);
	}
}
