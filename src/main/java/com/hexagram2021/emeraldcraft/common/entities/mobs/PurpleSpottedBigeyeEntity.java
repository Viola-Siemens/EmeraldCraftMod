package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PurpleSpottedBigeyeEntity extends AbstractSchoolingFish {
	public PurpleSpottedBigeyeEntity(EntityType<? extends PurpleSpottedBigeyeEntity> type, Level level) {
		super(type, level);
	}

	@Override @NotNull
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
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ECSounds.BIGEYE_HURT;
	}

	@Override @NotNull
	protected SoundEvent getFlopSound() {
		return ECSounds.BIGEYE_FLOP;
	}
}
