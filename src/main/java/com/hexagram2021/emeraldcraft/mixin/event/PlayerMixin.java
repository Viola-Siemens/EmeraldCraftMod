package com.hexagram2021.emeraldcraft.mixin.event;

import cn.sh1rocu.emeraldcraft.util.api.event.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void port_lib$playerStartTickEvent(CallbackInfo ci) {
        PlayerTickEvent.START.invoker().start((Player) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void port_lib$playerEndTickEvent(CallbackInfo ci) {
        PlayerTickEvent.END.invoker().end((Player) (Object) this);
    }
}