package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SnakeheadEntity extends AbstractSchoolingFish {
	public SnakeheadEntity(EntityType<? extends SnakeheadEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public int getMaxSchoolSize() {
		return 5;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ECItems.SNAKEHEAD_BUCKET);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ECSounds.SNAKEHEAD_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.SNAKEHEAD_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return ECSounds.SNAKEHEAD_HURT;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return ECSounds.SNAKEHEAD_FLOP;
	}
}
