package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobAttributeEventSubscriber {

	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(ECEntities.PIGLIN_CUTEY.get(), PiglinCuteyEntity.createAttributes().build());
		event.put(ECEntities.NETHER_PIGMAN.get(), NetherPigmanEntity.createAttributes().build());
		event.put(ECEntities.NETHER_LAMBMAN.get(), NetherLambmanEntity.createAttributes().build());
	}
}
