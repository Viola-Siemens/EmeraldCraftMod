package com.hexagram2021.emeraldcraft.common.crafting.compat.jade.entity;

import com.hexagram2021.emeraldcraft.common.entities.mobs.LumineEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public enum LumineDuplicationProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;
    public static final ResourceLocation UID = new ResourceLocation(MODID, "jade/lumine_duplication");

    LumineDuplicationProvider() {
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getServerData().contains("BreedingCD", Tag.TAG_INT)) {
            int time = entityAccessor.getServerData().getInt("BreedingCD");
            if (time > 0) {
                iTooltip.add(Component.translatable("jade.mobduplication.time", IThemeHelper.get().seconds(time)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        if (entityAccessor.getEntity() instanceof LumineEntity lumine) {
            int time = lumine.getDuplicationCooldown();
            if (time > 0) {
                compoundTag.putInt("BreedingCD", time);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
