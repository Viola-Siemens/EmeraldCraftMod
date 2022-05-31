package com.hexagram2021.emeraldcraft.common.register;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.blocks.plant.WarpedWartBlock;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public final class ECBlocks {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	private ECBlocks() {}

	private static <T extends Block> void registerStairs(Block fullBlock) {
		String name = fullBlock.getRegistryName().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_stairs");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_stairs");
		} else {
			name = name + "_stairs";
		}
		TO_STAIRS.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(fullBlock),
				p -> new StairBlock(() -> fullBlock.defaultBlockState(), p)
		));
	}
	private static <T extends Block> void registerStairs(BlockEntry<T> fullBlock) {
		String name = fullBlock.getId().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_stairs");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_stairs");
		} else {
			name = name + "_stairs";
		}
		TO_STAIRS.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				p -> new StairBlock(fullBlock::defaultBlockState, p)
		));
	}

	private static <T extends Block> void registerSlab(Block fullBlock) {
		String name = fullBlock.getRegistryName().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_slab");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_slab");
		} else {
			name = name + "_slab";
		}
		TO_SLAB.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(fullBlock),
				p -> new SlabBlock(p.isSuffocating((state, world, pos) ->
						fullBlock.defaultBlockState().isSuffocating(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					).isRedstoneConductor((state, world, pos) ->
						fullBlock.defaultBlockState().isRedstoneConductor(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					)
				)
		));
	}
	private static <T extends Block> void registerSlab(BlockEntry<T> fullBlock) {
		String name = fullBlock.getId().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_slab");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_slab");
		} else {
			name = name + "_slab";
		}
		TO_SLAB.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				p -> new SlabBlock(p.isSuffocating((state, world, pos) ->
						fullBlock.defaultBlockState().isSuffocating(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					).isRedstoneConductor((state, world, pos) ->
						fullBlock.defaultBlockState().isRedstoneConductor(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					)
				)
		));
	}

	private static <T extends Block> void registerWall(Block fullBlock) {
		String name = fullBlock.getRegistryName().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_wall");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_wall");
		} else {
			name = name + "_wall";
		}
		TO_WALL.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(fullBlock),
				WallBlock::new
		));
	}
	private static <T extends Block> void registerWall(BlockEntry<T> fullBlock) {
		String name = fullBlock.getId().getPath();
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", "_wall");
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick_wall");
		} else {
			name = name + "_wall";
		}
		TO_WALL.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				WallBlock::new
		));
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);

		MineralDecoration.init();
		WorkStation.init();
		//SculptureDecoration.init();
		Decoration.init();
		Plant.init();

		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<SlabBlock>> blockSlab : ECBlocks.TO_SLAB.entrySet()) {
			ECItems.REGISTER.register(blockSlab.getValue().getId().getPath(), () -> new BlockItem(blockSlab.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<StairBlock>> blockStairs : ECBlocks.TO_STAIRS.entrySet()) {
			ECItems.REGISTER.register(blockStairs.getValue().getId().getPath(), () -> new BlockItem(blockStairs.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<WallBlock>> blockWall : ECBlocks.TO_WALL.entrySet()) {
			ECItems.REGISTER.register(blockWall.getValue().getId().getPath(), () -> new BlockItem(blockWall.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
	}

	public static final class MineralDecoration {
		private static void init() {
			registerStairs(Blocks.EMERALD_BLOCK);
			registerStairs(Blocks.GOLD_BLOCK);
			registerStairs(Blocks.IRON_BLOCK);
			registerStairs(Blocks.LAPIS_BLOCK);
			registerStairs(Blocks.DIAMOND_BLOCK);
			registerStairs(Blocks.NETHERITE_BLOCK);

			registerSlab(Blocks.EMERALD_BLOCK);
			registerSlab(Blocks.GOLD_BLOCK);
			registerSlab(Blocks.IRON_BLOCK);
			registerSlab(Blocks.LAPIS_BLOCK);
			registerSlab(Blocks.DIAMOND_BLOCK);
			registerSlab(Blocks.NETHERITE_BLOCK);

			registerWall(Blocks.EMERALD_BLOCK);
			registerWall(Blocks.GOLD_BLOCK);
			registerWall(Blocks.IRON_BLOCK);
			registerWall(Blocks.LAPIS_BLOCK);
			registerWall(Blocks.DIAMOND_BLOCK);
			registerWall(Blocks.NETHERITE_BLOCK);
		}
	}

	public static final class WorkStation {
		public static final BlockEntry<CarpentryTableBlock> CARPENTRY_TABLE = new BlockEntry<>(
				"carpentry_table", CarpentryTableBlock.PROPERTIES, CarpentryTableBlock::new
		);
		public static final BlockEntry<GlassKilnBlock> GLASS_KILN = new BlockEntry<>(
				"glass_kiln", GlassKilnBlock.PROPERTIES, GlassKilnBlock::new
		);
		public static final BlockEntry<MineralTableBlock> MINERAL_TABLE = new BlockEntry<>(
				"mineral_table", MineralTableBlock.PROPERTIES, MineralTableBlock::new
		);
		public static final BlockEntry<CrystalballTableBlock> CRYSTALBALL_TABLE = new BlockEntry<>(
				"crystalball_table", CrystalballTableBlock.PROPERTIES, CrystalballTableBlock::new
		);
		public static final BlockEntry<SqueezerBlock> SQUEEZER = new BlockEntry<>(
				"squeezer", SqueezerBlock.PROPERTIES, SqueezerBlock::new
		);
		public static final BlockEntry<ContinuousMinerBlock> CONTINUOUS_MINER = new BlockEntry<>(
				"continuous_miner", ContinuousMinerBlock.PROPERTIES, ContinuousMinerBlock::new
		);
		public static final BlockEntry<IceMakerBlock> ICE_MAKER = new BlockEntry<>(
				"ice_maker", IceMakerBlock.PROPERTIES, IceMakerBlock::new
		);
		public static final BlockEntry<MelterBlock> MELTER = new BlockEntry<>(
				"melter", MelterBlock.PROPERTIES, MelterBlock::new
		);

		private static void init() {
			ECItems.REGISTER.register(CARPENTRY_TABLE.getId().getPath(), () -> new BlockItem(CARPENTRY_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GLASS_KILN.getId().getPath(), () -> new BlockItem(GLASS_KILN.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(MINERAL_TABLE.getId().getPath(), () -> new BlockItem(MINERAL_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CRYSTALBALL_TABLE.getId().getPath(), () -> new BlockItem(CRYSTALBALL_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SQUEEZER.getId().getPath(), () -> new BlockItem(SQUEEZER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CONTINUOUS_MINER.getId().getPath(), () -> new BlockItem(CONTINUOUS_MINER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(ICE_MAKER.getId().getPath(), () -> new BlockItem(ICE_MAKER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(MELTER.getId().getPath(), () -> new BlockItem(MELTER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
	}

	/*
	public static final class SculptureDecoration {
		public static final BlockEntry<SculptureBlock> VILLAGER_SCULPTURE = new BlockEntry<>(
				"villager_sculpture", SculptureBlock.PROPERTIES, SculptureBlock::new
		);
		public static final BlockEntry<SculptureBlock> IRON_GOLEM_SCULPTURE = new BlockEntry<>(
				"iron_golem_sculpture", SculptureBlock.PROPERTIES, SculptureBlock::new
		);
		public static final BlockEntry<SculptureBlock> WOLF_SCULPTURE = new BlockEntry<>(
				"wolf_sculpture", SculptureBlock.PROPERTIES, SculptureBlock::new
		);
		public static final BlockEntry<SculptureBlock> CAT_SCULPTURE = new BlockEntry<>(
				"cat_sculpture", SculptureBlock.PROPERTIES, SculptureBlock::new
		);

		private static void init() {
			ECItems.REGISTER.register(VILLAGER_SCULPTURE.getId().getPath(), () -> new BlockItem(VILLAGER_SCULPTURE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(IRON_GOLEM_SCULPTURE.getId().getPath(), () -> new BlockItem(IRON_GOLEM_SCULPTURE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(WOLF_SCULPTURE.getId().getPath(), () -> new BlockItem(WOLF_SCULPTURE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CAT_SCULPTURE.getId().getPath(), () -> new BlockItem(CAT_SCULPTURE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
	}
	//*/

	public static final class Decoration {
		public static final Supplier<BlockBehaviour.Properties> AZURE_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_BLUE)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> QUARTZ_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> JADEITE_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GREEN)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> EMERY_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SAND)
						.requiresCorrectToolForDrops().strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> BLUE_NETHER_BRICKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.STONE, MaterialColor.NETHER)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS);
		public static final Supplier<BlockBehaviour.Properties> CRIMSON_STONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.STONE, MaterialColor.CRIMSON_NYLIUM)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE);
		public static final Supplier<BlockBehaviour.Properties> WARPED_STONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WARPED_NYLIUM)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE);

		public static final BlockEntry<SandBlock> AZURE_SAND = new BlockEntry<>(
				"azure_sand", AZURE_SAND_PROPERTIES, (p) -> new SandBlock(12308970, p)
		);
		public static final BlockEntry<SandBlock> QUARTZ_SAND = new BlockEntry<>(
				"quartz_sand", QUARTZ_SAND_PROPERTIES, (p) -> new SandBlock(13816276, p)
		);
		public static final BlockEntry<SandBlock> JADEITE_SAND = new BlockEntry<>(
				"jadeite_sand", JADEITE_SAND_PROPERTIES, (p) -> new SandBlock(9823911, p)
		);
		public static final BlockEntry<SandBlock> EMERY_SAND = new BlockEntry<>(
				"emery_sand", EMERY_SAND_PROPERTIES, (p) -> new SandBlock(2500134, p)
		);

		public static final BlockEntry<Block> AZURE_SANDSTONE = new BlockEntry<>(
				"azure_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> QUARTZ_SANDSTONE = new BlockEntry<>(
				"quartz_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> JADEITE_SANDSTONE = new BlockEntry<>(
				"jadeite_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> EMERY_SANDSTONE = new BlockEntry<>(
				"emery_sandstone", SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> SMOOTH_AZURE_SANDSTONE = new BlockEntry<>(
				"smooth_azure_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_QUARTZ_SANDSTONE = new BlockEntry<>(
				"smooth_quartz_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_JADEITE_SANDSTONE = new BlockEntry<>(
				"smooth_jadeite_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_EMERY_SANDSTONE = new BlockEntry<>(
				"smooth_emery_sandstone", SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> CUT_AZURE_SANDSTONE = new BlockEntry<>(
				"cut_azure_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_QUARTZ_SANDSTONE = new BlockEntry<>(
				"cut_quartz_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_JADEITE_SANDSTONE = new BlockEntry<>(
				"cut_jadeite_sandstone", SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_EMERY_SANDSTONE = new BlockEntry<>(
				"cut_emery_sandstone", SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> BLUE_NETHER_BRICKS = new BlockEntry<>(
				"blue_nether_bricks", BLUE_NETHER_BRICKS_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> CRIMSON_STONE = new BlockEntry<>(
				"crimson_stone", CRIMSON_STONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CRIMSON_COBBLESTONE = new BlockEntry<>(
				"crimson_cobblestone", CRIMSON_STONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> WARPED_STONE = new BlockEntry<>(
				"warped_stone", WARPED_STONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> WARPED_COBBLESTONE = new BlockEntry<>(
				"warped_cobblestone", WARPED_STONE_PROPERTIES, Block::new
		);

		private static void init() {
			ECItems.REGISTER.register(AZURE_SAND.getId().getPath(), () -> new BlockItem(AZURE_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(QUARTZ_SAND.getId().getPath(), () -> new BlockItem(QUARTZ_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(JADEITE_SAND.getId().getPath(), () -> new BlockItem(JADEITE_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(EMERY_SAND.getId().getPath(), () -> new BlockItem(EMERY_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(SMOOTH_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(CUT_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(CUT_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(BLUE_NETHER_BRICKS.getId().getPath(), () -> new BlockItem(BLUE_NETHER_BRICKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(CRIMSON_STONE.getId().getPath(), () -> new BlockItem(CRIMSON_STONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CRIMSON_COBBLESTONE.getId().getPath(), () -> new BlockItem(CRIMSON_COBBLESTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(WARPED_STONE.getId().getPath(), () -> new BlockItem(WARPED_STONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(WARPED_COBBLESTONE.getId().getPath(), () -> new BlockItem(WARPED_COBBLESTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));


			registerStairs(Decoration.AZURE_SANDSTONE);
			registerStairs(Decoration.QUARTZ_SANDSTONE);
			registerStairs(Decoration.JADEITE_SANDSTONE);
			registerStairs(Decoration.EMERY_SANDSTONE);
			registerStairs(Decoration.SMOOTH_AZURE_SANDSTONE);
			registerStairs(Decoration.SMOOTH_QUARTZ_SANDSTONE);
			registerStairs(Decoration.SMOOTH_JADEITE_SANDSTONE);
			registerStairs(Decoration.SMOOTH_EMERY_SANDSTONE);
			registerStairs(Decoration.BLUE_NETHER_BRICKS);
			registerStairs(Decoration.CRIMSON_STONE);
			registerStairs(Decoration.CRIMSON_COBBLESTONE);
			registerStairs(Decoration.WARPED_STONE);
			registerStairs(Decoration.WARPED_COBBLESTONE);

			registerSlab(Decoration.AZURE_SANDSTONE);
			registerSlab(Decoration.QUARTZ_SANDSTONE);
			registerSlab(Decoration.JADEITE_SANDSTONE);
			registerSlab(Decoration.EMERY_SANDSTONE);
			registerSlab(Decoration.SMOOTH_AZURE_SANDSTONE);
			registerSlab(Decoration.SMOOTH_QUARTZ_SANDSTONE);
			registerSlab(Decoration.SMOOTH_JADEITE_SANDSTONE);
			registerSlab(Decoration.SMOOTH_EMERY_SANDSTONE);
			registerSlab(Decoration.CUT_AZURE_SANDSTONE);
			registerSlab(Decoration.CUT_QUARTZ_SANDSTONE);
			registerSlab(Decoration.CUT_JADEITE_SANDSTONE);
			registerSlab(Decoration.CUT_EMERY_SANDSTONE);
			registerSlab(Decoration.BLUE_NETHER_BRICKS);
			registerSlab(Decoration.CRIMSON_STONE);
			registerSlab(Decoration.CRIMSON_COBBLESTONE);
			registerSlab(Decoration.WARPED_STONE);
			registerSlab(Decoration.WARPED_COBBLESTONE);

			registerWall(Decoration.AZURE_SANDSTONE);
			registerWall(Decoration.QUARTZ_SANDSTONE);
			registerWall(Decoration.JADEITE_SANDSTONE);
			registerWall(Decoration.EMERY_SANDSTONE);
			registerWall(Decoration.BLUE_NETHER_BRICKS);
			registerWall(Decoration.CRIMSON_STONE);
			registerWall(Decoration.CRIMSON_COBBLESTONE);
			registerWall(Decoration.WARPED_STONE);
			registerWall(Decoration.WARPED_COBBLESTONE);
		}
	}

	public static final class Plant {
		public static final BlockEntry<WarpedWartBlock> WARPED_WART = new BlockEntry<>(
				"warped_wart", WarpedWartBlock.PROPERTIES, WarpedWartBlock::new
		);

		private static void init() { }
	}

	public static final Map<ResourceLocation, BlockEntry<SlabBlock>> TO_SLAB = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<StairBlock>> TO_STAIRS = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<WallBlock>> TO_WALL = new HashMap<>();

	public static final class BlockEntry<T extends Block> implements Supplier<T>, ItemLike {
		private final RegistryObject<T> regObject;
		private final Supplier<BlockBehaviour.Properties> properties;

		public static BlockEntry<Block> simple(String name, Supplier<BlockBehaviour.Properties> properties, Consumer<Block> extra) {
			return new BlockEntry<>(name, properties, p -> Util.make(new Block(p), extra));
		}

		public BlockEntry(String name, Supplier<BlockBehaviour.Properties> properties, Function<BlockBehaviour.Properties, T> make) {
			this.properties = properties;
			this.regObject = REGISTER.register(name, () -> make.apply(properties.get()));
		}

		@Override
		public T get()
		{
			return regObject.get();
		}

		public BlockState defaultBlockState() {
			return get().defaultBlockState();
		}

		public ResourceLocation getId() {
			return regObject.getId();
		}

		public BlockBehaviour.Properties getProperties()
		{
			return properties.get();
		}

		@Nonnull
		@Override
		public Item asItem()
		{
			return get().asItem();
		}
	}
}