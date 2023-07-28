package com.hexagram2021.emeraldcraft.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;

public class ECSaveData extends SavedData {
	@Nullable
	private static ECSaveData INSTANCE;
	public static final String dataName = "EmeraldCraft-SaveData";

	public ECSaveData() {
		super();
	}

	public ECSaveData(CompoundTag nbt) {
		this();
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag dimensionList = new ListTag();

		//nbt.put("", dimensionList);

		return nbt;
	}

	public static void markInstanceDirty() {
		if(INSTANCE!=null)
			INSTANCE.setDirty();
	}

	public static void setInstance(ECSaveData in) {
		INSTANCE = in;
	}
}
