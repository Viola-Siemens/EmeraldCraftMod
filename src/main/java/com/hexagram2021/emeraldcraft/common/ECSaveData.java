package com.hexagram2021.emeraldcraft.common;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public CompoundTag save(@Nonnull CompoundTag nbt)
	{
		ListTag dimensionList = new ListTag();

		//nbt.put("", dimensionList);


		ListTag receivedShaderList = new ListTag();
		//nbt.put("", receivedShaderList);

		return nbt;
	}


	public static void markInstanceDirty()
	{
		if(INSTANCE!=null)
			INSTANCE.setDirty();
	}

	public static void setInstance(ECSaveData in)
	{
		INSTANCE = in;
	}
}
