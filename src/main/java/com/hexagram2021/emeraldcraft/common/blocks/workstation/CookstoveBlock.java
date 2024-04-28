package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CookstoveBlock extends BaseEntityBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Properties.of()
			.requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F);

	public CookstoveBlock(Properties properties) {
		super(properties);
	}

	//TODO
	@Override @Nullable
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return null;
	}
}
