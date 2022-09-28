package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
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

	@Override @NotNull
	public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		return super.canSurvive(state, level, pos);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.is(Blocks.SOUL_SAND);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		int i = state.getValue(AGE);
		if (i < MAX_AGE && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(10) == 0)) {
			state = state.setValue(AGE, i + 1);
			level.setBlock(pos, state, Block.UPDATE_CLIENTS);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.NETHER;
	}

	@Override @NotNull
	public ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState blockState) {
		return new ItemStack(ECItems.WARPED_WART.asItem());
	}

	@Override @NotNull
	public Item asItem() {
		return ECItems.WARPED_WART.asItem();
	}
}
