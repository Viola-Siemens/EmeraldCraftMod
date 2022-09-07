/*
 * Copyright by BluSunrize
 */

package com.hexagram2021.emeraldcraft.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CommonProxy {
	public void onWorldLoad() { }

	public void resetManual() { }

	public void handleTileSound(SoundEvent soundEvent, TileEntity tile, boolean tileActive, float volume, float pitch) { }

	public void stopTileSound(String soundName, TileEntity tile) { }

	public World getClientWorld()
	{
		return null;
	}

	public PlayerEntity getClientPlayer()
	{
		return null;
	}

	public void reInitGui() { }

	public void clearRenderCaches() { }

	public void openManual() { }

	public void openTileScreen(String guiId, TileEntity tileEntity) { }
}
