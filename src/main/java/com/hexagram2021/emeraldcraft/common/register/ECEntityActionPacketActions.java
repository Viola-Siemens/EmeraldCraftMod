package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class ECEntityActionPacketActions {
	public static ServerboundPlayerCommandPacket.Action RIDING_FLY;

	static {
		ECLogger.debug("Add riding_fly action to actions. Total %d actions.".formatted(ServerboundPlayerCommandPacket.Action.values().length));
	}
}
