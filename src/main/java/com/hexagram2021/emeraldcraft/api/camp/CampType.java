package com.hexagram2021.emeraldcraft.api.camp;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public interface CampType extends StringRepresentable {
	@Override @NotNull
	default String getSerializedName() {
		return this.toString();
	}

	@Override
	String toString();
}