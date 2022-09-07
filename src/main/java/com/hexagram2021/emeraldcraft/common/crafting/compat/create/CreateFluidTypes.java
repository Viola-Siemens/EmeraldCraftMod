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
	},
	melted_copper{
		@Override
		public int getGUIID() {
			return 5;
		}
	},;

	public static void init() {
		FluidTypes.addFluidType(melted_zinc, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET, ECItems.CreateCompatItems.MELTED_ZINC_BUCKET.getId());
		FluidTypes.addFluidType(melted_copper, ECItems.CreateCompatItems.MELTED_COPPER_BUCKET, ECItems.CreateCompatItems.MELTED_COPPER_BUCKET.getId());
	}
}
