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

public class HerringEntity extends AbstractSchoolingFish {
	public HerringEntity(EntityType<? extends HerringEntity> type, Level level) {
		super(type, level);
	}

	@Override @NotNull
	public ItemStack getBucketItemStack() {
		return new ItemStack(ECItems.HERRING_BUCKET);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ECSounds.HERRING_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.HERRING_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ECSounds.HERRING_HURT;
	}

	@Override @NotNull
	protected SoundEvent getFlopSound() {
		return ECSounds.HERRING_FLOP;
	}
}
