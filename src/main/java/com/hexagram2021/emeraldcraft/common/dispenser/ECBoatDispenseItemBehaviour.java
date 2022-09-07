package com.hexagram2021.emeraldcraft.common.dispenser;

import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ECBoatDispenseItemBehaviour extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
	private final ECBoat.ECBoatType type;

	public ECBoatDispenseItemBehaviour(ECBoat.ECBoatType type) {
		this.type = type;
	}

	@Override @Nonnull
	public ItemStack execute(IBlockSource block, @Nonnull ItemStack itemStack) {
		Direction direction = block.getBlockState().getValue(DispenserBlock.FACING);
		World level = block.getLevel();
		double d0 = block.x() + (double)((float)direction.getStepX() * 1.125F);
		double d1 = block.y() + (double)((float)direction.getStepY() * 1.125F);
		double d2 = block.z() + (double)((float)direction.getStepZ() * 1.125F);
		BlockPos blockpos = block.getPos().relative(direction);
		double d3;
		if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
			d3 = 1.0D;
		} else {
			if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
				return this.defaultDispenseItemBehavior.dispense(block, itemStack);
			}

			d3 = 0.0D;
		}

		ECBoat boat = new ECBoat(level, d0, d1 + d3, d2);
		boat.setECBoatType(this.type);
		boat.yRot = direction.toYRot();
		level.addFreshEntity(boat);
		itemStack.shrink(1);
		return itemStack;
	}

	@Override
	protected void playSound(IBlockSource block) {
		block.getLevel().levelEvent(1000, block.getPos(), 0);
	}
}
