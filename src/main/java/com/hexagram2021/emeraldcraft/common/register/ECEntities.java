package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECEntities {
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

	public static final RegistryObject<EntityType<PiglinCuteyEntity>> PIGLIN_CUTEY = REGISTER.register(
			"piglin_cutey", () -> EntityType.Builder.of(PiglinCuteyEntity::new, MobCategory.MISC)
					.sized(0.6F, 1.9F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "piglin_cutey").toString())
	);
	public static final RegistryObject<EntityType<NetherPigmanEntity>> NETHER_PIGMAN = REGISTER.register(
			"nether_pigman", () -> EntityType.Builder.of(NetherPigmanEntity::new, MobCategory.CREATURE)
					.sized(0.6F, 1.95F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "nether_pigman").toString())
	);
	public static final RegistryObject<EntityType<NetherLambmanEntity>> NETHER_LAMBMAN = REGISTER.register(
			"nether_lambman", () -> EntityType.Builder.of(NetherLambmanEntity::new, MobCategory.CREATURE)
					.sized(0.6F, 1.95F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "nether_lambman").toString())
	);

	public static final RegistryObject<EntityType<ECBoat>> BOAT = REGISTER.register(
			"boat", () -> EntityType.Builder.<ECBoat>of(ECBoat::new, MobCategory.MISC)
					.sized(1.375F, 0.5625F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "boat").toString())
	);

	private ECEntities() { }

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
