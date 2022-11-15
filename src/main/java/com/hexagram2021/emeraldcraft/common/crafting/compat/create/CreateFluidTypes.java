package com.hexagram2021.emeraldcraft.common.crafting.compat.create;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;

public enum CreateFluidTypes implements FluidType {
	melted_zinc {
		@Override
		public int getGUIID() {
			return 7;
		}
	};

	public static void init() {
		FluidTypes.addFluidType(melted_zinc, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET.getId());
	}
}
