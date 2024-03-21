package com.hexagram2021.emeraldcraft.mixin.vanilla.block.behavior;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.SqueezerBlock;
import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$24")
public abstract class GlassBottleDispenseItemBehaviorMixin {
	@Shadow
	protected abstract ItemStack takeLiquid(BlockSource blockSource, ItemStack itemStack, ItemStack result);

	@SuppressWarnings("DiscouragedShift")
	@Inject(method = "execute", at = @At(value = "RETURN", shift = At.Shift.BEFORE, ordinal = 2), cancellable = true)
	private void emeraldcraft$tryTakeHoneyFromSqueezer(BlockSource blockSource, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
		OptionalDispenseItemBehavior current = (OptionalDispenseItemBehavior)(Object)this;
		ServerLevel level = blockSource.getLevel();
		BlockPos blockPos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
		BlockState blockState = level.getBlockState(blockPos);
		if(blockState.hasProperty(ECProperties.HONEY_COUNT)) {
			int honeyCount = blockState.getValue(ECProperties.HONEY_COUNT);
			if(honeyCount > 0) {
				level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				if(blockState.getBlock() instanceof SqueezerBlock squeezerBlock) {
					squeezerBlock.resetHoneyCount(level, blockState, blockPos);
				}
				current.setSuccess(true);
				cir.setReturnValue(this.takeLiquid(blockSource, itemStack, new ItemStack(Items.HONEY_BOTTLE)));
			}
		}
	}
}
