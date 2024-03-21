package com.hexagram2021.emeraldcraft.common.dispenser;

import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.ECChestBoat;
import com.hexagram2021.emeraldcraft.common.entities.IECBoat;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

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
		Direction direction = block.state().getValue(DispenserBlock.FACING);
		Level level = block.level();
		Vec3 center = block.center();
		double multiplier = 0.5625D + (double) ECEntities.BOAT.getWidth() / 2.0D;
		double x = center.x() + direction.getStepX() * multiplier;
		double y = center.y() + direction.getStepY() * 1.125D;
		double z = center.z() + direction.getStepZ() * multiplier;
		BlockPos blockpos = block.pos().relative(direction);

		IECBoat boat = isChestBoat ? new ECChestBoat(level, x, y, z) : new ECBoat(level, x, y, z);
		boat.setECBoatType(this.type);
		Entity boatEntity = (Entity)boat;
		boatEntity.setYRot(direction.toYRot());

		double deltaY;
		if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
			deltaY = 1.0D;
		} else {
			if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
				return this.defaultDispenseItemBehavior.dispense(block, itemStack);
			}

			deltaY = 0.0D;
		}

		boatEntity.setPos(x, y + deltaY, z);
		level.addFreshEntity(boatEntity);
		itemStack.shrink(1);
		return itemStack;
	}
}
