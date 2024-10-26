package com.hexagram2021.emeraldcraft.common.crafting.compat.jade.block;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.HorizontalLineElement;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public enum CookstoveProvider implements IBlockComponentProvider {
	INSTANCE;
	public static final ResourceLocation UID = new ResourceLocation(MODID, "jade/cookstove");

	CookstoveProvider() {
	}

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
		IElementHelper helper = IElementHelper.get();
		if(blockAccessor.getBlockEntity() instanceof CookstoveBlockEntity cookstoveBlockEntity) {
			ItemStack result = cookstoveBlockEntity.getResult();
			if(!result.isEmpty()) {
				iTooltip.add(new HorizontalLineElement());
				iTooltip.add(helper.textElement(Component.translatable("jade.emeraldcraft.cookstove.result")).scale(0.5F));
				iTooltip.add(new HorizontalLineElement());
				iTooltip.add(helper.item(result));
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}
