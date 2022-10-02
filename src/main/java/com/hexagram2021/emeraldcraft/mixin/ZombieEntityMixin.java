package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.Convertible;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieEntityMixin {
	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	public void defineZombifiedPiglinData(CallbackInfo ci) {
		Zombie current = (Zombie)(Object)this;
		if(current instanceof ZombifiedPiglin zombifiedPiglin) {
			zombifiedPiglin.getEntityData().define(Convertible.DATA_PIGLIN_CONVERTING_ID, false);
		}
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickConverting(CallbackInfo ci) {
		Zombie current = (Zombie)(Object)this;

		if(current instanceof ZombifiedPiglin zombifiedPiglin) {
			Convertible convertible = (Convertible)zombifiedPiglin;
			if (!zombifiedPiglin.level.isClientSide && zombifiedPiglin.isAlive() && convertible.isConverting()) {
				int i = convertible.getConversionProgress();
				convertible.decreaseConversionRemainTime(i);
				if (convertible.getConversionRemainTime() <= 0 &&
						ForgeEventFactory.canLivingConvert(zombifiedPiglin, EntityType.VILLAGER, convertible::setConversionRemainTime)) {
					convertible.finishConversion((ServerLevel) zombifiedPiglin.level);
				}
			}
		}
	}
}
