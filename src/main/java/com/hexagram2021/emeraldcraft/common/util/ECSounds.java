package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECSounds {
	static Set<SoundEvent> registeredEvents = new HashSet<>();
	//public static final SoundEvent PIGLIN_CUTEY_ADMIRING_ITEM = registerSound("piglin_cutey.admiring_item");
	public static final SoundEvent PIGLIN_CUTEY_AMBIENT = registerSound("piglin_cutey.ambient");
	public static final SoundEvent PIGLIN_CUTEY_CELEBRATE = registerSound("piglin_cutey.celebrate");
	public static final SoundEvent PIGLIN_CUTEY_DEATH = registerSound("piglin_cutey.death");
	public static final SoundEvent PIGLIN_CUTEY_HURT = registerSound("piglin_cutey.hurt");
	public static final SoundEvent PIGLIN_CUTEY_NO = registerSound("piglin_cutey.no");
	public static final SoundEvent PIGLIN_CUTEY_TRADE = registerSound("piglin_cutey.trade");
	public static final SoundEvent PIGLIN_CUTEY_YES = registerSound("piglin_cutey.yes");

	public static final SoundEvent NETHER_PIGMAN_AMBIENT = registerSound("nether_pigman.ambient");
	public static final SoundEvent NETHER_PIGMAN_DEATH = registerSound("nether_pigman.death");
	public static final SoundEvent NETHER_PIGMAN_HURT = registerSound("nether_pigman.hurt");
	public static final SoundEvent NETHER_PIGMAN_NO = registerSound("nether_pigman.no");
	public static final SoundEvent NETHER_PIGMAN_TRADE = registerSound("nether_pigman.trade");
	public static final SoundEvent NETHER_PIGMAN_YES = registerSound("nether_pigman.yes");

	public static final SoundEvent NETHER_LAMBMAN_AMBIENT = registerSound("nether_lambman.ambient");
	public static final SoundEvent NETHER_LAMBMAN_DEATH = registerSound("nether_lambman.death");
	public static final SoundEvent NETHER_LAMBMAN_HURT = registerSound("nether_lambman.hurt");
	public static final SoundEvent NETHER_LAMBMAN_NO = registerSound("nether_lambman.no");
	public static final SoundEvent NETHER_LAMBMAN_TRADE = registerSound("nether_lambman.trade");
	public static final SoundEvent NETHER_LAMBMAN_YES = registerSound("nether_lambman.yes");

	public static final SoundEvent VILLAGER_WORK_ASTROLOGIST = registerSound("villager.work_astrologist");
	public static final SoundEvent VILLAGER_WORK_BEEKEEPER = registerSound("villager.work_beekeeper");
	public static final SoundEvent VILLAGER_WORK_CARPENTER = registerSound("villager.work_carpenter");
	public static final SoundEvent VILLAGER_WORK_CHEMICAL_ENGINEER = registerSound("villager.work_chemical_engineer");
	public static final SoundEvent VILLAGER_WORK_GEOLOGIST = registerSound("villager.work_geologist");
	public static final SoundEvent VILLAGER_WORK_GLAZIER = registerSound("villager.work_glazier");
	public static final SoundEvent VILLAGER_WORK_GROWER = registerSound("villager.work_grower");
	public static final SoundEvent VILLAGER_WORK_ICER = registerSound("villager.work_icer");
	public static final SoundEvent VILLAGER_WORK_MINER = registerSound("villager.work_miner");

	public static final SoundEvent HIGAN_BANA_DROP_LEAVES = registerSound("flower.drop_leaves");

	public static final SoundEvent HERRING_AMBIENT = registerSound("herring.ambient");
	public static final SoundEvent HERRING_FLOP = registerSound("herring.flop");
	public static final SoundEvent HERRING_HURT = registerSound("herring.hurt");
	public static final SoundEvent HERRING_DEATH = registerSound("herring.death");
	public static final SoundEvent BIGEYE_AMBIENT = registerSound("purple_spotted_bigeye.ambient");
	public static final SoundEvent BIGEYE_FLOP = registerSound("purple_spotted_bigeye.flop");
	public static final SoundEvent BIGEYE_HURT = registerSound("purple_spotted_bigeye.hurt");
	public static final SoundEvent BIGEYE_DEATH = registerSound("purple_spotted_bigeye.death");

	public static final SoundEvent WRAITH_AMBIENT = registerSound("wraith.ambient");
	public static final SoundEvent WRAITH_HURT = registerSound("wraith.hurt");
	public static final SoundEvent WRAITH_DEATH = registerSound("wraith.death");

	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(MODID, name);
		SoundEvent event = new SoundEvent(location);
		registeredEvents.add(event.setRegistryName(location));
		return event;
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
		for(SoundEvent event : registeredEvents)
			evt.getRegistry().register(event);
	}

	public static void PlaySoundForPlayer(Entity player, SoundEvent sound, float volume, float pitch) {
		if(player instanceof ServerPlayer)
			((ServerPlayer)player).connection.send(new ClientboundSoundPacket(sound, player.getSoundSource(),
					player.getX(), player.getY(), player.getZ(), volume, pitch));
	}
}
