package com.hexagram2021.emeraldcraft.common.fluids;

import com.hexagram2021.emeraldcraft.common.register.ECFluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class Resin extends ECFluid {
    @Override
    public Fluid getSource() {
        return ECFluids.RESIN;
    }

    @Override
    public Fluid getFlowing() {
        return ECFluids.FLOWING_RESIN;
    }

    @Override
    public Item getBucket() {
        return ECFluids.RESIN_BUCKET.get();
    }

    public static class Flowing extends Resin {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Source extends Resin {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}
