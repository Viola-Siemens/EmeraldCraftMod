package com.hexagram2021.emeraldcraft.common.entities.mobs;


import javax.annotation.Nullable;
import java.util.UUID;

public interface PlayerHealable {
	boolean isPlayerHealed();
	void setPlayerHealed(boolean healed);

	UUID getHealedPlayer();
	void setHealedPlayer(@Nullable UUID player);
}
