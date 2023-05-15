package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.ECChestBoat;
import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegisterEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECEntities {
	public static final EntityType<PiglinCuteyEntity> PIGLIN_CUTEY = EntityType.Builder.of(PiglinCuteyEntity::new, MobCategory.MISC)
					.sized(0.6F, 1.9F)
					.clientTrackingRange(8)
					.build(new ResourceLocation(MODID, "piglin_cutey").toString());
	public static final EntityType<NetherPigmanEntity> NETHER_PIGMAN = EntityType.Builder.of(NetherPigmanEntity::new, MobCategory.CREATURE)
					.sized(0.6F, 1.95F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "nether_pigman").toString());
	public static final EntityType<NetherLambmanEntity> NETHER_LAMBMAN = EntityType.Builder.of(NetherLambmanEntity::new, MobCategory.CREATURE)
					.sized(0.6F, 1.95F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "nether_lambman").toString());
	public static final EntityType<HerringEntity> HERRING = EntityType.Builder.of(HerringEntity::new, MobCategory.WATER_AMBIENT)
					.sized(0.5F, 0.4F)
					.clientTrackingRange(4)
					.build(new ResourceLocation(MODID, "herring").toString());
	public static final EntityType<PurpleSpottedBigeyeEntity> PURPLE_SPOTTED_BIGEYE = EntityType.Builder.of(PurpleSpottedBigeyeEntity::new, MobCategory.WATER_AMBIENT)
					.sized(0.5F, 0.4F)
					.clientTrackingRange(4)
					.build(new ResourceLocation(MODID, "purple_spotted_bigeye").toString());
	public static final EntityType<SnakeheadEntity> SNAKEHEAD = EntityType.Builder.of(SnakeheadEntity::new, MobCategory.WATER_AMBIENT)
					.sized(0.5F, 0.4F)
					.clientTrackingRange(4)
					.build(new ResourceLocation(MODID, "snakehead").toString());
	public static final EntityType<WraithEntity> WRAITH = EntityType.Builder.of(WraithEntity::new, MobCategory.MONSTER)
					.sized(0.75F, 2.5F)
					.clientTrackingRange(8)
					.build(new ResourceLocation(MODID, "wraith").toString());

	public static final EntityType<MantaEntity> MANTA = EntityType.Builder.of(MantaEntity::new, MobCategory.CREATURE)
					.sized(1.0F, 0.5F)
					.clientTrackingRange(8)
					.build(new ResourceLocation(MODID, "manta").toString());

	public static final EntityType<LumineEntity> LUMINE = EntityType.Builder.of(LumineEntity::new, MobCategory.CREATURE)
					.sized(0.35F, 0.6F)
					.clientTrackingRange(8)
					.build(new ResourceLocation(MODID, "lumine").toString());

	public static final EntityType<ECBoat> BOAT = EntityType.Builder.<ECBoat>of(ECBoat::new, MobCategory.MISC)
					.sized(1.375F, 0.5625F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "boat").toString());

	public static final EntityType<ECChestBoat> CHEST_BOAT = EntityType.Builder.<ECChestBoat>of(ECChestBoat::new, MobCategory.MISC)
					.sized(1.375F, 0.5625F)
					.clientTrackingRange(10)
					.build(new ResourceLocation(MODID, "chest_boat").toString());

	private ECEntities() { }

	public static void init(RegisterEvent event) {
		event.register(Registry.ENTITY_TYPE_REGISTRY, helper -> {
			helper.register(new ResourceLocation(MODID, "piglin_cutey"), PIGLIN_CUTEY);
			helper.register(new ResourceLocation(MODID, "nether_pigman"), NETHER_PIGMAN);
			helper.register(new ResourceLocation(MODID, "nether_lambman"), NETHER_LAMBMAN);
			helper.register(new ResourceLocation(MODID, "herring"), HERRING);
			helper.register(new ResourceLocation(MODID, "purple_spotted_bigeye"), PURPLE_SPOTTED_BIGEYE);
			helper.register(new ResourceLocation(MODID, "wraith"), WRAITH);
			helper.register(new ResourceLocation(MODID, "manta"), MANTA);
			helper.register(new ResourceLocation(MODID, "lumine"), LUMINE);
			helper.register(new ResourceLocation(MODID, "boat"), BOAT);
			helper.register(new ResourceLocation(MODID, "chest_boat"), CHEST_BOAT);
		});
	}
}
