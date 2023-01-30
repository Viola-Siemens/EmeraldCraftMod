package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
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

public class WarpedWartBlock extends NetherWartBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.PLANT)
			.sound(SoundType.NETHER_WART)
			.noCollission()
			.strength(0)
			.randomTicks();

	public WarpedWartBlock(Properties props) {
		super(props);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.NETHER;
	}

	@Override @NotNull
	public ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState blockState) {
		return new ItemStack(ECItems.WARPED_WART.asItem());
	}
}
