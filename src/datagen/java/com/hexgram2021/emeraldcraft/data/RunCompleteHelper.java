package com.hexgram2021.emeraldcraft.data;

import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunCompleteHelper implements DataProvider {
	@Override
	public void run(HashCache cache) throws IOException {
		Path toCreate = Paths.get("ec_data_gen_done");
		if(!Files.exists(toCreate))
			Files.createFile(toCreate);
	}

	@Nonnull
	@Override
	public String getName() { return "Create a file to allow crashes in datagen to be detected"; }
}
