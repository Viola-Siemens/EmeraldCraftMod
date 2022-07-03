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
import com.hexagram2021.emeraldcraft.common.world.grower.GinkgoTreeGrower;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public final class ECBlocks {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	private ECBlocks() {}

	private static String changeNameTo(String name, String postfix) {
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", postfix);
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick" + postfix);
		} else if(name.endsWith("_planks")) {
			name = name.replaceAll("_planks", postfix);
		} else {
			name = name + postfix;
		}
		return name;
	}

	private static void registerStairs(Block fullBlock) {
		String name = changeNameTo(fullBlock.getRegistryName().getPath(), "_stairs");
		TO_STAIRS.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(fullBlock),
				p -> new StairBlock(fullBlock::defaultBlockState, p)
		));
	}
	private static <T extends Block> void registerStairs(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_stairs");
		TO_STAIRS.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				p -> new StairBlock(fullBlock::defaultBlockState, p)
		));
	}

	private static void registerSlab(Block fullBlock) {
		String name = changeNameTo(fullBlock.getRegistryName().getPath(), "_slab");
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
		String name = changeNameTo(fullBlock.getId().getPath(), "_slab");
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

	private static void registerWall(Block fullBlock) {
		String name = changeNameTo(fullBlock.getRegistryName().getPath(), "_wall");
		TO_WALL.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(fullBlock),
				WallBlock::new
		));
	}
	private static <T extends Block> void registerWall(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_wall");
		TO_WALL.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				WallBlock::new
		));
	}

	private static <T extends Block> void registerFence(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence");
		TO_FENCE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				FenceBlock::new
		));
	}

	private static <T extends Block> void registerFenceGate(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence_gate");
		TO_FENCE_GATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				FenceGateBlock::new
		));
	}

	private static <T extends Block> void registerDoor(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_door");
		TO_DOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				DoorBlock::new
		));
	}

	private static <T extends Block> void registerTrapDoor(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_trapdoor");
		TO_TRAPDOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				TrapDoorBlock::new
		));
	}

	private static <T extends Block> void registerPressurePlate(BlockEntry<T> fullBlock, PressurePlateBlock.Sensitivity sensitivity) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_pressure_plate");
		TO_PRESSURE_PLATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				(props) -> new PressurePlateBlock(sensitivity, props)
		));
	}

	private static <T extends Block> void registerWoodButton(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_button");
		TO_BUTTON.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				WoodButtonBlock::new
		));
	}

	private static <T extends Block> void registerSign(BlockEntry<T> fullBlock, WoodType woodType) {
		String name1 = changeNameTo(fullBlock.getId().getPath(), "_sign");
		String name2 = changeNameTo(fullBlock.getId().getPath(), "_wall_sign");
		TO_SIGN.put(fullBlock.getId(), new Tuple<>(new BlockEntry<>(
				name1,
				fullBlock::getProperties,
				(props) -> new StandingSignBlock(props, woodType)
		), new BlockEntry<>(
				name2,
				fullBlock::getProperties,
				(props) -> new WallSignBlock(props, woodType)
		)));
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);

		MineralDecoration.init();
		WorkStation.init();
		//SculptureDecoration.init();
		Decoration.init();
		Plant.init();

		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<SlabBlock>> blockSlab : ECBlocks.TO_SLAB.entrySet()) {
			ECItems.REGISTER.register(blockSlab.getValue().getId().getPath(), () ->
					new BlockItem(blockSlab.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<StairBlock>> blockStairs : ECBlocks.TO_STAIRS.entrySet()) {
			ECItems.REGISTER.register(blockStairs.getValue().getId().getPath(), () ->
					new BlockItem(blockStairs.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<WallBlock>> blockWall : ECBlocks.TO_WALL.entrySet()) {
			ECItems.REGISTER.register(blockWall.getValue().getId().getPath(), () ->
					new BlockItem(blockWall.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<FenceBlock>> blockFence : ECBlocks.TO_FENCE.entrySet()) {
			ECItems.REGISTER.register(blockFence.getValue().getId().getPath(), () ->
					new BlockItem(blockFence.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<FenceGateBlock>> blockFenceGate : ECBlocks.TO_FENCE_GATE.entrySet()) {
			ECItems.REGISTER.register(blockFenceGate.getValue().getId().getPath(), () ->
					new BlockItem(blockFenceGate.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<DoorBlock>> blockDoor : ECBlocks.TO_DOOR.entrySet()) {
			ECItems.REGISTER.register(blockDoor.getValue().getId().getPath(), () ->
					new BlockItem(blockDoor.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<TrapDoorBlock>> blockTrapDoor : ECBlocks.TO_TRAPDOOR.entrySet()) {
			ECItems.REGISTER.register(blockTrapDoor.getValue().getId().getPath(), () ->
					new BlockItem(blockTrapDoor.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<PressurePlateBlock>> blockPressurePlate : ECBlocks.TO_PRESSURE_PLATE.entrySet()) {
			ECItems.REGISTER.register(blockPressurePlate.getValue().getId().getPath(), () ->
					new BlockItem(blockPressurePlate.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<ButtonBlock>> blockButton : ECBlocks.TO_BUTTON.entrySet()) {
			ECItems.REGISTER.register(blockButton.getValue().getId().getPath(), () ->
					new BlockItem(blockButton.getValue().get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
		}
		for(Map.Entry<ResourceLocation, Tuple<BlockEntry<StandingSignBlock>, BlockEntry<WallSignBlock>>> blockSign :
				ECBlocks.TO_SIGN.entrySet()) {
			ECItems.REGISTER.register(blockSign.getValue().getA().getId().getPath(), () ->
					new SignItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP),
							blockSign.getValue().getA().get(),
							blockSign.getValue().getB().get()));
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
		public static final Supplier<BlockBehaviour.Properties> DARK_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BROWN)
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

		public static final BlockEntry<Block> VITRIFIED_SAND = new BlockEntry<>(
				"vitrified_sand", SANDSTONE_PROPERTIES, Block::new
		);

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
		public static final BlockEntry<SandBlock> DARK_SAND = new BlockEntry<>(
				"dark_sand", DARK_SAND_PROPERTIES, (p) -> new SandBlock(10391046, p)
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
		public static final BlockEntry<Block> DARK_SANDSTONE = new BlockEntry<>(
				"dark_sandstone", SANDSTONE_PROPERTIES, Block::new
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
		public static final BlockEntry<Block> SMOOTH_DARK_SANDSTONE = new BlockEntry<>(
				"smooth_dark_sandstone", SANDSTONE_PROPERTIES, Block::new
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
		public static final BlockEntry<Block> CUT_DARK_SANDSTONE = new BlockEntry<>(
				"cut_dark_sandstone", SANDSTONE_PROPERTIES, Block::new
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
			ECItems.REGISTER.register(VITRIFIED_SAND.getId().getPath(), () -> new BlockItem(VITRIFIED_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(AZURE_SAND.getId().getPath(), () -> new BlockItem(AZURE_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(QUARTZ_SAND.getId().getPath(), () -> new BlockItem(QUARTZ_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(JADEITE_SAND.getId().getPath(), () -> new BlockItem(JADEITE_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(EMERY_SAND.getId().getPath(), () -> new BlockItem(EMERY_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(DARK_SAND.getId().getPath(), () -> new BlockItem(DARK_SAND.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(DARK_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(SMOOTH_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(SMOOTH_DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_DARK_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(CUT_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_AZURE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_QUARTZ_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_JADEITE_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_EMERY_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CUT_DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_DARK_SANDSTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(BLUE_NETHER_BRICKS.getId().getPath(), () -> new BlockItem(BLUE_NETHER_BRICKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

			ECItems.REGISTER.register(CRIMSON_STONE.getId().getPath(), () -> new BlockItem(CRIMSON_STONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CRIMSON_COBBLESTONE.getId().getPath(), () -> new BlockItem(CRIMSON_COBBLESTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(WARPED_STONE.getId().getPath(), () -> new BlockItem(WARPED_STONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(WARPED_COBBLESTONE.getId().getPath(), () -> new BlockItem(WARPED_COBBLESTONE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));


			registerStairs(Decoration.AZURE_SANDSTONE);
			registerStairs(Decoration.QUARTZ_SANDSTONE);
			registerStairs(Decoration.JADEITE_SANDSTONE);
			registerStairs(Decoration.EMERY_SANDSTONE);
			registerStairs(Decoration.DARK_SANDSTONE);
			registerStairs(Decoration.SMOOTH_AZURE_SANDSTONE);
			registerStairs(Decoration.SMOOTH_QUARTZ_SANDSTONE);
			registerStairs(Decoration.SMOOTH_JADEITE_SANDSTONE);
			registerStairs(Decoration.SMOOTH_EMERY_SANDSTONE);
			registerStairs(Decoration.SMOOTH_DARK_SANDSTONE);
			registerStairs(Decoration.BLUE_NETHER_BRICKS);
			registerStairs(Decoration.CRIMSON_STONE);
			registerStairs(Decoration.CRIMSON_COBBLESTONE);
			registerStairs(Decoration.WARPED_STONE);
			registerStairs(Decoration.WARPED_COBBLESTONE);

			registerSlab(Decoration.AZURE_SANDSTONE);
			registerSlab(Decoration.QUARTZ_SANDSTONE);
			registerSlab(Decoration.JADEITE_SANDSTONE);
			registerSlab(Decoration.EMERY_SANDSTONE);
			registerSlab(Decoration.DARK_SANDSTONE);
			registerSlab(Decoration.SMOOTH_AZURE_SANDSTONE);
			registerSlab(Decoration.SMOOTH_QUARTZ_SANDSTONE);
			registerSlab(Decoration.SMOOTH_JADEITE_SANDSTONE);
			registerSlab(Decoration.SMOOTH_EMERY_SANDSTONE);
			registerSlab(Decoration.SMOOTH_DARK_SANDSTONE);
			registerSlab(Decoration.CUT_AZURE_SANDSTONE);
			registerSlab(Decoration.CUT_QUARTZ_SANDSTONE);
			registerSlab(Decoration.CUT_JADEITE_SANDSTONE);
			registerSlab(Decoration.CUT_EMERY_SANDSTONE);
			registerSlab(Decoration.CUT_DARK_SANDSTONE);
			registerSlab(Decoration.BLUE_NETHER_BRICKS);
			registerSlab(Decoration.CRIMSON_STONE);
			registerSlab(Decoration.CRIMSON_COBBLESTONE);
			registerSlab(Decoration.WARPED_STONE);
			registerSlab(Decoration.WARPED_COBBLESTONE);

			registerWall(Decoration.AZURE_SANDSTONE);
			registerWall(Decoration.QUARTZ_SANDSTONE);
			registerWall(Decoration.JADEITE_SANDSTONE);
			registerWall(Decoration.EMERY_SANDSTONE);
			registerWall(Decoration.DARK_SANDSTONE);
			registerWall(Decoration.BLUE_NETHER_BRICKS);
			registerWall(Decoration.CRIMSON_STONE);
			registerWall(Decoration.CRIMSON_COBBLESTONE);
			registerWall(Decoration.WARPED_STONE);
			registerWall(Decoration.WARPED_COBBLESTONE);
		}
	}

	public static final class Plant {
		public static final Supplier<BlockBehaviour.Properties> FLOWER_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS);
		public static final Supplier<BlockBehaviour.Properties> GINKGO_SAPLING_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
		public static final Supplier<BlockBehaviour.Properties> POTTED_FLOWER_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion();
		public static final Supplier<BlockBehaviour.Properties> GINKGO_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.WOOD, (blockState) ->
						blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
								MaterialColor.TERRACOTTA_ORANGE : MaterialColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> STRIPPED_GINKGO_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_ORANGE)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> GINKGO_WOOD_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<BlockBehaviour.Properties> LEAVES_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
						.isValidSpawn((blockState, level, pos, entityType) -> entityType == EntityType.OCELOT || entityType == EntityType.PARROT)
						.isSuffocating((blockState, level, pos) -> false)
						.isViewBlocking((blockState, level, pos) -> false);

		public static final Supplier<BlockBehaviour.Properties> GINKGO_PLANKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_ORANGE)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);


		public static final BlockEntry<WarpedWartBlock> WARPED_WART = new BlockEntry<>(
				"warped_wart", WarpedWartBlock.PROPERTIES, WarpedWartBlock::new
		);

		public static final BlockEntry<FlowerBlock> CYAN_PETUNIA = new BlockEntry<>(
				"cyan_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(MobEffects.WATER_BREATHING, 8, props)
		);
		public static final BlockEntry<FlowerBlock> MAGENTA_PETUNIA = new BlockEntry<>(
				"magenta_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(MobEffects.WATER_BREATHING, 8, props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_CYAN_PETUNIA = new BlockEntry<>(
				"potted_cyan_petunia", POTTED_FLOWER_PROPERTIES, (props) -> new FlowerPotBlock(
				null, CYAN_PETUNIA, props
			)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_MAGENTA_PETUNIA = new BlockEntry<>(
				"potted_magenta_petunia", POTTED_FLOWER_PROPERTIES, (props) -> new FlowerPotBlock(
				null, MAGENTA_PETUNIA, props
		)
		);

		public static final BlockEntry<SaplingBlock> GINKGO_SAPLING = new BlockEntry<>(
				"ginkgo_sapling", GINKGO_SAPLING_PROPERTIES, (props) -> new SaplingBlock(new GinkgoTreeGrower(), props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_GINKGO_SAPLING = new BlockEntry<>(
				"potted_ginkgo_sapling", POTTED_FLOWER_PROPERTIES, (props) -> new FlowerPotBlock(
						null, GINKGO_SAPLING, props
				)
		);
		public static final BlockEntry<RotatedPillarBlock> GINKGO_LOG = new BlockEntry<>(
				"ginkgo_log", GINKGO_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_GINKGO_LOG = new BlockEntry<>(
				"stripped_ginkgo_log", STRIPPED_GINKGO_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> GINKGO_WOOD = new BlockEntry<>(
				"ginkgo_wood", GINKGO_WOOD_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_GINKGO_WOOD = new BlockEntry<>(
				"stripped_ginkgo_wood", STRIPPED_GINKGO_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<LeavesBlock> GINKGO_LEAVES = new BlockEntry<>(
				"ginkgo_leaves", LEAVES_PROPERTIES, LeavesBlock::new
		);

		public static final BlockEntry<Block> GINKGO_PLANKS = new BlockEntry<>(
				"ginkgo_planks", GINKGO_PLANKS_PROPERTIES, Block::new
		);

		private static void init() {
			ECItems.REGISTER.register(CYAN_PETUNIA.getId().getPath(), () -> new BlockItem(CYAN_PETUNIA.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(MAGENTA_PETUNIA.getId().getPath(), () -> new BlockItem(MAGENTA_PETUNIA.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GINKGO_SAPLING.getId().getPath(), () -> new BlockItem(GINKGO_SAPLING.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GINKGO_LOG.getId().getPath(), () -> new BlockItem(GINKGO_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_GINKGO_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_GINKGO_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GINKGO_WOOD.getId().getPath(), () -> new BlockItem(GINKGO_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_GINKGO_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_GINKGO_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GINKGO_LEAVES.getId().getPath(), () -> new BlockItem(GINKGO_LEAVES.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(GINKGO_PLANKS.getId().getPath(), () -> new BlockItem(GINKGO_PLANKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));


			registerStairs(Plant.GINKGO_PLANKS);
			registerSlab(Plant.GINKGO_PLANKS);
			registerFence(Plant.GINKGO_PLANKS);
			registerFenceGate(Plant.GINKGO_PLANKS);
			registerDoor(Plant.GINKGO_PLANKS);
			registerTrapDoor(Plant.GINKGO_PLANKS);
			registerPressurePlate(Plant.GINKGO_PLANKS, PressurePlateBlock.Sensitivity.EVERYTHING);
			registerWoodButton(Plant.GINKGO_PLANKS);
			registerSign(Plant.GINKGO_PLANKS, ECWoodType.GINKGO);
		}
	}

	public static final Map<ResourceLocation, BlockEntry<SlabBlock>> TO_SLAB = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<StairBlock>> TO_STAIRS = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<WallBlock>> TO_WALL = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<FenceBlock>> TO_FENCE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<FenceGateBlock>> TO_FENCE_GATE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<DoorBlock>> TO_DOOR = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<TrapDoorBlock>> TO_TRAPDOOR = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<PressurePlateBlock>> TO_PRESSURE_PLATE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<ButtonBlock>> TO_BUTTON = new HashMap<>();
	public static final Map<ResourceLocation, Tuple<BlockEntry<StandingSignBlock>, BlockEntry<WallSignBlock>>> TO_SIGN = new HashMap<>();

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