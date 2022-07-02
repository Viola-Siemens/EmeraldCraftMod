package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.world.NetherWarfieldFeature;
import com.hexagram2021.emeraldcraft.common.world.ShelterFeature;
import com.hexagram2021.emeraldcraft.common.world.ShelterPieces;
import com.hexagram2021.emeraldcraft.mixin.StructureFeatureAccess;
import com.hexagram2021.emeraldcraft.mixin.StructureSettingsAccess;
import com.hexagram2021.emeraldcraft.mixin.StructureSettingsConfigAccess;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.VillageFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructures {
	public static final StructurePieceType SHELTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "shelter", ShelterPieces.ShelterPiece::new);

	public static final ShelterFeature SHELTER = new ShelterFeature(NoneFeatureConfiguration.CODEC);
	public static final StructureFeature<JigsawConfiguration> NETHER_WARFIELD = new NetherWarfieldFeature(JigsawConfiguration.CODEC);

	public static void init(RegistryEvent.Register<StructureFeature<?>> event) {
		SHELTER.setRegistryName(new ResourceLocation(MODID, "shelter"));
		NETHER_WARFIELD.setRegistryName(new ResourceLocation(MODID, "nether_warfield"));
		event.getRegistry().register(SHELTER);
		event.getRegistry().register(NETHER_WARFIELD);

		StructureFeatureConfiguration shelterConfig;
		shelterConfig = new StructureFeatureConfiguration(64, 32, 19260817);
		StructureFeatureConfiguration netherWarfieldConfig = new StructureFeatureConfiguration(32, 8, 10387312);

		StructureFeature.STRUCTURES_REGISTRY.put(SHELTER.getRegistryName().toString().toLowerCase(Locale.ROOT), SHELTER);
		StructureFeature.STRUCTURES_REGISTRY.put(NETHER_WARFIELD.getRegistryName().toString().toLowerCase(Locale.ROOT), NETHER_WARFIELD);
		StructureFeatureAccess.setNOISE_AFFECTING_FEATURES(ImmutableList.<StructureFeature<?>>builder()
				.addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
				.add(SHELTER)
				.add(NETHER_WARFIELD)
				.build()
		);
		StructureSettingsAccess.setDEFAULTS(ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
				.putAll(StructureSettings.DEFAULTS)
				.put(SHELTER, shelterConfig)
				.put(NETHER_WARFIELD, netherWarfieldConfig)
				.build()
		);


		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

			if(structureMap instanceof ImmutableMap){
				Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
				tempMap.put(SHELTER, shelterConfig);
				tempMap.put(NETHER_WARFIELD, netherWarfieldConfig);
				((StructureSettingsConfigAccess)settings.getValue().structureSettings()).setStructureConfig(tempMap);
			}
			else{
				structureMap.put(SHELTER, shelterConfig);
				structureMap.put(NETHER_WARFIELD, netherWarfieldConfig);
			}
		});
	}
}
