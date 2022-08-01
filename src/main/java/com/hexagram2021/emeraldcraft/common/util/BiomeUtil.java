package com.hexagram2021.emeraldcraft.common.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeUtil {
	private static final List<Level> worldList = Lists.newArrayList();

	public static ResourceKey<Biome> biomeOrFallback(Registry<Biome> biomeRegistry, ResourceKey<Biome>... biomes) {
		for (ResourceKey<Biome> key : biomes) {
			if (isKeyRegistered(biomeRegistry, key)) {
				return key;
			}
		}

		throw new RuntimeException("Failed to find fallback for biome!");
	}

	public static boolean isKeyRegistered(Registry<Biome> registry, ResourceKey<Biome> key) {
		return key != null && registry.get(key) != null;
	}

	public static ResourceKey<Biome> getBiomeKey(Biome biome) {
		if (biome == null) {
			throw new RuntimeException("Cannot get registry key for null biome");
		} else if (biome.delegate.name() == null) {
			if (FMLEnvironment.dist == Dist.CLIENT) {
				return getClientKey(biome);
			} else {
				throw new RuntimeException("Failed to get registry key for biome!");
			}
		} else {
			return ResourceKey.create(Registry.BIOME_REGISTRY, biome.delegate.name());
		}
	}

	public static Biome getBiome(ResourceKey<Biome> key) {
		Biome biome = ForgeRegistries.BIOMES.getValue(key.location());
		if (biome == null) {
			if (FMLEnvironment.dist == Dist.CLIENT) {
				try {
					biome = getClientBiome(key);
				} catch (Exception var3) {
					ECLogger.error(var3.getMessage());
				}

				if (biome == null) {
					biome = getBiomeFromWorlds(key);
				}

				return biome;
			}

			if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
				return getBiomeFromWorlds(key);
			}
		}

		return biome;
	}

	public static Biome getBiome(int id) {
		if (id == -1) {
			throw new RuntimeException("Attempted to get biome with id -1");
		} else {
			return getBiome(((ForgeRegistry<Biome>)ForgeRegistries.BIOMES).getKey(id));
		}
	}

	public static int getBiomeId(Biome biome) {
		if (biome == null) {
			throw new RuntimeException("Attempted to get id of null biome");
		} else {
			int id = ((ForgeRegistry<Biome>)ForgeRegistries.BIOMES).getID(biome);
			if (id == -1) {
				throw new RuntimeException("Biome id is -1 for biome " + biome.delegate.name());
			} else {
				return id;
			}
		}
	}

	public static int getBiomeId(ResourceKey<Biome> key) {
		return getBiomeId(getBiome(key));
	}

	public static boolean exists(ResourceKey<Biome> key) {
		return ForgeRegistries.BIOMES.containsKey(key.location());
	}

	public static boolean exists(int id) {
		return getBiome(id) != null;
	}

	@OnlyIn(Dist.CLIENT)
	private static Registry<Biome> getClientBiomeRegistry() {
		Minecraft minecraft = Minecraft.getInstance();
		Level world = minecraft.level;
		if (world == null) {
			throw new RuntimeException("Cannot acquire biome registry when the world is null.");
		} else {
			return world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static ResourceKey<Biome> getClientKey(Biome biome) {
		return getClientBiomeRegistry().getResourceKey(biome).orElseThrow(
				() -> new RuntimeException("Failed to get client registry key for biome!")
		);
	}

	@OnlyIn(Dist.CLIENT)
	private static Biome getClientBiome(ResourceKey<Biome> key) {
		Biome biome = getClientBiomeRegistry().get(key);
		if (biome == null) {
			throw new RuntimeException("Failed to get client biome for registry key " + key.location() + "!");
		}

		return biome;
	}

	private static Biome getBiomeFromWorlds(ResourceKey<Biome> key) {
		Iterator<Level> var1 = worldList.iterator();

		Biome biome;
		do {
			if (!var1.hasNext()) {
				throw new RuntimeException("Failed to get biome for registry key " + key.location() + " !");
			}

			Level world = var1.next();
			biome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(key);
		} while(biome == null);

		return biome;
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		worldList.add((Level)event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		worldList.remove((Level)event.getWorld());
	}
}
