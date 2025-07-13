package cn.sh1rocu.emeraldcraft.util.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public class PlayerTickEvent {
    public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, listeners -> (player) -> {
        for (Start listener : listeners) {
            listener.start(player);
        }
    });

    public static final Event<End> END = EventFactory.createArrayBacked(End.class, listeners -> (player) -> {
        for (End listener : listeners) {
            listener.end(player);
        }
    });

    @FunctionalInterface
    public interface Start {
        void start(Player player);
    }

    @FunctionalInterface
    public interface End {
        void end(Player player);
    }
}
