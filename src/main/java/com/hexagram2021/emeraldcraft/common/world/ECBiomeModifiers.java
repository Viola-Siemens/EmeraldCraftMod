package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.register.ECPlacedFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeModifiers {
	private static final DeferredRegister<Codec<? extends BiomeModifier>> REGISTER = DeferredRegister.create(
			ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID
	);

	public static final RegistryObject<Codec<ECHiganBanaBiomeModifier>> EC_HIGAN_BANA_MODIFIER = REGISTER.register(
			"ec_higan_bana", () -> RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(ECHiganBanaBiomeModifier::biomes)
			).apply(builder, ECHiganBanaBiomeModifier::new))
	);

	public record ECHiganBanaBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {

		@Override
		public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
			if(phase == Phase.ADD && this.biomes.contains(biome)) {
				builder.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.FLOWER_HIGAN_BANA);
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return EC_HIGAN_BANA_MODIFIER.get();
		}
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}


