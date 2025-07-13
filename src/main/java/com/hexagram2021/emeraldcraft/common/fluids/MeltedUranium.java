package com.hexagram2021.emeraldcraft.common.fluids;

import com.hexagram2021.emeraldcraft.common.register.ECFluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class MeltedUranium extends ECFluid {
    @Override
    public Fluid getSource() {
        return ECFluids.MELTED_URANIUM;
    }

    @Override
    public Fluid getFlowing() {
        return ECFluids.FLOWING_MELTED_URANIUM;
    }

    @Override
    public Item getBucket() {
        return ECFluids.MELTED_URANIUM_BUCKET.get();
    }

    public static class Flowing extends MeltedUranium {
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

    public static class Source extends MeltedUranium {
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
