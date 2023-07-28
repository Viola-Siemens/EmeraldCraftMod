package com.hexagram2021.emeraldcraft.common.dispenser;

import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.ECChestBoat;
import com.hexagram2021.emeraldcraft.common.entities.IECBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ECBoatDispenseItemBehaviour extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
	private final ECBoat.ECBoatType type;
	private final boolean isChestBoat;

	@SuppressWarnings("unused")
	public ECBoatDispenseItemBehaviour(ECBoat.ECBoatType type) {
		this(type, false);
	}

	public ECBoatDispenseItemBehaviour(ECBoat.ECBoatType type, boolean withChest) {
		this.type = type;
		this.isChestBoat = withChest;
	}

	@Override
	public ItemStack execute(BlockSource block, ItemStack itemStack) {
		Direction direction = block.getBlockState().getValue(DispenserBlock.FACING);
		Level level = block.getLevel();
		double d0 = block.x() + (double)((float)direction.getStepX() * 1.125F);
		double d1 = block.y() + (double)((float)direction.getStepY() * 1.125F);
		double d2 = block.z() + (double)((float)direction.getStepZ() * 1.125F);
		BlockPos blockpos = block.getPos().relative(direction);

		IECBoat boat = isChestBoat ? new ECChestBoat(level, d0, d1, d2) : new ECBoat(level, d0, d1, d2);
		boat.setECBoatType(this.type);
		Entity boatEntity = (Entity)boat;
		boatEntity.setYRot(direction.toYRot());

		double d3;
		if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
			d3 = 1.0D;
		} else {
			if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
				return this.defaultDispenseItemBehavior.dispense(block, itemStack);
			}

			d3 = 0.0D;
		}

		boatEntity.setPos(d0, d1 + d3, d2);
		level.addFreshEntity(boatEntity);
		itemStack.shrink(1);
		return itemStack;
	}

	@Override
	protected void playSound(BlockSource block) {
		block.getLevel().levelEvent(1000, block.getPos(), 0);
	}
}
