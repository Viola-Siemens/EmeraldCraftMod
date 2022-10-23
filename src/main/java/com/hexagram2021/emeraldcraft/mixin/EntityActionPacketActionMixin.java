package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.network.play.client.CEntityActionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions.*;

@Mixin(CEntityActionPacket.Action.class)
public class EntityActionPacketActionMixin {
	EntityActionPacketActionMixin(String name, int ord) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}
	
	@Final
	@Shadow
	@Mutable
	private static CEntityActionPacket.Action[] $VALUES;
	
	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/play/client/CEntityActionPacket$Action;$VALUES:[Lnet/minecraft/network/play/client/CEntityActionPacket$Action;"))
	private static void injectEnum(CallbackInfo ci) {
		int ordinal = $VALUES.length;
		$VALUES = Arrays.copyOf($VALUES, ordinal + 1);
		RIDING_FLY = $VALUES[ordinal] = (CEntityActionPacket.Action)(Object)new EntityActionPacketActionMixin("RIDING_FLY", ordinal);
	}
}
