package com.hexagram2021.emeraldcraft.common.crafting.compat.jade;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.ContinuousMinerBlock;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.CookstoveBlock;
import com.hexagram2021.emeraldcraft.common.crafting.compat.jade.block.ContinuousMinerProvider;
import com.hexagram2021.emeraldcraft.common.crafting.compat.jade.block.CookstoveProvider;
import com.hexagram2021.emeraldcraft.common.crafting.compat.jade.entity.LumineDuplicationProvider;
import com.hexagram2021.emeraldcraft.common.entities.mobs.LumineEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ECJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ContinuousMinerProvider.INSTANCE, ContinuousMinerBlockEntity.class);
        registration.registerEntityDataProvider(LumineDuplicationProvider.INSTANCE, LumineEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ContinuousMinerProvider.INSTANCE, ContinuousMinerBlock.class);
        registration.registerBlockComponent(CookstoveProvider.INSTANCE, CookstoveBlock.class);
        registration.registerEntityComponent(LumineDuplicationProvider.INSTANCE, LumineEntity.class);
    }
}
