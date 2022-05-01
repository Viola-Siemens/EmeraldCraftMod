package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.client.models.entities.PiglinCuteyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECEntities {
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

	public static final RegistryObject<EntityType<PiglinCuteyEntity>> PIGLIN_CUTEY = REGISTER.register(
			"piglin_cutey", () -> EntityType.Builder.of(PiglinCuteyEntity::new, MobCategory.MISC)
					.sized(0.6F, 1.9F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "piglin_cutey").toString())
	);

	private ECEntities() { }

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
