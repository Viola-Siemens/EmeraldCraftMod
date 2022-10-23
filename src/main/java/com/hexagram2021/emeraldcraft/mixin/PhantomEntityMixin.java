package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.util.Convertible;
import net.minecraft.block.BlockState;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin implements Convertible {
	private int phantomConversionTime;
	@Nullable
	private UUID conversionStarter;

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	public void definePhantomCureData(CallbackInfo ci) {
		((PhantomEntity)(Object)this).getEntityData().define(Convertible.DATA_PHANTOM_CONVERTING_ID, false);
	}

	@Override
	public int getConversionProgress() {
		PhantomEntity current = (PhantomEntity)(Object)this;
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
						if (blockstate.getLightValue(current.level, mutable) > 8) {
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

		PhantomEntity current = (PhantomEntity)(Object)this;
		current.getEntityData().set(DATA_PHANTOM_CONVERTING_ID, true);
		current.removeEffect(Effects.GLOWING);
		current.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, time, Math.min(current.level.getDifficulty().getId() - 1, 0)));
		current.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
	}

	@Override
	public void finishConversion(ServerWorld level) {
		PhantomEntity current = (PhantomEntity)(Object)this;
		MantaEntity manta = current.convertTo(ECEntities.MANTA.get(), true);
		if(manta == null) {
			return;
		}

		if (this.conversionStarter != null) {
			PlayerEntity player = level.getPlayerByUUID(this.conversionStarter);
			if(player != null) {
				manta.cureFrom(current, player);
			}
		}

		manta.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
		manta.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
		ForgeEventFactory.onLivingConvert(current, manta);
	}

	@Override
	public boolean isConverting() {
		return ((PhantomEntity)(Object)this).getEntityData().get(DATA_PHANTOM_CONVERTING_ID);
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickConverting(CallbackInfo ci) {
		PhantomEntity current = (PhantomEntity)(Object)this;
		if (!current.level.isClientSide && current.isAlive() && this.isConverting()) {
			int i = this.getConversionProgress();
			this.decreaseConversionRemainTime(i);
			if (this.getConversionRemainTime() <= 0 &&
					ForgeEventFactory.canLivingConvert(current, ECEntities.MANTA.get(), this::setConversionRemainTime)) {
				this.finishConversion((ServerWorld) current.level);
			}
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addConversionData(CompoundNBT nbt, CallbackInfo ci) {
		nbt.putInt("ConversionTime", this.isConverting() ? this.phantomConversionTime : -1);
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
