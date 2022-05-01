package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.surface.AzureDesertSurfaceBuilder;
import com.hexagram2021.emeraldcraft.common.world.surface.JadeiteDesertSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECSurfaceBuilder {
	public static final DeferredRegister<SurfaceBuilder<?>> REGISTER = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, MODID);

	public static final RegistryObject<AzureDesertSurfaceBuilder> AZURE_DESERT = REGISTER.register(
			"azure_desert", () -> new AzureDesertSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC)
	);
	public static final RegistryObject<JadeiteDesertSurfaceBuilder> JADEITE_DESERT = REGISTER.register(
			"jadeite_desert", () -> new JadeiteDesertSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
