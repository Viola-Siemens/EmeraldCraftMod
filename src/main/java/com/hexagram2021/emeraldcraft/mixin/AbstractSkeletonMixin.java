package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {
	@Inject(method = "registerGoals", at = @At(value = "TAIL"))
	protected void registerNetherVillagerGoals(CallbackInfo info) {
		((AbstractSkeleton)(Object)this).targetSelector.addGoal(4, new NearestAttackableTargetGoal<>((AbstractSkeleton)(Object)this, NetherPigmanEntity.class, true));
		((AbstractSkeleton)(Object)this).targetSelector.addGoal(4, new NearestAttackableTargetGoal<>((AbstractSkeleton)(Object)this, NetherLambmanEntity.class, true));
	}
}
