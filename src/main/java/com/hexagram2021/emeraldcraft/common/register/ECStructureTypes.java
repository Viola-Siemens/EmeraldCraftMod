package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreeFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ECStructureTypes {
    public static final StructureType<ShelterFeature> SHELTER = register("shelter", ShelterFeature.CODEC);
    public static final StructureType<EntrenchmentFeature> ENTRENCHMENT = register("entrenchment", EntrenchmentFeature.CODEC);
    public static final StructureType<CampFeature> CAMP = register("camp", CampFeature.CODEC);
    public static final StructureType<HollowTreeFeature> HOLLOW_TREE = register("hollow_tree", HollowTreeFeature.CODEC);

    private static <T extends Structure> StructureType<T> register(String name, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, EmeraldCraft.id(name), () -> codec);
    }

    public static void init() {
    }
}
