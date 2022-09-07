package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Random;
import java.util.function.Supplier;

public class WarpedWartBlock extends BushBlock {
	public static final int MAX_AGE = 3;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
	};

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.PLANT)
			.sound(SoundType.NETHER_WART)
			.noCollission()
			.strength(0)
			.randomTicks();

	public WarpedWartBlock(Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull IWorldReader world, @Nonnull BlockPos pos) {
		return super.canSurvive(state, world, pos);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return state.is(Blocks.SOUL_SAND);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public void randomTick(BlockState state, @Nonnull ServerWorld level, @Nonnull BlockPos pos, @Nonnull Random random) {
		int i = state.getValue(AGE);
		if (i < MAX_AGE && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(10) == 0)) {
			state = state.setValue(AGE, i + 1);
			level.setBlock(pos, state, 2);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.NETHER;
	}

	@Nonnull
	@Override
	public ItemStack getCloneItemStack(IBlockReader p_54973_, BlockPos p_54974_, BlockState p_54975_) {
		return new ItemStack(ECItems.WARPED_WART.asItem());
	}

	@Nonnull
	@Override
	public Item asItem() {
		return ECItems.WARPED_WART.asItem();
	}
}
