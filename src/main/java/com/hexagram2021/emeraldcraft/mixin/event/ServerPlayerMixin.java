package com.hexagram2021.emeraldcraft.mixin.event;

import cn.sh1rocu.emeraldcraft.util.api.event.PlayerContainerEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "openMenu", at = @At(value = "INVOKE", target = "Ljava/util/OptionalInt;of(I)Ljava/util/OptionalInt;"))
    private void kilt$callContainerOpenEvent(MenuProvider menu, CallbackInfoReturnable<OptionalInt> cir) {
        PlayerContainerEvent.OPEN.invoker().open((ServerPlayer) (Object) this, this.containerMenu);
    }

    @Inject(method = "openHorseInventory", at = @At("TAIL"))
    private void kilt$callContainerOpenEvent(AbstractHorse horse, Container inventory, CallbackInfo ci) {
        PlayerContainerEvent.OPEN.invoker().open((ServerPlayer) (Object) this, this.containerMenu);
    }

    @Inject(method = "doCloseContainer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;transferState(Lnet/minecraft/world/inventory/AbstractContainerMenu;)V", shift = At.Shift.AFTER))
    private void kilt$callContainerCloseEvent(CallbackInfo ci) {
        PlayerContainerEvent.CLOSE.invoker().close((ServerPlayer) (Object) this, this.containerMenu);
    }
}