package com.hexagram2021.emeraldcraft.common.util;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryEntry;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings("unused")
public class BiomeUtil {
    private static final List<Level> worldList = Lists.newArrayList();

    public static Registry<Biome> getBiomeRegistry() {
        return EmeraldCraft.getCurrentServer().registryAccess().registryOrThrow(Registries.BIOME);
    }

    @SafeVarargs
    public static ResourceKey<Biome> biomeOrFallback(Registry<Biome> biomeRegistry, ECBiomeKeys.BiomeKey key, ResourceKey<Biome>... biomes) {
        if (isKeyRegistered(biomeRegistry, key)) {
            return key.key();
        }
        for (ResourceKey<Biome> biome : biomes) {
            if (biome != null) {
                return biome;
            }
        }
        throw new RuntimeException("Failed to find fallback for biome!");
    }

    public static boolean isKeyRegistered(Registry<Biome> registry, @Nullable ECBiomeKeys.BiomeKey key) {
        return key != null && key.generate() && registry.get(key.key()) != null;
    }

    public static ResourceKey<Biome> getBiomeKey(@Nullable Biome biome) {
        if (biome == null) {
            throw new RuntimeException("Cannot get registry key for null biome");
        } else {
            ResourceLocation name = getBiomeRegistry().getKey(biome);
            if (name == null) {
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                    return getClientKey(biome);
                } else {
                    throw new RuntimeException("Failed to get registry key for biome!");
                }
            } else {
                return ResourceKey.create(Registries.BIOME, name);
            }
        }
    }

    @Nullable
    public static Biome getBiome(ResourceKey<Biome> key) {
        Biome biome = getRegistryEntry(getBiomeRegistry(), key.location(), null);
        if (biome == null) {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
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

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                return getBiomeFromWorlds(key);
            }
        }

        return biome;
    }

    @Nullable
    public static Biome getBiome(int id) {
        if (id == -1) {
            throw new RuntimeException("Attempted to get biome with id -1");
        } else {
            return getBiomeRegistry().byId(id);
        }
    }

    public static int getBiomeId(MinecraftServer server, @Nullable Biome biome) {
        if (biome == null) {
            throw new RuntimeException("Attempted to get id of null biome");
        } else {
            int id = server.registryAccess().registryOrThrow(Registries.BIOME).getId(biome);
            if (id == -1) {
                throw new RuntimeException("Biome id is -1 for biome " + getRegistryName(server, biome));
            } else {
                return id;
            }
        }
    }

    public static int getBiomeId(ResourceKey<Biome> key) {
        return getBiomeId(EmeraldCraft.getCurrentServer(), getBiome(key));
    }

    public static boolean exists(ResourceKey<Biome> key) {
        return getBiomeRegistry().containsKey(key);
    }

    public static boolean exists(int id) {
        return getBiome(id) != null;
    }

    @Environment(EnvType.CLIENT)
    private static Registry<Biome> getClientBiomeRegistry() {
        Minecraft minecraft = Minecraft.getInstance();
        Level world = minecraft.level;
        if (world == null) {
            throw new RuntimeException("Cannot acquire biome registry when the world is null.");
        } else {
            return world.registryAccess().registryOrThrow(Registries.BIOME);
        }
    }

    @Environment(EnvType.CLIENT)
    private static ResourceKey<Biome> getClientKey(Biome biome) {
        return getClientBiomeRegistry().getResourceKey(biome).orElseThrow(
                () -> new RuntimeException("Failed to get client registry key for biome!")
        );
    }

    @Environment(EnvType.CLIENT)
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
            biome = world.registryAccess().registryOrThrow(Registries.BIOME).get(key);
        } while (biome == null);

        return biome;
    }

    public static void onWorldLoad(MinecraftServer server, ServerLevel world) {
        worldList.add(world);
    }

    public static void onWorldUnload(MinecraftServer server, ServerLevel world) {
        worldList.remove(world);
    }
}
