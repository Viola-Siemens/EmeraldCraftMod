package com.hexagram2021.emeraldcraft.common.world.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class NetherWarfieldPools {
	public static final Holder<StructureTemplatePool> START = Pools.register(new StructureTemplatePool(
			new ResourceLocation(MODID, "nether_warfield/center"),
			new ResourceLocation("empty"),
			ImmutableList.of(Pair.of(
					StructurePoolElement.legacy(MODID + ":nether_warfield/center/nether_warfield1"), 1
			), Pair.of(
					StructurePoolElement.legacy(MODID + ":nether_warfield/center/nether_warfield2"), 2
			), Pair.of(
					StructurePoolElement.legacy(MODID + ":nether_warfield/center/nether_warfield3"), 2
			)),
			StructureTemplatePool.Projection.RIGID)
	);

	@SuppressWarnings("unused")
	public static void bootstrap() {
	}

	static {
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/springs"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/center/spring"), 5
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/springs"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/center/spring"), 5
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/streets"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/streets/straight1"), 2
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/streets/straight2"), 2
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/streets/straight3"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/streets/straight4"), 3
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/streets"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/streets/straight1"), 2
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/streets/straight2"), 2
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/streets/straight3"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/streets/straight4"), 3
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/strider"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/striders/strider1"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/striders/strider2"), 2
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/strider"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/striders/strider1"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/striders/strider2"), 2
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/decor"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/decor/lamp"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/decor/candles"), 2
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/decor"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/decor/lamp"), 3
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/decor/candles"), 2
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/houses"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/houses/small1"), 9
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/houses/small2"), 10
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/houses/plant"), 4
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/houses/medium1"), 6
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/houses/farm1"), 3
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/houses"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/houses/small1"), 10
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/houses/small2"), 9
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/houses/plant"), 4
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/houses/medium1"), 6
				), Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/houses/farm1"), 3
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "crimson_trench/villagers"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/crimson/villager"), 10
				)),
				StructureTemplatePool.Projection.RIGID)
		);
		Pools.register(new StructureTemplatePool(
				new ResourceLocation(MODID, "warped_trench/villagers"),
				new ResourceLocation("empty"),
				ImmutableList.of(Pair.of(
						StructurePoolElement.legacy(MODID + ":nether_warfield/warped/villager"), 10
				)),
				StructureTemplatePool.Projection.RIGID)
		);
	}
}
