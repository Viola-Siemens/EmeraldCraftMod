package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobEntityMixin {
	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	protected void tryCureMobsAndGetItems(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack itemstack = player.getItemInHand(hand);
		Mob current = (Mob)(Object)this;
		if (current instanceof ZombifiedPiglin zombifiedPiglin && ECCommonConfig.ENABLE_CURE_ZOMBIFIED_PIGLIN.get()) {
			if (itemstack.is(Items.GOLDEN_CARROT) && zombifiedPiglin.hasEffect(MobEffects.HUNGER)) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				if (!zombifiedPiglin.level.isClientSide) {
					((Convertible)zombifiedPiglin).startConverting(player.getUUID(), zombifiedPiglin.getRandom().nextInt(2401) + 3600);
				}

				cir.setReturnValue(InteractionResult.SUCCESS);
				cir.cancel();
				return;
			}
			cir.setReturnValue(InteractionResult.PASS);
			cir.cancel();
		} else if(current instanceof Phantom phantom && ECCommonConfig.ENABLE_CURE_PHANTOM.get()) {
			if(itemstack.is(ECItems.GOLDEN_PEACH.get()) && phantom.hasEffect(MobEffects.GLOWING)) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				if (!phantom.level.isClientSide) {
					((Convertible)phantom).startConverting(player.getUUID(), phantom.getRandom().nextInt(2401) + 3600);
				}

				cir.setReturnValue(InteractionResult.SUCCESS);
				cir.cancel();
				return;
			}
			cir.setReturnValue(InteractionResult.PASS);
			cir.cancel();
		} else if(current instanceof Piglin piglin) {
			PlayerHealable playerHealable = (PlayerHealable) piglin;
			if(itemstack.isEmpty() && playerHealable.isPlayerHealed() && playerHealable.getHealedPlayer().equals(player.getUUID())) {
				if (!piglin.level.isClientSide) {
					for(int i = 0; i < piglin.getInventory().getContainerSize(); ++i) {
						ItemStack invItemStack = piglin.getInventory().getItem(i).copy();
						if(!invItemStack.isEmpty()) {
							player.setItemInHand(hand, invItemStack);
							piglin.getInventory().removeItem(i, invItemStack.getCount());

							cir.setReturnValue(InteractionResult.SUCCESS);
							cir.cancel();
							return;
						}
					}
				}
			}
			cir.setReturnValue(InteractionResult.PASS);
			cir.cancel();
		}
	}
}
