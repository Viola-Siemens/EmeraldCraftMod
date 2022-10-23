package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.Convertible;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	public void defineZombifiedPiglinData(CallbackInfo ci) {
		ZombieEntity current = (ZombieEntity)(Object)this;
		if(current instanceof ZombifiedPiglinEntity) {
			current.getEntityData().define(Convertible.DATA_PIGLIN_CONVERTING_ID, false);
		}
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickConverting(CallbackInfo ci) {
		ZombieEntity current = (ZombieEntity)(Object)this;

		if(current instanceof ZombifiedPiglinEntity) {
			Convertible convertible = (Convertible)current;
			if (!current.level.isClientSide && current.isAlive() && convertible.isConverting()) {
				int i = convertible.getConversionProgress();
				convertible.decreaseConversionRemainTime(i);
				if (convertible.getConversionRemainTime() <= 0 &&
						ForgeEventFactory.canLivingConvert(current, EntityType.PIGLIN, convertible::setConversionRemainTime)) {
					convertible.finishConversion((ServerWorld) current.level);
				}
			}
		}
	}
}
