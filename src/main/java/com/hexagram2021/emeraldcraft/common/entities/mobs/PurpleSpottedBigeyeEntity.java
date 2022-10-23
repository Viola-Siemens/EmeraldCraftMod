package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurpleSpottedBigeyeEntity extends AbstractFishEntity {
	public PurpleSpottedBigeyeEntity(EntityType<? extends PurpleSpottedBigeyeEntity> type, World level) {
		super(type, level);
	}

	@Override @Nonnull
	public ItemStack getBucketItemStack() {
		return new ItemStack(ECItems.BIGEYE_BUCKET);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ECSounds.BIGEYE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.BIGEYE_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
		return ECSounds.BIGEYE_HURT;
	}

	@Override @Nonnull
	protected SoundEvent getFlopSound() {
		return ECSounds.BIGEYE_FLOP;
	}
}
