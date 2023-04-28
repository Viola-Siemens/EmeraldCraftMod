package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ZombifiedPiglin.class)
public class ZombifiedPiglinEntityMixin implements Convertible {
	private int piglinConversionTime;
	@Nullable
	private UUID conversionStarter;

	@Override
	public int getConversionProgress() {
		ZombifiedPiglin current = (ZombifiedPiglin)(Object)this;
		int ret = 1;
		if (current.getRandom().nextFloat() < 0.01F) {
			int cnt = 0;
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			int x = (int)current.getX();
			int y = (int)current.getY();
			int z = (int)current.getZ();

			for(int dx = x - 4; dx < x + 4 && cnt < 15; ++dx) {
				for(int dy = y - 4; dy < y + 4 && cnt < 15; ++dy) {
					for(int dz = z - 4; dz < z + 4 && cnt < 15; ++dz) {
						BlockState blockstate = current.level.getBlockState(mutable.set(dx, dy, dz));
						if (blockstate.is(BlockTags.GUARDED_BY_PIGLINS)) {
							if (current.getRandom().nextBoolean()) {
								++ret;
							}

							++cnt;
						}
					}
				}
			}
		}

		return ret;
	}

	@Override
	public int getConversionRemainTime() {
		return this.piglinConversionTime;
	}

	@Override
	public void setConversionRemainTime(int time) {
		this.piglinConversionTime = time;
	}

	@Override
	public void decreaseConversionRemainTime(int dec) {
		this.piglinConversionTime -= dec;
	}

	@Override
	public void startConverting(@Nullable UUID player, int time) {
		this.conversionStarter = player;
		this.piglinConversionTime = time;

		ZombifiedPiglin current = (ZombifiedPiglin)(Object)this;
		current.getEntityData().set(DATA_PIGLIN_CONVERTING_ID, true);
		current.removeEffect(MobEffects.HUNGER);
		current.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time, Math.min(current.level.getDifficulty().getId() - 1, 0)));
		current.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
	}

	@Override
	public void finishConversion(ServerLevel level) {
		ZombifiedPiglin current = (ZombifiedPiglin)(Object)this;
		AbstractPiglin piglin;
		if(current.getRandom().nextInt(ECCommonConfig.ZOMBIFIED_PIGLIN_CONVERT_TO_PIGLIN_BRUTE_POSSIBILITY_INV.get()) == 0) {
			piglin = current.convertTo(EntityType.PIGLIN_BRUTE, true);
		} else {
			piglin = current.convertTo(EntityType.PIGLIN, true);
		}
		if(piglin == null) {
			return;
		}

		piglin.setImmuneToZombification(true);
		piglin.setCanPickUpLoot(true);
		piglin.setPersistenceRequired();
		PlayerHealable playerHealable = (PlayerHealable)piglin;
		playerHealable.setPlayerHealed(true);
		playerHealable.setHealedPlayer(this.conversionStarter);

		if (this.conversionStarter != null) {
			Player player = level.getPlayerByUUID(this.conversionStarter);
			ECTriggers.CURED_ZOMBIFIED_PIGLIN.trigger((ServerPlayer) player, current, piglin);
		}

		piglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
		piglin.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
		ForgeEventFactory.onLivingConvert(current, piglin);
	}

	@Override
	public boolean isConverting() {
		return ((ZombifiedPiglin)(Object)this).getEntityData().get(DATA_PIGLIN_CONVERTING_ID);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addConversionData(CompoundTag nbt, CallbackInfo ci) {
		nbt.putInt("ConversionTime", this.isConverting() ? this.piglinConversionTime : -1);
		if (this.conversionStarter != null) {
			nbt.putUUID("ConversionPlayer", this.conversionStarter);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readConversionData(CompoundTag nbt, CallbackInfo ci) {
		if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1) {
			this.startConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
		}
	}
}
