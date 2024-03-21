package com.hexagram2021.emeraldcraft.common.crafting.compat.create;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;

import java.util.Locale;

public enum CreateFluidTypes implements FluidType {
	MELTED_ZINC {
		@Override
		public int getGuiId() {
			return 7;
		}
	};

	public static void init() {
		FluidTypes.addFluidType(MELTED_ZINC, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET.getId());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
