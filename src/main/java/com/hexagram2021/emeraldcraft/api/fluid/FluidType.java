package com.hexagram2021.emeraldcraft.api.fluid;

public interface FluidType {
	int getGUIID();

	String toString();
	boolean equals(Object obj);
	int hashCode();

	default String getTranslationTag() {
		return "fluids.name." + this;
	}
}
