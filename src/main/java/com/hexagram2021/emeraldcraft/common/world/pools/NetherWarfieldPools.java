package com.hexagram2021.emeraldcraft.common.world.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class NetherWarfieldPools {
	public static final JigsawPattern START = JigsawPatternRegistry.register(new JigsawPattern(
			new ResourceLocation(MODID, "nether_warfield/center"),
			new ResourceLocation("empty"),
			ImmutableList.of(Pair.of(
					JigsawPiece.legacy(MODID + ":nether_warfield/center/nether_warfield1"), 1
			), Pair.of(
					JigsawPiece.legacy(MODID + ":nether_warfield/center/nether_warfield2"), 2
			), Pair.of(
					JigsawPiece.legacy(MODID + ":nether_warfield/center/nether_warfield3"), 2
			)),
			JigsawPattern.PlacementBehaviour.RIGID)
	);

	public static void bootstrap() {
	}

	static {
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/springs"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/center/spring"), 5
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/springs"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/center/spring"), 5
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/streets"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/streets/straight1"), 2
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/streets/straight2"), 2
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/streets/straight3"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/streets/straight4"), 3
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/streets"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/streets/straight1"), 2
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/streets/straight2"), 2
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/streets/straight3"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/streets/straight4"), 3
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/strider"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/striders/strider1"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/striders/strider2"), 2
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/strider"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/striders/strider1"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/striders/strider2"), 2
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/decor"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/decor/lamp"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/decor/candles"), 2
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/decor"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/decor/lamp"), 3
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/decor/candles"), 2
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/houses"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/houses/small1"), 9
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/houses/small2"), 10
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/houses/plant"), 4
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/houses/medium1"), 6
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/houses/farm1"), 3
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/houses"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/houses/small1"), 10
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/houses/small2"), 9
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/houses/plant"), 4
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/houses/medium1"), 6
				), Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/houses/farm1"), 3
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "crimson_trench/villagers"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/crimson/villager"), 10
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
		JigsawPatternRegistry.register(new JigsawPattern(
				new ResourceLocation(MODID, "warped_trench/villagers"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						JigsawPiece.legacy(MODID + ":nether_warfield/warped/villager"), 10
				)),
				JigsawPattern.PlacementBehaviour.RIGID)
		);
	}
}
