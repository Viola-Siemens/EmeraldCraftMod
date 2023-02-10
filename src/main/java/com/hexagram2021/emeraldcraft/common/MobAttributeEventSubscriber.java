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
		event.put(ECEntities.PIGLIN_CUTEY, PiglinCuteyEntity.createAttributes().build());
		event.put(ECEntities.NETHER_PIGMAN, NetherPigmanEntity.createAttributes().build());
		event.put(ECEntities.NETHER_LAMBMAN, NetherLambmanEntity.createAttributes().build());
		event.put(ECEntities.HERRING, AbstractFish.createAttributes().build());
		event.put(ECEntities.PURPLE_SPOTTED_BIGEYE, AbstractFish.createAttributes().build());
		event.put(ECEntities.WRAITH, WraithEntity.createAttributes().build());
		event.put(ECEntities.MANTA, MantaEntity.createAttributes().build());
		event.put(ECEntities.LUMINE, LumineEntity.createAttributes().build());
	}
}
