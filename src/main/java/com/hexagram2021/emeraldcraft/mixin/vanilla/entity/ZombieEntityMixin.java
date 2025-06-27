package com.hexagram2021.emeraldcraft.mixin.vanilla.entity;

import com.hexagram2021.emeraldcraft.common.entities.mobs.Convertible;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieEntityMixin {
	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	public void emeraldcraft$defineZombifiedPiglinData(CallbackInfo ci) {
		Zombie current = (Zombie)(Object)this;
		if(current instanceof ZombifiedPiglin zombifiedPiglin) {
			zombifiedPiglin.getEntityData().define(Convertible.Ids.DATA_PIGLIN_CONVERTING_ID, false);
		}
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void emeraldcraft$tickConverting(CallbackInfo ci) {
		Zombie current = (Zombie)(Object)this;

		if(current instanceof ZombifiedPiglin zombifiedPiglin) {
			Convertible convertible = (Convertible)zombifiedPiglin;
			if (!zombifiedPiglin.level().isClientSide && zombifiedPiglin.isAlive() && convertible.emeraldcraft$isConverting()) {
				int i = convertible.emeraldcraft$getConversionProgress();
				convertible.emeraldcraft$decreaseConversionRemainTime(i);
				if (convertible.emeraldcraft$getConversionRemainTime() <= 0 &&
						EventHooks.canLivingConvert(zombifiedPiglin, EntityType.PIGLIN, convertible::emeraldcraft$setConversionRemainTime)) {
					convertible.emeraldcraft$finishConversion((ServerLevel) zombifiedPiglin.level());
				}
			}
		}
	}
}
