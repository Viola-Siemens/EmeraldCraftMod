package com.hexagram2021.emeraldcraft.common.register;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.blocks.plant.*;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.*;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.world.grower.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.simibubi.create.AllBlocks.COPPER_BLOCK;
import static com.simibubi.create.AllBlocks.ZINC_BLOCK;

public final class ECBlocks {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	private ECBlocks() {}

	private static String changeNameTo(String name, String postfix) {
		if(name.endsWith("_block")) {
			name = name.replaceAll("_block", postfix);
		} else if(name.endsWith("_bricks")) {
			name = name.replaceAll("_bricks", "_brick" + postfix);
		} else if(name.endsWith("_tiles")) {
			name = name.replaceAll("_tiles", "_tile" + postfix);
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
				() -> AbstractBlock.Properties.copy(fullBlock),
				p -> new StairsBlock(fullBlock::defaultBlockState, p)
		));
	}
	private static <T extends Block> void registerStairs(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_stairs");
		TO_STAIRS.put(fullBlock.getId(), new BlockEntry<>(
				name,
				fullBlock::getProperties,
				p -> new StairsBlock(fullBlock::defaultBlockState, p)
		));
	}
	private static void registerStairs(ResourceLocation fullBlockRegistryName, String fullBlockName, AbstractBlock.Properties props, Supplier<BlockState> defaultBlockState) {
		String name = changeNameTo(fullBlockName, "_stairs");
		TO_STAIRS.put(fullBlockRegistryName, new BlockEntry<>(
				name,
				() -> props,
				p -> new StairsBlock(defaultBlockState, p)
		));
	}

	private static void registerSlab(Block fullBlock) {
		String name = changeNameTo(fullBlock.getRegistryName().getPath(), "_slab");
		TO_SLAB.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.copy(fullBlock),
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
	private static void registerSlab(ResourceLocation fullBlockRegistryName, String fullBlockName, AbstractBlock.Properties props, Supplier<BlockState> defaultBlockState) {
		String name = changeNameTo(fullBlockName, "_slab");
		TO_SLAB.put(fullBlockRegistryName, new BlockEntry<>(
				name,
				() -> props,
				p -> new SlabBlock(p.isSuffocating((state, world, pos) ->
						defaultBlockState.get().isSuffocating(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					).isRedstoneConductor((state, world, pos) ->
						defaultBlockState.get().isRedstoneConductor(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
					)
				)
		));
	}

	private static void registerWall(Block fullBlock) {
		String name = changeNameTo(fullBlock.getRegistryName().getPath(), "_wall");
		TO_WALL.put(fullBlock.getRegistryName(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.copy(fullBlock),
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
	private static void registerWall(ResourceLocation fullBlockRegistryName, String fullBlockName, AbstractBlock.Properties props) {
		String name = changeNameTo(fullBlockName, "_wall");
		TO_WALL.put(fullBlockRegistryName, new BlockEntry<>(
				name,
				() -> props,
				WallBlock::new
		));
	}

	private static <T extends Block> void registerWoodenFence(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence");
		TO_FENCE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD),
				FenceBlock::new
		));
	}

	private static <T extends Block> void registerWoodenFenceGate(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence_gate");
		TO_FENCE_GATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD),
				FenceGateBlock::new
		));
	}

	private static <T extends Block> void registerWoodenDoor(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_door");
		TO_DOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(),
				DoorBlock::new
		));
	}

	private static <T extends Block> void registerWoodenTrapDoor(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_trapdoor");
		TO_TRAPDOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).strength(3.0F)
						.sound(SoundType.WOOD).noOcclusion().isValidSpawn((blockState, blockReader, blockPos, entityType) -> false),
				TrapDoorBlock::new
		));
	}

	private static <T extends Block> void registerWoodPressurePlate(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_pressure_plate");
		TO_PRESSURE_PLATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD),
				(props) -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, props)
		));
	}

	private static <T extends Block> void registerWoodButton(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_button");
		TO_BUTTON.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> AbstractBlock.Properties.of(Material.DECORATION, fullBlock.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD),
				WoodButtonBlock::new
		));
	}

	private static <T extends Block> void registerSign(BlockEntry<T> fullBlock, WoodType woodType) {
		String name1 = changeNameTo(fullBlock.getId().getPath(), "_sign");
		String name2 = changeNameTo(fullBlock.getId().getPath(), "_wall_sign");
		TO_SIGN.put(fullBlock.getId(), new Tuple<>(new BlockEntry<>(
				name1,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD),
				(props) -> new StandingSignBlock(props, woodType)
		), new BlockEntry<>(
				name2,
				() -> AbstractBlock.Properties.of(Material.WOOD, fullBlock.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD),
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
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<StairsBlock>> blockStairs : ECBlocks.TO_STAIRS.entrySet()) {
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
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<AbstractButtonBlock>> blockButton : ECBlocks.TO_BUTTON.entrySet()) {
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

			if(ModsLoadedEventSubscriber.CREATE) {
				registerStairs(
						ZINC_BLOCK.getId(),
						ZINC_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK),
						ZINC_BLOCK::getDefaultState
				);
				registerSlab(
						ZINC_BLOCK.getId(),
						ZINC_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK),
						ZINC_BLOCK::getDefaultState
				);
				registerWall(
						ZINC_BLOCK.getId(),
						ZINC_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)
				);
				registerStairs(
						COPPER_BLOCK.getId(),
						COPPER_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK),
						COPPER_BLOCK::getDefaultState
				);
				registerSlab(
						COPPER_BLOCK.getId(),
						COPPER_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK),
						COPPER_BLOCK::getDefaultState
				);
				registerWall(
						COPPER_BLOCK.getId(),
						COPPER_BLOCK.getId().getPath(),
						AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)
				);
			} else {
				fakeCompatBlock("zinc_stairs", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
				fakeCompatBlock("zinc_slab", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
				fakeCompatBlock("zinc_wall", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
				fakeCompatBlock("copper_stairs", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
				fakeCompatBlock("copper_slab", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
				fakeCompatBlock("copper_wall", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
			}
		}

		private static void fakeCompatBlock(String name, Supplier<Block> make) {
			RegistryObject<Block> tempBlock = REGISTER.register(name, make);
			ECItems.REGISTER.register(name, () ->
					new BlockItem(tempBlock.get(), new Item.Properties()));
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
			ECItems.REGISTER.register(
					CARPENTRY_TABLE.getId().getPath(),
					() -> new BlockItem(CARPENTRY_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.carpentry_table").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					GLASS_KILN.getId().getPath(),
					() -> new BlockItem(GLASS_KILN.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.glass_kiln").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					MINERAL_TABLE.getId().getPath(),
					() -> new BlockItem(MINERAL_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.mineral_table").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					CRYSTALBALL_TABLE.getId().getPath(),
					() -> new BlockItem(CRYSTALBALL_TABLE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.crystalball_table").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					SQUEEZER.getId().getPath(),
					() -> new BlockItem(SQUEEZER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.squeezer").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					CONTINUOUS_MINER.getId().getPath(),
					() -> new BlockItem(CONTINUOUS_MINER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.continuous_miner").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					ICE_MAKER.getId().getPath(),
					() -> new BlockItem(ICE_MAKER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.ice_maker").withStyle(TextFormatting.GRAY));
						}
					}
			);
			ECItems.REGISTER.register(
					MELTER.getId().getPath(),
					() -> new BlockItem(MELTER.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)) {
						@Override
						public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World level, @Nonnull List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
							components.add(new TranslationTextComponent("desc.emeraldcraft.melter").withStyle(TextFormatting.GRAY));
						}
					}
			);
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
		public static final Supplier<AbstractBlock.Properties> AZURE_SAND_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_BLUE)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<AbstractBlock.Properties> QUARTZ_SAND_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<AbstractBlock.Properties> JADEITE_SAND_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GREEN)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<AbstractBlock.Properties> EMERY_SAND_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<AbstractBlock.Properties> DARK_SAND_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_BROWN)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<AbstractBlock.Properties> SANDSTONE_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.STONE, MaterialColor.SAND)
						.requiresCorrectToolForDrops().strength(0.8F);
		public static final Supplier<AbstractBlock.Properties> BLUE_NETHER_BRICKS_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS);
		public static final Supplier<AbstractBlock.Properties> CRIMSON_STONE_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.STONE, MaterialColor.CRIMSON_NYLIUM)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE);
		public static final Supplier<AbstractBlock.Properties> WARPED_STONE_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.STONE, MaterialColor.WARPED_NYLIUM)
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

		public static final BlockEntry<Block> DEEPSLATE = new BlockEntry<>(
				"deepslate",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.STONE),
				RotatedPillarBlock::new
		);
		public static final BlockEntry<Block> COBBLED_DEEPSLATE = new BlockEntry<>(
				"cobbled_deepslate",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> POLISHED_DEEPSLATE = new BlockEntry<>(
				"polished_deepslate",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> DEEPSLATE_TILES = new BlockEntry<>(
				"deepslate_tiles",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> DEEPSLATE_BRICKS = new BlockEntry<>(
				"deepslate_bricks",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> CHISELED_DEEPSLATE = new BlockEntry<>(
				"chiseled_deepslate",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> CRACKED_DEEPSLATE_BRICKS = new BlockEntry<>(
				"cracked_deepslate_bricks",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
		);
		public static final BlockEntry<Block> CRACKED_DEEPSLATE_TILES = new BlockEntry<>(
				"cracked_deepslate_tiles",
				() -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.5F, 6.0F).sound(SoundType.STONE),
				Block::new
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

			ECItems.REGISTER.register(DEEPSLATE.getId().getPath(), () -> new BlockItem(DEEPSLATE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(COBBLED_DEEPSLATE.getId().getPath(), () -> new BlockItem(COBBLED_DEEPSLATE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(POLISHED_DEEPSLATE.getId().getPath(), () -> new BlockItem(POLISHED_DEEPSLATE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(DEEPSLATE_TILES.getId().getPath(), () -> new BlockItem(DEEPSLATE_TILES.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(DEEPSLATE_BRICKS.getId().getPath(), () -> new BlockItem(DEEPSLATE_BRICKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CHISELED_DEEPSLATE.getId().getPath(), () -> new BlockItem(CHISELED_DEEPSLATE.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CRACKED_DEEPSLATE_BRICKS.getId().getPath(), () -> new BlockItem(CRACKED_DEEPSLATE_BRICKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(CRACKED_DEEPSLATE_TILES.getId().getPath(), () -> new BlockItem(CRACKED_DEEPSLATE_TILES.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));

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

			registerStairs(Decoration.COBBLED_DEEPSLATE);
			registerStairs(Decoration.POLISHED_DEEPSLATE);
			registerStairs(Decoration.DEEPSLATE_TILES);
			registerStairs(Decoration.DEEPSLATE_BRICKS);
			registerSlab(Decoration.COBBLED_DEEPSLATE);
			registerSlab(Decoration.POLISHED_DEEPSLATE);
			registerSlab(Decoration.DEEPSLATE_TILES);
			registerSlab(Decoration.DEEPSLATE_BRICKS);
			registerWall(Decoration.COBBLED_DEEPSLATE);
			registerWall(Decoration.POLISHED_DEEPSLATE);
			registerWall(Decoration.DEEPSLATE_TILES);
			registerWall(Decoration.DEEPSLATE_BRICKS);
		}
	}

	public static final class Plant {
		public static final Supplier<AbstractBlock.Properties> FLOWER_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS);
		public static final Supplier<AbstractBlock.Properties> SAPLING_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
		public static final Supplier<AbstractBlock.Properties> POTTED_FLOWER_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.DECORATION).instabreak().noOcclusion();
		public static final Supplier<AbstractBlock.Properties> GINKGO_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, (blockState) ->
						blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
								MaterialColor.TERRACOTTA_ORANGE : MaterialColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> STRIPPED_GINKGO_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_ORANGE)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> GINKGO_WOOD_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<AbstractBlock.Properties> PALM_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, (blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MaterialColor.TERRACOTTA_BROWN : MaterialColor.COLOR_BLACK)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> STRIPPED_PALM_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> PALM_WOOD_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<AbstractBlock.Properties> PEACH_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, (blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MaterialColor.COLOR_MAGENTA : MaterialColor.TERRACOTTA_YELLOW)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> STRIPPED_PEACH_LOG_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_MAGENTA)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> PEACH_WOOD_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_YELLOW)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<AbstractBlock.Properties> LEAVES_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
						.isValidSpawn((blockState, level, pos, entityType) -> entityType == EntityType.OCELOT || entityType == EntityType.PARROT)
						.isSuffocating((blockState, level, pos) -> false)
						.isViewBlocking((blockState, level, pos) -> false);

		public static final Supplier<AbstractBlock.Properties> GINKGO_PLANKS_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_ORANGE)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> PALM_PLANKS_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<AbstractBlock.Properties> PEACH_PLANKS_PROPERTIES = () ->
				AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_MAGENTA)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);


		public static final BlockEntry<WarpedWartBlock> WARPED_WART = new BlockEntry<>(
				"warped_wart", WarpedWartBlock.PROPERTIES, WarpedWartBlock::new
		);

		public static final BlockEntry<FlowerBlock> CYAN_PETUNIA = new BlockEntry<>(
				"cyan_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(Effects.WATER_BREATHING, 8, props)
		);
		public static final BlockEntry<FlowerBlock> MAGENTA_PETUNIA = new BlockEntry<>(
				"magenta_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(Effects.WATER_BREATHING, 8, props)
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

		//GINKGO
		public static final BlockEntry<SaplingBlock> GINKGO_SAPLING = new BlockEntry<>(
				"ginkgo_sapling", SAPLING_PROPERTIES, (props) -> new SaplingBlock(new GinkgoTreeGrower(), props)
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

		//PALM
		public static final BlockEntry<SaplingBlock> PALM_SAPLING = new BlockEntry<>(
				"palm_sapling", SAPLING_PROPERTIES, (props) -> new SaplingBlock(new PalmTreeGrower(), props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_PALM_SAPLING = new BlockEntry<>(
				"potted_palm_sapling", POTTED_FLOWER_PROPERTIES, (props) -> new FlowerPotBlock(
						null, PALM_SAPLING, props
				)
		);
		public static final BlockEntry<RotatedPillarBlock> PALM_LOG = new BlockEntry<>(
				"palm_log", PALM_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PALM_LOG = new BlockEntry<>(
				"stripped_palm_log", STRIPPED_PALM_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> PALM_WOOD = new BlockEntry<>(
				"palm_wood", PALM_WOOD_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PALM_WOOD = new BlockEntry<>(
				"stripped_palm_wood", STRIPPED_PALM_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<LeavesBlock> PALM_LEAVES = new BlockEntry<>(
				"palm_leaves", LEAVES_PROPERTIES, LeavesBlock::new
		);

		public static final BlockEntry<Block> PALM_PLANKS = new BlockEntry<>(
				"palm_planks", PALM_PLANKS_PROPERTIES, Block::new
		);

		//PEACH
		public static final BlockEntry<SaplingBlock> PEACH_SAPLING = new BlockEntry<>(
				"peach_sapling", SAPLING_PROPERTIES, (props) -> new SaplingBlock(new PeachTreeGrower(), props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_PEACH_SAPLING = new BlockEntry<>(
				"potted_peach_sapling", POTTED_FLOWER_PROPERTIES, (props) -> new FlowerPotBlock(
						null, PEACH_SAPLING, props
				)
		);
		public static final BlockEntry<RotatedPillarBlock> PEACH_LOG = new BlockEntry<>(
				"peach_log", PEACH_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PEACH_LOG = new BlockEntry<>(
				"stripped_peach_log", STRIPPED_PEACH_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> PEACH_WOOD = new BlockEntry<>(
				"peach_wood", PEACH_WOOD_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PEACH_WOOD = new BlockEntry<>(
				"stripped_peach_wood", STRIPPED_PEACH_LOG_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<LeavesBlock> PEACH_LEAVES = new BlockEntry<>(
				"peach_leaves", LEAVES_PROPERTIES, LeavesBlock::new
		);

		public static final BlockEntry<Block> PEACH_PLANKS = new BlockEntry<>(
				"peach_planks", PEACH_PLANKS_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> MOSS_CARPET = new BlockEntry<>(
				"moss_carpet",
				() -> AbstractBlock.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN).strength(0.1F).sound(SoundType.GRASS),
				MossCarpetBlock::new
		);
		public static final BlockEntry<Block> MOSS_BLOCK = new BlockEntry<>(
				"moss_block",
				() -> AbstractBlock.Properties.of(Material.GRASS, MaterialColor.COLOR_GREEN).strength(0.1F).sound(SoundType.GRASS),
				MossBlock::new
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
			ECItems.REGISTER.register(PALM_SAPLING.getId().getPath(), () -> new BlockItem(PALM_SAPLING.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PALM_LOG.getId().getPath(), () -> new BlockItem(PALM_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_PALM_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_PALM_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PALM_WOOD.getId().getPath(), () -> new BlockItem(PALM_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_PALM_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_PALM_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PALM_LEAVES.getId().getPath(), () -> new BlockItem(PALM_LEAVES.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PALM_PLANKS.getId().getPath(), () -> new BlockItem(PALM_PLANKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PEACH_SAPLING.getId().getPath(), () -> new BlockItem(PEACH_SAPLING.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PEACH_LOG.getId().getPath(), () -> new BlockItem(PEACH_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_PEACH_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_PEACH_LOG.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PEACH_WOOD.getId().getPath(), () -> new BlockItem(PEACH_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(STRIPPED_PEACH_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_PEACH_WOOD.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PEACH_LEAVES.getId().getPath(), () -> new BlockItem(PEACH_LEAVES.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(PEACH_PLANKS.getId().getPath(), () -> new BlockItem(PEACH_PLANKS.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(MOSS_CARPET.getId().getPath(), () -> new BlockItem(MOSS_CARPET.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));
			ECItems.REGISTER.register(MOSS_BLOCK.getId().getPath(), () -> new BlockItem(MOSS_BLOCK.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP)));


			registerStairs(Plant.GINKGO_PLANKS);
			registerSlab(Plant.GINKGO_PLANKS);
			registerWoodenFence(Plant.GINKGO_PLANKS);
			registerWoodenFenceGate(Plant.GINKGO_PLANKS);
			registerWoodenDoor(Plant.GINKGO_PLANKS);
			registerWoodenTrapDoor(Plant.GINKGO_PLANKS);
			registerWoodPressurePlate(Plant.GINKGO_PLANKS);
			registerWoodButton(Plant.GINKGO_PLANKS);
			registerSign(Plant.GINKGO_PLANKS, ECWoodType.GINKGO);
			registerStairs(Plant.PALM_PLANKS);
			registerSlab(Plant.PALM_PLANKS);
			registerWoodenFence(Plant.PALM_PLANKS);
			registerWoodenFenceGate(Plant.PALM_PLANKS);
			registerWoodenDoor(Plant.PALM_PLANKS);
			registerWoodenTrapDoor(Plant.PALM_PLANKS);
			registerWoodPressurePlate(Plant.PALM_PLANKS);
			registerWoodButton(Plant.PALM_PLANKS);
			registerSign(Plant.PALM_PLANKS, ECWoodType.PALM);
			registerStairs(Plant.PEACH_PLANKS);
			registerSlab(Plant.PEACH_PLANKS);
			registerWoodenFence(Plant.PEACH_PLANKS);
			registerWoodenFenceGate(Plant.PEACH_PLANKS);
			registerWoodenDoor(Plant.PEACH_PLANKS);
			registerWoodenTrapDoor(Plant.PEACH_PLANKS);
			registerWoodPressurePlate(Plant.PEACH_PLANKS);
			registerWoodButton(Plant.PEACH_PLANKS);
			registerSign(Plant.PEACH_PLANKS, ECWoodType.PEACH);
		}
	}

	public static final Map<ResourceLocation, BlockEntry<SlabBlock>> TO_SLAB = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<StairsBlock>> TO_STAIRS = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<WallBlock>> TO_WALL = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<FenceBlock>> TO_FENCE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<FenceGateBlock>> TO_FENCE_GATE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<DoorBlock>> TO_DOOR = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<TrapDoorBlock>> TO_TRAPDOOR = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<PressurePlateBlock>> TO_PRESSURE_PLATE = new HashMap<>();
	public static final Map<ResourceLocation, BlockEntry<AbstractButtonBlock>> TO_BUTTON = new HashMap<>();
	public static final Map<ResourceLocation, Tuple<BlockEntry<StandingSignBlock>, BlockEntry<WallSignBlock>>> TO_SIGN = new HashMap<>();

	public static final class BlockEntry<T extends Block> implements Supplier<T>, IItemProvider {
		private final RegistryObject<T> regObject;
		private final Supplier<AbstractBlock.Properties> properties;

		public static BlockEntry<Block> simple(String name, Supplier<AbstractBlock.Properties> properties, Consumer<Block> extra) {
			return new BlockEntry<>(name, properties, p -> Util.make(new Block(p), extra));
		}

		public BlockEntry(String name, Supplier<AbstractBlock.Properties> properties, Function<AbstractBlock.Properties, T> make) {
			this.properties = properties;
			this.regObject = REGISTER.register(name, () -> make.apply(properties.get()));
		}

		@Override
		public T get() {
			return regObject.get();
		}

		public BlockState defaultBlockState() {
			return get().defaultBlockState();
		}

		public ResourceLocation getId() {
			return regObject.getId();
		}

		public AbstractBlock.Properties getProperties()
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