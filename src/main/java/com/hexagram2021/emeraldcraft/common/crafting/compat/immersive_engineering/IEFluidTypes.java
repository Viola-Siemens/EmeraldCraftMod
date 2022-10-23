package com.hexagram2021.emeraldcraft.common.crafting.compat.immersive_engineering;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;

public enum IEFluidTypes implements FluidType {
	melted_aluminum {
		@Override
		public int getGUIID() {
			return 15;
		}
	},
	melted_lead {
		@Override
		public int getGUIID() {
			return 14;
		}
	},
	melted_silver {
		@Override
		public int getGUIID() {
			return 7;
		}
	},
	melted_nickel {
		@Override
		public int getGUIID() {
			return 11;
		}
	},
	melted_uranium {
		@Override
		public int getGUIID() {
			return 10;
		}
	};

	public static void init() {
		FluidTypes.addFluidType(melted_aluminum, ECItems.IECompatItems.MELTED_ALUMINUM_BUCKET, ECItems.IECompatItems.MELTED_ALUMINUM_BUCKET.getId());
		FluidTypes.addFluidType(melted_lead, ECItems.IECompatItems.MELTED_LEAD_BUCKET, ECItems.IECompatItems.MELTED_LEAD_BUCKET.getId());
		FluidTypes.addFluidType(melted_silver, ECItems.IECompatItems.MELTED_SILVER_BUCKET, ECItems.IECompatItems.MELTED_SILVER_BUCKET.getId());
		FluidTypes.addFluidType(melted_nickel, ECItems.IECompatItems.MELTED_NICKEL_BUCKET, ECItems.IECompatItems.MELTED_NICKEL_BUCKET.getId());
		FluidTypes.addFluidType(melted_uranium, ECItems.IECompatItems.MELTED_URANIUM_BUCKET, ECItems.IECompatItems.MELTED_URANIUM_BUCKET.getId());
	}
}
