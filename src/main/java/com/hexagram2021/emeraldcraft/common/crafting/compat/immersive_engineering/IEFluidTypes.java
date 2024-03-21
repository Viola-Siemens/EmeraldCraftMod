package com.hexagram2021.emeraldcraft.common.crafting.compat.immersive_engineering;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;

import java.util.Locale;

public enum IEFluidTypes implements FluidType {
	MELTED_ALUMINUM {
		@Override
		public int getGuiId() {
			return 15;
		}
	},
	MELTED_LEAD {
		@Override
		public int getGuiId() {
			return 14;
		}
	},
	MELTED_SILVER {
		@Override
		public int getGuiId() {
			return 7;
		}
	},
	MELTED_NICKEL {
		@Override
		public int getGuiId() {
			return 11;
		}
	},
	MELTED_URANIUM {
		@Override
		public int getGuiId() {
			return 10;
		}
	};

	public static void init() {
		FluidTypes.addFluidType(MELTED_ALUMINUM, ECItems.IECompatItems.MELTED_ALUMINUM_BUCKET, ECItems.IECompatItems.MELTED_ALUMINUM_BUCKET.getId());
		FluidTypes.addFluidType(MELTED_LEAD, ECItems.IECompatItems.MELTED_LEAD_BUCKET, ECItems.IECompatItems.MELTED_LEAD_BUCKET.getId());
		FluidTypes.addFluidType(MELTED_SILVER, ECItems.IECompatItems.MELTED_SILVER_BUCKET, ECItems.IECompatItems.MELTED_SILVER_BUCKET.getId());
		FluidTypes.addFluidType(MELTED_NICKEL, ECItems.IECompatItems.MELTED_NICKEL_BUCKET, ECItems.IECompatItems.MELTED_NICKEL_BUCKET.getId());
		FluidTypes.addFluidType(MELTED_URANIUM, ECItems.IECompatItems.MELTED_URANIUM_BUCKET, ECItems.IECompatItems.MELTED_URANIUM_BUCKET.getId());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
