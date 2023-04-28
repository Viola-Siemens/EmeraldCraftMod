package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorkAtPoi.class)
public class WorkAtPoiMixin {
	@Inject(method = "useWorkstation", at = @At(value = "HEAD"), cancellable = true)
	protected void specialProfessionUseWorkstation(ServerLevel level, Villager villagerWithProfession, CallbackInfo ci) {
		ResourceLocation professionID = villagerWithProfession.getVillagerData().getProfession().getRegistryName();
		if(Villages.ASTROLOGIST.equals(professionID)) {
			if(villagerWithProfession.getRandom().nextInt(4) == 0) {
				List<ServerPlayer> players = level.getPlayers(player -> villagerWithProfession.distanceToSqr(player) < 1024.0D && player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE));
				List<? extends Villager> villagers = level.getEntities(EntityType.VILLAGER, entity -> villagerWithProfession.distanceToSqr(entity) < 1024.0D);
				for (ServerPlayer player : players) {
					player.heal(1.0F);
				}
				for (Villager villager : villagers) {
					villager.heal(1.0F);
				}
			}
			ci.cancel();
		} else if(Villages.GEOLOGIST.equals(professionID)) {
			if(villagerWithProfession.getRandom().nextInt(4) == 0) {
				villagerWithProfession.getBrain().getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> {
					BlockPos blockPos = globalPos.pos();
					BlockState blockState = level.getBlockState(blockPos);
					BlockEntity blockEntity = level.getBlockEntity(blockPos);
					if (blockState.is(ECBlocks.WorkStation.CONTINUOUS_MINER.get()) &&
							blockEntity instanceof ContinuousMinerBlockEntity continuousMinerBlockEntity) {
						continuousMinerBlockEntity.dispenseFrom(blockState, level, blockPos, level.getRandom(), false);
					}
				});
			}
			ci.cancel();
		}
	}
}
