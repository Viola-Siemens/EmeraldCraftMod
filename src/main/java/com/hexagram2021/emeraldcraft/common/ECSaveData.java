package com.hexagram2021.emeraldcraft.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;

public class ECSaveData extends SavedData {
	private static ECSaveData INSTANCE;
	public static final String dataName = "EmeraldCraft-SaveData";

	public ECSaveData()
	{
		super();
	}

	public ECSaveData(CompoundTag nbt) {
		this();
	}

	@Nonnull
	@Override
	public CompoundTag save(@Nonnull CompoundTag nbt) {
		ListTag dimensionList = new ListTag();

		//nbt.put("", dimensionList);


		ListTag receivedShaderList = new ListTag();
		//nbt.put("", receivedShaderList);

		return nbt;
	}


	public static void markInstanceDirty() {
		if(INSTANCE!=null)
			INSTANCE.setDirty();
	}

	public static void setInstance(ECSaveData in)
	{
		INSTANCE = in;
	}
}
