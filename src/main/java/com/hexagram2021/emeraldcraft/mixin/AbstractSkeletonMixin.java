package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public class AbstractSkeletonMixin {
	@Inject(method = "registerGoals", at = @At(value = "TAIL"))
	protected void registerNetherVillagerGoals(CallbackInfo info) {
		((AbstractSkeletonEntity)(Object)this).targetSelector.addGoal(4, new NearestAttackableTargetGoal<>((AbstractSkeletonEntity)(Object)this, NetherPigmanEntity.class, true));
		((AbstractSkeletonEntity)(Object)this).targetSelector.addGoal(4, new NearestAttackableTargetGoal<>((AbstractSkeletonEntity)(Object)this, NetherLambmanEntity.class, true));
	}
}
