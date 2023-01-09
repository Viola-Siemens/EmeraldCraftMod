package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.goals.OfferFlowerToPlayerGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolem.class)
public class IronGolemEntityMixin {
	@Inject(method = "registerGoals", at = @At(value = "TAIL"))
	protected void registerOfferPlayerFlowerGoal(CallbackInfo info) {
		IronGolem current = (IronGolem)(Object)this;
		current.goalSelector.addGoal(6, new OfferFlowerToPlayerGoal(current));
	}

	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	protected void tryGetFlower(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		IronGolem current = (IronGolem)(Object)this;
		ItemStack itemstack = player.getItemInHand(hand);
		if(itemstack.isEmpty() && current.getOfferFlowerTick() > 0 && player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
			current.offerFlower(false);
			player.setItemInHand(hand, new ItemStack(Items.POPPY, 1));
			cir.setReturnValue(InteractionResult.sidedSuccess(current.level.isClientSide));
			cir.cancel();
		}
	}
}
