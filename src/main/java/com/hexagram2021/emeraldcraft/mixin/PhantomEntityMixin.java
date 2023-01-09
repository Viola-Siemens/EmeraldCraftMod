package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Phantom.class)
public class PhantomEntityMixin implements Convertible {
	private int phantomConversionTime;
	@Nullable
	private UUID conversionStarter;

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	public void definePhantomCureData(CallbackInfo ci) {
		((Phantom)(Object)this).getEntityData().define(Convertible.DATA_PHANTOM_CONVERTING_ID, false);
	}

	@Override
	public int getConversionProgress() {
		Phantom current = (Phantom)(Object)this;
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
						if (blockstate.getLightEmission(current.level, mutable) > 8) {
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
		return this.phantomConversionTime;
	}

	@Override
	public void setConversionRemainTime(int time) {
		this.phantomConversionTime = time;
	}

	@Override
	public void decreaseConversionRemainTime(int dec) {
		this.phantomConversionTime -= dec;
	}

	@Override
	public void startConverting(@Nullable UUID player, int time) {
		this.conversionStarter = player;
		this.phantomConversionTime = time;

		Phantom current = (Phantom)(Object)this;
		current.getEntityData().set(DATA_PHANTOM_CONVERTING_ID, true);
		current.removeEffect(MobEffects.GLOWING);
		current.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time, Math.min(current.level.getDifficulty().getId() - 1, 0)));
		current.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
	}

	@Override
	public void finishConversion(ServerLevel level) {
		Phantom current = (Phantom)(Object)this;
		MantaEntity manta = current.convertTo(ECEntities.MANTA, true);
		if(manta == null) {
			return;
		}

		if (this.conversionStarter != null) {
			Player player = level.getPlayerByUUID(this.conversionStarter);
			if(player != null) {
				manta.cureFrom(current, player);
			}
		}

		manta.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
		manta.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
		ForgeEventFactory.onLivingConvert(current, manta);
	}

	@Override
	public boolean isConverting() {
		return ((Phantom)(Object)this).getEntityData().get(DATA_PHANTOM_CONVERTING_ID);
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickConverting(CallbackInfo ci) {
		Phantom current = (Phantom)(Object)this;
		if (!current.level.isClientSide && current.isAlive() && this.isConverting()) {
			int i = this.getConversionProgress();
			this.decreaseConversionRemainTime(i);
			if (this.getConversionRemainTime() <= 0 &&
					ForgeEventFactory.canLivingConvert(current, ECEntities.MANTA, this::setConversionRemainTime)) {
				this.finishConversion((ServerLevel) current.level);
			}
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addConversionData(CompoundTag nbt, CallbackInfo ci) {
		nbt.putInt("ConversionTime", this.isConverting() ? this.phantomConversionTime : -1);
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
