package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.world.entity.animal.AbstractFish;
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
		event.put(ECEntities.HERRING.get(), AbstractFish.createAttributes().build());
		event.put(ECEntities.PURPLE_SPOTTED_BIGEYE.get(), AbstractFish.createAttributes().build());
		event.put(ECEntities.MANTA.get(), MantaEntity.createAttributes().build());
		event.put(ECEntities.WRAITH.get(), WraithEntity.createAttributes().build());
	}
}
