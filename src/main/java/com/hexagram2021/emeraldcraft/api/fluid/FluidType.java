package com.hexagram2021.emeraldcraft.api.fluid;

import net.minecraft.util.StringRepresentable;

public interface FluidType extends StringRepresentable {
	int getGuiId();

	String toString();
	boolean equals(Object obj);
	int hashCode();

	default String getTranslationTag() {
		return "fluids.name." + this;
	}

	@Override
	default String getSerializedName() {
		return this.toString();
	}

	static FluidType[] values() {
		return FluidTypes.ALL_FLUID_TYPES.toArray(new FluidType[0]);
	}
}
