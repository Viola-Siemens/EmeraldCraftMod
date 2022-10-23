package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin implements Convertible {
	private int piglinConversionTime;
	@Nullable
	private UUID conversionStarter;

	@Override
	public int getConversionProgress() {
		ZombifiedPiglinEntity current = (ZombifiedPiglinEntity)(Object)this;
		int ret = 1;
		if (current.getRandom().nextFloat() < 0.01F) {
			int cnt = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();
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

		ZombifiedPiglinEntity current = (ZombifiedPiglinEntity)(Object)this;
		current.getEntityData().set(DATA_PIGLIN_CONVERTING_ID, true);
		current.removeEffect(Effects.HUNGER);
		current.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, time, Math.min(current.level.getDifficulty().getId() - 1, 0)));
		current.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
	}

	@Override
	public void finishConversion(ServerWorld level) {
		ZombifiedPiglinEntity current = (ZombifiedPiglinEntity)(Object)this;
		AbstractPiglinEntity piglin;
		if(current.getRandom().nextInt(16) == 0) {
			piglin = current.convertTo(EntityType.PIGLIN_BRUTE, true);
		} else {
			piglin = current.convertTo(EntityType.PIGLIN, true);
		}
		if(piglin == null) {
			return;
		}

		piglin.setImmuneToZombification(true);
		piglin.setPersistenceRequired();
		((PlayerHealable)piglin).setPlayerHealed(true);

		if (this.conversionStarter != null) {
			PlayerEntity player = level.getPlayerByUUID(this.conversionStarter);
			ECTriggers.CURED_ZOMBIFIED_PIGLIN.trigger((ServerPlayerEntity) player, current, piglin);
		}

		piglin.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
		piglin.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
		ForgeEventFactory.onLivingConvert(current, piglin);
	}

	@Override
	public boolean isConverting() {
		return ((ZombifiedPiglinEntity)(Object)this).getEntityData().get(DATA_PIGLIN_CONVERTING_ID);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addConversionData(CompoundNBT nbt, CallbackInfo ci) {
		nbt.putInt("ConversionTime", this.isConverting() ? this.piglinConversionTime : -1);
		if (this.conversionStarter != null) {
			nbt.putUUID("ConversionPlayer", this.conversionStarter);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readConversionData(CompoundNBT nbt, CallbackInfo ci) {
		if (nbt.contains("ConversionTime", Constants.NBT.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1) {
			this.startConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
		}
	}
}
