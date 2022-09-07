package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.world.pools.SwampVillagePools;
import net.minecraft.world.gen.feature.structure.VillagesPools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagesPools.class)
public class VillagePoolsMixin {
	@Inject(method = "bootstrap", at = @At(value = "TAIL"))
	private static void addSwampVillage(CallbackInfo info) {
		SwampVillagePools.bootstrap();
	}
}
