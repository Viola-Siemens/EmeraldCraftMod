package com.hexagram2021.emeraldcraft.mixin.vanilla.block.behavior;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.SqueezerBlock;
import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearsDispenseItemBehaviorMixin {
	@Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/dispenser/ShearsDispenseItemBehavior;setSuccess(Z)V", shift = At.Shift.AFTER))
	private void emeraldcraft$tryShearSqueezer(BlockSource blockSource, ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
		OptionalDispenseItemBehavior current = (OptionalDispenseItemBehavior)(Object)this;
		if(!current.isSuccess()) {
			ServerLevel level = blockSource.getLevel();
			BlockPos blockPos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
			BlockState blockState = level.getBlockState(blockPos);
			if(blockState.hasProperty(ECProperties.HONEY_COUNT)) {
				int honeyCount = blockState.getValue(ECProperties.HONEY_COUNT);
				if(honeyCount > 0) {
					level.playSound(null, blockPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
					SqueezerBlock.dropHoneycomb(level, blockPos);
					if(blockState.getBlock() instanceof SqueezerBlock squeezerBlock) {
						squeezerBlock.resetHoneyCount(level, blockState, blockPos);
					}
					level.gameEvent(null, GameEvent.SHEAR, blockPos);
					current.setSuccess(true);
				}
			}
		}
	}
}
