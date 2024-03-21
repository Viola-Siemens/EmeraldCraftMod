package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static com.hexagram2021.emeraldcraft.common.register.ECMapDecorationTypes.*;

@Mixin(MapDecoration.Type.class)
public class MapDecorationTypeMixin {
	@SuppressWarnings("unused")
	MapDecorationTypeMixin(String name, int ord, String decorationName, boolean renderedOnFrame, boolean trackCount) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}

	@SuppressWarnings("unused")
	MapDecorationTypeMixin(String name, int ord, String decorationName, boolean renderedOnFrame, int MapColor, boolean trackCount, boolean isExplorationMapElement) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}

	@Shadow @Final @Mutable
	private static MapDecoration.Type[] $VALUES;

	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;$VALUES:[Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;"))
	private static void emeraldcraft$injectEnum(CallbackInfo ci) {
		int ordinal = $VALUES.length;
		$VALUES = Arrays.copyOf($VALUES, ordinal + 2);

		SHELTER = $VALUES[ordinal] = (MapDecoration.Type)(Object)new MapDecorationTypeMixin("EMERALDCRAFT:SHELTER", ordinal, "emeraldcraft:shelter", true, 0xa81228, false, true);
		ENTRENCHMENT = $VALUES[ordinal + 1] = (MapDecoration.Type)(Object)new MapDecorationTypeMixin("EMERALDCRAFT:ENTRENCHMENT", ordinal + 1, "emeraldcraft:entrenchment", true, 0xd606d6, false, true);
	}
}
