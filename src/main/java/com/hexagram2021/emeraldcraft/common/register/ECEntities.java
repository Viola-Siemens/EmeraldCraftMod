package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.MobAttributeEventSubscriber;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.ECChestBoat;
import com.hexagram2021.emeraldcraft.common.entities.mobs.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECEntities {
    public static final EntityType<PiglinCuteyEntity> PIGLIN_CUTEY = register("piglin_cutey", EntityType.Builder.of(PiglinCuteyEntity::new, MobCategory.MISC)
            .sized(0.6F, 1.9F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "piglin_cutey").toString()));

    public static final EntityType<NetherPigmanEntity> NETHER_PIGMAN = register("nether_pigman", EntityType.Builder.of(NetherPigmanEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(10)
            .build(new ResourceLocation(MODID, "nether_pigman").toString()));

    public static final EntityType<NetherLambmanEntity> NETHER_LAMBMAN = register("nether_lambman", EntityType.Builder.of(NetherLambmanEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(10)
            .build(new ResourceLocation(MODID, "nether_lambman").toString()));

    public static final EntityType<HerringEntity> HERRING = register("herring", EntityType.Builder.of(HerringEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.5F, 0.4F)
            .clientTrackingRange(4)
            .build(new ResourceLocation(MODID, "herring").toString()));

    public static final EntityType<PurpleSpottedBigeyeEntity> PURPLE_SPOTTED_BIGEYE = register("purple_spotted_bigeye", EntityType.Builder.of(PurpleSpottedBigeyeEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.5F, 0.4F)
            .clientTrackingRange(4)
            .build(new ResourceLocation(MODID, "purple_spotted_bigeye").toString()));

    public static final EntityType<SnakeheadEntity> SNAKEHEAD = register("snakehead", EntityType.Builder.of(SnakeheadEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.5F, 0.4F)
            .clientTrackingRange(4)
            .build(new ResourceLocation(MODID, "snakehead").toString()));

    public static final EntityType<WraithEntity> WRAITH = register("wraith", EntityType.Builder.of(WraithEntity::new, MobCategory.MONSTER)
            .sized(0.75F, 2.5F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "wraith").toString()));

    public static final EntityType<WombatEntity> WOMBAT = register("wombat", EntityType.Builder.of(WombatEntity::new, MobCategory.CREATURE)
            .sized(1.0F, 0.5F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "wombat").toString()));

    public static final EntityType<MantaEntity> MANTA = register("manta", EntityType.Builder.of(MantaEntity::new, MobCategory.CREATURE)
            .sized(1.0F, 0.5F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "manta").toString()));

    public static final EntityType<LumineEntity> LUMINE = register("lumine", EntityType.Builder.of(LumineEntity::new, MobCategory.CREATURE)
            .sized(0.35F, 0.6F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "lumine").toString()));

    public static final EntityType<ECBoat> BOAT = register("boat", EntityType.Builder.<ECBoat>of(ECBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(new ResourceLocation(MODID, "boat").toString()));

    public static final EntityType<ECChestBoat> CHEST_BOAT = register("chest_boat", EntityType.Builder.<ECChestBoat>of(ECChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(new ResourceLocation(MODID, "chest_boat").toString()));

    private ECEntities() {
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, EmeraldCraft.id(name), type);
    }

    public static void init() {
        MobAttributeEventSubscriber.onAttributeCreate();
    }
}
