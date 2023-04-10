package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreeFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructureTypes {
	private static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, MODID);

	public static final RegistryObject<StructureType<ShelterFeature>> SHELTER = register("shelter", () -> ShelterFeature.CODEC);
	public static final RegistryObject<StructureType<EntrenchmentFeature>> ENTRENCHMENT = register("entrenchment", () -> EntrenchmentFeature.CODEC);
	public static final RegistryObject<StructureType<CampFeature>> CAMP = register("camp", () -> CampFeature.CODEC);
	public static final RegistryObject<StructureType<HollowTreeFeature>> HOLLOW_TREE = register("hollow_tree", () -> HollowTreeFeature.CODEC);

	private static <T extends Structure> RegistryObject<StructureType<T>> register(String name, StructureType<T> codec) {
		return REGISTER.register(name, () -> codec);
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
