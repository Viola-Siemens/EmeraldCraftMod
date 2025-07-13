package cn.sh1rocu.emeraldcraft.util.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PlayerContainerEvent {
    public static final Event<Open> OPEN = EventFactory.createArrayBacked(Open.class, listeners -> (player, container) -> {
        for (Open listener : listeners) {
            listener.open(player, container);
        }
    });

    public static final Event<Close> CLOSE = EventFactory.createArrayBacked(Close.class, listeners -> (player, container) -> {
        for (Close listener : listeners) {
            listener.close(player, container);
        }
    });

    @FunctionalInterface
    public interface Open {
        void open(ServerPlayer player, AbstractContainerMenu container);
    }

    @FunctionalInterface
    public interface Close {
        void close(ServerPlayer player, AbstractContainerMenu container);
    }
}
