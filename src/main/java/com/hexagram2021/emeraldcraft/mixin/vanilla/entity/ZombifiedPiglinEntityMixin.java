package com.hexagram2021.emeraldcraft.mixin.vanilla.entity;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.entities.mobs.Convertible;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerHealable;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ZombifiedPiglin.class)
public class ZombifiedPiglinEntityMixin implements Convertible {
    @Unique
    private int emeraldcraft$piglinConversionTime;
    @Unique
    @Nullable
    private UUID emeraldcraft$conversionStarter;

    @Override
    public int emeraldcraft$getConversionProgress() {
        ZombifiedPiglin current = (ZombifiedPiglin) (Object) this;
        int ret = 1;
        if (current.getRandom().nextFloat() < 0.01F) {
            int cnt = 0;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            int x = (int) current.getX();
            int y = (int) current.getY();
            int z = (int) current.getZ();

            for (int dx = x - 4; dx < x + 4 && cnt < 15; ++dx) {
                for (int dy = y - 4; dy < y + 4 && cnt < 15; ++dy) {
                    for (int dz = z - 4; dz < z + 4 && cnt < 15; ++dz) {
                        BlockState blockstate = current.level().getBlockState(mutable.set(dx, dy, dz));
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
    public int emeraldcraft$getConversionRemainTime() {
        return this.emeraldcraft$piglinConversionTime;
    }

    @Override
    public void emeraldcraft$setConversionRemainTime(int time) {
        this.emeraldcraft$piglinConversionTime = time;
    }

    @Override
    public void emeraldcraft$decreaseConversionRemainTime(int dec) {
        this.emeraldcraft$piglinConversionTime -= dec;
    }

    @Override
    public void emeraldcraft$startConverting(@Nullable UUID player, int time) {
        this.emeraldcraft$conversionStarter = player;
        this.emeraldcraft$piglinConversionTime = time;

        ZombifiedPiglin current = (ZombifiedPiglin) (Object) this;
        current.getEntityData().set(Ids.DATA_PIGLIN_CONVERTING_ID, true);
        current.removeEffect(MobEffects.HUNGER);
        current.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time, Math.min(current.level().getDifficulty().getId() - 1, 0)));
        current.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
    }

    @Override
    public void emeraldcraft$finishConversion(ServerLevel level) {
        ZombifiedPiglin current = (ZombifiedPiglin) (Object) this;
        AbstractPiglin piglin;
        if (current.getRandom().nextInt(ECCommonConfig.ZOMBIFIED_PIGLIN_CONVERT_TO_PIGLIN_BRUTE_POSSIBILITY_INV.get()) == 0) {
            piglin = current.convertTo(EntityType.PIGLIN_BRUTE, true);
        } else {
            piglin = current.convertTo(EntityType.PIGLIN, true);
        }
        if (piglin == null) {
            return;
        }

        piglin.setImmuneToZombification(true);
        piglin.setCanPickUpLoot(true);
        piglin.setPersistenceRequired();
        PlayerHealable playerHealable = (PlayerHealable) piglin;
        playerHealable.emeraldcraft$setPlayerHealed(true);
        playerHealable.emeraldcraft$setHealedPlayer(this.emeraldcraft$conversionStarter);

        if (this.emeraldcraft$conversionStarter != null) {
            Player player = level.getPlayerByUUID(this.emeraldcraft$conversionStarter);
            if (player instanceof ServerPlayer serverPlayer) {
                ECTriggers.CURED_ZOMBIFIED_PIGLIN.trigger(serverPlayer, current, piglin);
            }
        }

        piglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        piglin.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
        /*ForgeEventFactory.onLivingConvert(current, piglin);*/
        ServerLivingEntityEvents.MOB_CONVERSION.invoker().onConversion(current, piglin, true);
    }

    @Override
    public boolean emeraldcraft$isConverting() {
        return ((ZombifiedPiglin) (Object) this).getEntityData().get(Ids.DATA_PIGLIN_CONVERTING_ID);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    public void emeraldcraft$addConversionData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("ConversionTime", this.emeraldcraft$isConverting() ? this.emeraldcraft$piglinConversionTime : -1);
        if (this.emeraldcraft$conversionStarter != null) {
            nbt.putUUID("ConversionPlayer", this.emeraldcraft$conversionStarter);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    public void emeraldcraft$readConversionData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1) {
            this.emeraldcraft$startConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
        }
    }

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void emeraldcraft$initializeId(CallbackInfo ci) {
        Ids.DATA_PIGLIN_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedPiglin.class, EntityDataSerializers.BOOLEAN);
    }
}
