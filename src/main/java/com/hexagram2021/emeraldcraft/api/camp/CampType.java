package com.hexagram2021.emeraldcraft.api.camp;

import net.minecraft.util.StringRepresentable;

public interface CampType extends StringRepresentable {
	@Override
	default String getSerializedName() {
		return this.toString();
	}

	@Override
	String toString();

	static CampType[] values() {
		return CampTypes.ALL_CAMPS.toArray(new CampType[0]);
	}
}