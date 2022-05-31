package com.hexgram2021.emeraldcraft.data;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public class ECDataGenerator {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ExistingFileHelper exHelper = event.getExistingFileHelper();
		DataGenerator gen = event.getGenerator();
		if(event.includeServer()) {
			gen.addProvider(new Recipes(gen));
			gen.addProvider(new StructureUpdater("structures/village", MODID, exHelper, gen));
			// Always keep this as the last provider!
			gen.addProvider(new RunCompleteHelper());
		}
	}
}
