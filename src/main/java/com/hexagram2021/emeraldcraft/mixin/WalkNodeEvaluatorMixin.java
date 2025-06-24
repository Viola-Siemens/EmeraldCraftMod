package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.CookstoveBlock;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {
	@Inject(method = "isBurningBlock", at = @At(value = "HEAD"), cancellable = true)
	private static void emeraldcraft$isMyBurningBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if(state.is(ECBlocks.WorkStation.COOKSTOVE.get()) && state.getValue(CookstoveBlock.LIT)) {
			cir.setReturnValue(true);
		}
	}
}
