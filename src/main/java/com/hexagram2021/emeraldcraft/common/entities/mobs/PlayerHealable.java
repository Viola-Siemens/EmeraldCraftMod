package com.hexagram2021.emeraldcraft.common.entities.mobs;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public interface PlayerHealable {
	boolean isPlayerHealed();
	void setPlayerHealed(boolean healed);

	@NotNull
	UUID getHealedPlayer();
	void setHealedPlayer(@Nullable UUID player);
}
