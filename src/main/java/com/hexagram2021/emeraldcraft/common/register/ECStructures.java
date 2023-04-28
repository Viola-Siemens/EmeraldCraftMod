package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.util.RegistryHelper;
import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreeFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.nether_warfield.NetherWarfieldFeature;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructures {
	public static final ShelterFeature SHELTER = new ShelterFeature(NoneFeatureConfiguration.CODEC);
	public static final StructureFeature<JigsawConfiguration> NETHER_WARFIELD = new NetherWarfieldFeature(JigsawConfiguration.CODEC);
	public static final HollowTreeFeature HOLLOW_TREE = new HollowTreeFeature(NoneFeatureConfiguration.CODEC);
	public static final EntrenchmentFeature ENTRENCHMENT = new EntrenchmentFeature(NoneFeatureConfiguration.CODEC);
	public static final CampFeature BADLANDS_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.BADLANDS);
	public static final CampFeature BIRCH_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.BIRCH);
	public static final CampFeature DESERT_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.DESERT);
	public static final CampFeature JUNGLE_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.JUNGLE);
	public static final CampFeature PLAINS_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.PLAINS);
	public static final CampFeature SAVANNA_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SAVANNA);
	public static final CampFeature SNOW_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SNOW);
	public static final CampFeature STONY_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.STONY);
	public static final CampFeature SWAMP_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SWAMP);
	public static final CampFeature TAIGA_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.TAIGA);

	public static void init(RegistryEvent.Register<StructureFeature<?>> event) {
		RegistryHelper.register(event, new ResourceLocation(MODID, "shelter"), SHELTER);
		RegistryHelper.register(event, new ResourceLocation(MODID, "nether_warfield"), NETHER_WARFIELD);
		RegistryHelper.register(event, new ResourceLocation(MODID, "hollow_tree"), HOLLOW_TREE);
		RegistryHelper.register(event, new ResourceLocation(MODID, "entrenchment"), ENTRENCHMENT);
		RegistryHelper.register(event, new ResourceLocation(MODID, "badlands_camp"), BADLANDS_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "birch_camp"), BIRCH_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "desert_camp"), DESERT_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "jungle_camp"), JUNGLE_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "plains_camp"), PLAINS_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "savanna_camp"), SAVANNA_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "snow_camp"), SNOW_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "stony_camp"), STONY_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "swamp_camp"), SWAMP_CAMP);
		RegistryHelper.register(event, new ResourceLocation(MODID, "taiga_camp"), TAIGA_CAMP);
	}
}
