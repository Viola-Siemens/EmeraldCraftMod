package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.PlantType;

import java.util.function.Supplier;

public class WarpedWartBlock extends NetherWartBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of().mapColor(MapColor.COLOR_CYAN)
			.noCollission().randomTicks().sound(SoundType.NETHER_WART).pushReaction(PushReaction.DESTROY);

	public WarpedWartBlock(Properties props) {
		super(props);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.NETHER;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState blockState) {
		return new ItemStack(ECItems.WARPED_WART.asItem());
	}
}
