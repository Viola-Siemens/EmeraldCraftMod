package cn.sh1rocu.emeraldcraft.util.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class BlockToolModificationEvent {
    public static final Event<AxeStrip> AXE_STRIP = EventFactory.createArrayBacked(AxeStrip.class, listeners -> (context, pos, state) -> {
        for (AxeStrip listener : listeners) {
            listener.strip(context, pos, state);
        }
    });

    @FunctionalInterface
    public interface AxeStrip {
        void strip(UseOnContext context, BlockPos pos, BlockState state);
    }
}
