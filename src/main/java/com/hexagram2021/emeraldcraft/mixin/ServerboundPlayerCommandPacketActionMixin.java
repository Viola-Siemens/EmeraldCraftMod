package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions.*;

@Mixin(ServerboundPlayerCommandPacket.Action.class)
public class ServerboundPlayerCommandPacketActionMixin {
	ServerboundPlayerCommandPacketActionMixin(String name, int ord) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}
	
	@Final
	@Shadow
	@Mutable
	private static ServerboundPlayerCommandPacket.Action[] $VALUES;
	
	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket$Action;$VALUES:[Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket$Action;"))
	private static void injectEnum(CallbackInfo ci) {
		int ordinal = $VALUES.length;
		$VALUES = Arrays.copyOf($VALUES, ordinal + 1);
		RIDING_FLY = $VALUES[ordinal] = (ServerboundPlayerCommandPacket.Action)(Object)new ServerboundPlayerCommandPacketActionMixin("RIDING_FLY", ordinal);
	}
}
