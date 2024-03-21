package com.hexagram2021.emeraldcraft.common.entities.mobs;


import javax.annotation.Nullable;
import java.util.UUID;

public interface PlayerHealable {
	boolean emeraldcraft$isPlayerHealed();
	void emeraldcraft$setPlayerHealed(boolean healed);

	UUID emeraldcraft$getHealedPlayer();
	void emeraldcraft$setHealedPlayer(@Nullable UUID player);
}
