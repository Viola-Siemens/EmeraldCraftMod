package com.hexagram2021.emeraldcraft.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
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

	@SuppressWarnings("DataFlowIssue")
	public static Factory<ECSaveData> factory() {
		return new SavedData.Factory<>(ECSaveData::new, ECSaveData::new, null);
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		return nbt;
	}

	public static void markInstanceDirty() {
		if(INSTANCE != null) {
			INSTANCE.setDirty();
		}
	}

	public static void setInstance(ECSaveData in) {
		INSTANCE = in;
	}
}
