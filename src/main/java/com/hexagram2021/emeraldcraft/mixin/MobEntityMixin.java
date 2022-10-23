package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	protected void tryCureMobs(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
		ItemStack itemstack = player.getItemInHand(hand);
		MobEntity current = (MobEntity)(Object)this;
		if (current instanceof ZombifiedPiglinEntity) {
			ZombifiedPiglinEntity zombifiedPiglin = (ZombifiedPiglinEntity)current;
			if (itemstack.getItem() == Items.GOLDEN_CARROT && zombifiedPiglin.hasEffect(Effects.HUNGER)) {
				if (!player.abilities.instabuild) {
					itemstack.shrink(1);
				}

				if (!zombifiedPiglin.level.isClientSide) {
					((Convertible)zombifiedPiglin).startConverting(player.getUUID(), zombifiedPiglin.getRandom().nextInt(2401) + 3600);
				}

				cir.setReturnValue(ActionResultType.SUCCESS);
				cir.cancel();
				return;
			}
			cir.setReturnValue(ActionResultType.CONSUME);
			cir.cancel();
		} else if(current instanceof PhantomEntity) {
			PhantomEntity phantom = (PhantomEntity)current;
			if(itemstack.getItem() == ECItems.GOLDEN_PEACH.get() && phantom.hasEffect(Effects.GLOWING)) {
				if (!player.abilities.instabuild) {
					itemstack.shrink(1);
				}

				if (!phantom.level.isClientSide) {
					((Convertible)phantom).startConverting(player.getUUID(), phantom.getRandom().nextInt(2401) + 3600);
				}

				cir.setReturnValue(ActionResultType.SUCCESS);
				cir.cancel();
				return;
			}
			cir.setReturnValue(ActionResultType.CONSUME);
			cir.cancel();
		}
	}
}
