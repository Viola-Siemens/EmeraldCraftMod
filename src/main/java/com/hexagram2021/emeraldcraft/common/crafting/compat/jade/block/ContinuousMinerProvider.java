package com.hexagram2021.emeraldcraft.common.crafting.compat.jade.block;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public enum ContinuousMinerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;
	public static final ResourceLocation UID = new ResourceLocation(MODID, "jade/continuous_miner");

	ContinuousMinerProvider() {
	}

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
		if (blockAccessor.getServerData().contains("NextResultTime", Tag.TAG_INT)) {
			int time = blockAccessor.getServerData().getInt("NextResultTime");
			if (time > 0) {
				iTooltip.add(Component.translatable("jade.emeraldcraft.continuous_miner.time", IThemeHelper.get().seconds(time)));
			}
		}
	}

	@Override
	public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
		if(blockAccessor.getBlockEntity() instanceof ContinuousMinerBlockEntity continuousMinerBlockEntity) {
			int time = continuousMinerBlockEntity.getMineTime();
			if(time > 0) {
				compoundTag.putInt("NextResultTime", time);
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}
