package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.blocks.nylium.PurpuraceusNyliumBlock;
import com.hexagram2021.emeraldcraft.common.blocks.plant.CabbageBlock;
import com.hexagram2021.emeraldcraft.common.blocks.plant.ChiliBlock;
import com.hexagram2021.emeraldcraft.common.blocks.plant.HiganBanaFlowerBlock;
import com.hexagram2021.emeraldcraft.common.blocks.plant.WarpedWartBlock;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.*;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.world.grower.GinkgoTreeGrower;
import com.hexagram2021.emeraldcraft.common.world.grower.PalmTreeGrower;
import com.hexagram2021.emeraldcraft.common.world.grower.PeachTreeGrower;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings("unused")
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
		String name = changeNameTo(Objects.requireNonNull(getRegistryName(fullBlock)).getPath(), "_stairs");
		TO_STAIRS.put(getRegistryName(fullBlock), new BlockEntry<>(
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
	private static void registerStairs(ResourceLocation fullBlockRegistryName, String fullBlockName, BlockBehaviour.Properties props,
									   Supplier<BlockState> defaultBlockState, Supplier<Boolean> addToTab) {
		String name = changeNameTo(fullBlockName, "_stairs");
		BlockEntry<StairBlock> blockEntry = new BlockEntry<>(
				name,
				() -> props,
				p -> new StairBlock(defaultBlockState, p)
		);
		if(addToTab.get()) {
			TO_STAIRS.put(fullBlockRegistryName, blockEntry);
		} else {
			ECItems.REGISTER.register(blockEntry.getId().getPath(), () -> new BlockItem(blockEntry.get(), new Item.Properties()));
		}
	}

	private static void registerSlab(Block fullBlock) {
		String name = changeNameTo(Objects.requireNonNull(getRegistryName(fullBlock)).getPath(), "_slab");
		TO_SLAB.put(getRegistryName(fullBlock), new BlockEntry<>(
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
	@SuppressWarnings("SameParameterValue")
	private static void registerSlab(ResourceLocation fullBlockRegistryName, String fullBlockName, BlockBehaviour.Properties props,
									 Supplier<BlockState> defaultBlockState, Supplier<Boolean> addToTab) {
		String name = changeNameTo(fullBlockName, "_slab");
		BlockEntry<SlabBlock> blockEntry = new BlockEntry<>(
				name,
				() -> props,
				p -> new SlabBlock(p.isSuffocating((state, world, pos) ->
						defaultBlockState.get().isSuffocating(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
				).isRedstoneConductor((state, world, pos) ->
						defaultBlockState.get().isRedstoneConductor(world, pos) && state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
				)
				)
		);
		if(addToTab.get()) {
			TO_SLAB.put(fullBlockRegistryName, blockEntry);
		} else {
			ECItems.REGISTER.register(blockEntry.getId().getPath(), () -> new BlockItem(blockEntry.get(), new Item.Properties()));
		}
	}

	private static void registerWall(Block fullBlock) {
		String name = changeNameTo(Objects.requireNonNull(getRegistryName(fullBlock)).getPath(), "_wall");
		TO_WALL.put(getRegistryName(fullBlock), new BlockEntry<>(
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
	private static void registerWall(ResourceLocation fullBlockRegistryName, String fullBlockName, BlockBehaviour.Properties props, Supplier<Boolean> addToTab) {
		String name = changeNameTo(fullBlockName, "_wall");
		BlockEntry<WallBlock> blockEntry = new BlockEntry<>(
				name,
				() -> props,
				WallBlock::new
		);
		if(addToTab.get()) {
			TO_WALL.put(fullBlockRegistryName, blockEntry);
		} else {
			ECItems.REGISTER.register(blockEntry.getId().getPath(), () -> new BlockItem(blockEntry.get(), new Item.Properties()));
		}
	}

	private static <T extends Block> void registerFence(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence");
		TO_FENCE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).mapColor(fullBlock.get().defaultMapColor()),
				FenceBlock::new
		));
	}

	private static <T extends Block> void registerFenceGate(BlockEntry<T> fullBlock) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_fence_gate");
		TO_FENCE_GATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new FenceGateBlock(props, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN)
		));
	}

	private static <T extends Block> void registerDoor(BlockEntry<T> fullBlock, BlockSetType blockSetType) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_door");
		TO_DOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new DoorBlock(props, blockSetType)
		));
	}

	private static <T extends Block> void registerTrapDoor(BlockEntry<T> fullBlock, BlockSetType blockSetType) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_trapdoor");
		TO_TRAPDOOR.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new TrapDoorBlock(props, blockSetType)
		));
	}

	private static <T extends Block> void registerWoodPressurePlate(BlockEntry<T> fullBlock, BlockSetType blockSetType) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_pressure_plate");
		TO_PRESSURE_PLATE.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, props, blockSetType)
		));
	}

	private static <T extends Block> void registerWoodButton(BlockEntry<T> fullBlock, BlockSetType blockSetType) {
		String name = changeNameTo(fullBlock.getId().getPath(), "_button");
		TO_BUTTON.put(fullBlock.getId(), new BlockEntry<>(
				name,
				() -> BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new ButtonBlock(props, blockSetType, 30, true)
		));
	}

	private static <T extends Block> void registerSign(BlockEntry<T> fullBlock, WoodType woodType) {
		String name1 = changeNameTo(fullBlock.getId().getPath(), "_sign");
		String name2 = changeNameTo(fullBlock.getId().getPath(), "_wall_sign");
		TO_SIGN.put(fullBlock.getId(), new Tuple<>(new BlockEntry<>(
				name1,
				() -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).sound(SoundType.WOOD).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new StandingSignBlock(props, woodType)
		), new BlockEntry<>(
				name2,
				() -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).sound(SoundType.WOOD).mapColor(fullBlock.get().defaultMapColor()),
				(props) -> new WallSignBlock(props, woodType)
		)));
	}

	private static <T extends Block> void addColoredBlockToList(List<BlockEntry<?>> list, DyeColor color, BlockEntry<T> colorlessBlock,
																Function<DyeColor, Supplier<BlockBehaviour.Properties>> properties,
																Function<BlockBehaviour.Properties, T> factory) {
		list.add(new BlockEntry<>(color.getName() + "_" + colorlessBlock.getId().getPath(), properties.apply(color), factory));
	}

	@SuppressWarnings("SameParameterValue")
	private static <T extends Block> List<BlockEntry<?>> registerAllColors(BlockEntry<T> colorlessBlock,
																		   Function<DyeColor, Supplier<BlockBehaviour.Properties>> properties,
																		   Function<BlockBehaviour.Properties, T> factory) {
		List<BlockEntry<?>> list = new ArrayList<>();
		/* 不会吧，不会真有mod会添加或修改原版DyeColor吧，我怕了还不行吗 */
		addColoredBlockToList(list, DyeColor.WHITE, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.ORANGE, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.MAGENTA, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.LIGHT_BLUE, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.YELLOW, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.LIME, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.PINK, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.GRAY, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.LIGHT_GRAY, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.CYAN, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.PURPLE, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.BLUE, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.BROWN, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.GREEN, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.RED, colorlessBlock, properties, factory);
		addColoredBlockToList(list, DyeColor.BLACK, colorlessBlock, properties, factory);
		TO_COLORS.put(colorlessBlock.getId(), list);
		return list;
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);

		MineralDecoration.init();
		WorkStation.init();
		//SculptureDecoration.init();
		Decoration.init();
		Plant.init();

		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<SlabBlock>> blockSlab : ECBlocks.TO_SLAB.entrySet()) {
			ECItems.ItemEntry.register(blockSlab.getValue().getId().getPath(), () ->
					new BlockItem(blockSlab.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<StairBlock>> blockStairs : ECBlocks.TO_STAIRS.entrySet()) {
			ECItems.ItemEntry.register(blockStairs.getValue().getId().getPath(), () ->
					new BlockItem(blockStairs.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<WallBlock>> blockWall : ECBlocks.TO_WALL.entrySet()) {
			ECItems.ItemEntry.register(blockWall.getValue().getId().getPath(), () ->
					new BlockItem(blockWall.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<FenceBlock>> blockFence : ECBlocks.TO_FENCE.entrySet()) {
			ECItems.ItemEntry.register(blockFence.getValue().getId().getPath(), () ->
					new BlockItem(blockFence.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<FenceGateBlock>> blockFenceGate : ECBlocks.TO_FENCE_GATE.entrySet()) {
			ECItems.ItemEntry.register(blockFenceGate.getValue().getId().getPath(), () ->
					new BlockItem(blockFenceGate.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<DoorBlock>> blockDoor : ECBlocks.TO_DOOR.entrySet()) {
			ECItems.ItemEntry.register(blockDoor.getValue().getId().getPath(), () ->
					new BlockItem(blockDoor.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<TrapDoorBlock>> blockTrapDoor : ECBlocks.TO_TRAPDOOR.entrySet()) {
			ECItems.ItemEntry.register(blockTrapDoor.getValue().getId().getPath(), () ->
					new BlockItem(blockTrapDoor.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<PressurePlateBlock>> blockPressurePlate : ECBlocks.TO_PRESSURE_PLATE.entrySet()) {
			ECItems.ItemEntry.register(blockPressurePlate.getValue().getId().getPath(), () ->
					new BlockItem(blockPressurePlate.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, ECBlocks.BlockEntry<ButtonBlock>> blockButton : ECBlocks.TO_BUTTON.entrySet()) {
			ECItems.ItemEntry.register(blockButton.getValue().getId().getPath(), () ->
					new BlockItem(blockButton.getValue().get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, Tuple<BlockEntry<StandingSignBlock>, BlockEntry<WallSignBlock>>> blockSign :
				ECBlocks.TO_SIGN.entrySet()) {
			ECItems.ItemEntry.register(blockSign.getValue().getA().getId().getPath(), () ->
					new SignItem(new Item.Properties(),
							blockSign.getValue().getA().get(),
							blockSign.getValue().getB().get()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
		}
		for(Map.Entry<ResourceLocation, List<BlockEntry<?>>> colorBlock: ECBlocks.TO_COLORS.entrySet()) {
			for(BlockEntry<?> blockEntry: colorBlock.getValue()) {
				ECItems.ItemEntry.register(blockEntry.getId().getPath(), () -> new BlockItem(blockEntry.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			}
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

			//Create
			Supplier<Boolean> createCompatPredicate = () -> ModsLoadedEventSubscriber.CREATE;

			registerStairs(
					new ResourceLocation("create", "zinc_block"),
					"zinc_block",
					BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.GLOW_LICHEN),
					Blocks.AIR::defaultBlockState,
					createCompatPredicate
			);
			registerSlab(
					new ResourceLocation("create", "zinc_block"),
					"zinc_block",
					BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.GLOW_LICHEN),
					Blocks.AIR::defaultBlockState,
					createCompatPredicate
			);
			registerWall(
					new ResourceLocation("create", "zinc_block"),
					"zinc_block",
					BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.GLOW_LICHEN),
					createCompatPredicate
			);

			//ImmersiveEngineering
			Supplier<Boolean> ieCompatPredicate = () -> ModsLoadedEventSubscriber.IE;

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_aluminum"),
					"aluminum_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_aluminum"),
					"aluminum_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_lead"),
					"lead_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_lead"),
					"lead_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_silver"),
					"silver_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_silver"),
					"silver_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_nickel"),
					"nickel_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_nickel"),
					"nickel_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_uranium"),
					"uranium_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_uranium"),
					"uranium_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_constantan"),
					"constantan_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_constantan"),
					"constantan_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_electrum"),
					"electrum_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_electrum"),
					"electrum_block",
					BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);

			registerStairs(
					new ResourceLocation("immersiveengineering", "storage_steel"),
					"steel_block",
					BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					Blocks.AIR::defaultBlockState,
					ieCompatPredicate
			);
			registerWall(
					new ResourceLocation("immersiveengineering", "storage_steel"),
					"steel_block",
					BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 10.0F).requiresCorrectToolForDrops(),
					ieCompatPredicate
			);
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
		public static final BlockEntry<RabbleFurnaceBlock> RABBLE_FURNACE = new BlockEntry<>(
				"rabble_furnace", RabbleFurnaceBlock.PROPERTIES, RabbleFurnaceBlock::new
		);
		public static final BlockEntry<CookstoveBlock> COOKSTOVE = new BlockEntry<>(
				"cookstove", CookstoveBlock.PROPERTIES, CookstoveBlock::new
		);
		public static final BlockEntry<MeatGrinderBlock> MEAT_GRINDER = new BlockEntry<>(
				"meat_grinder", MeatGrinderBlock.PROPERTIES, MeatGrinderBlock::new
		);

		private static void init() {
			ECItems.ItemEntry.register(
					CARPENTRY_TABLE.getId().getPath(),
					() -> new BlockItem(CARPENTRY_TABLE.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.carpentry_table").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					GLASS_KILN.getId().getPath(),
					() -> new BlockItem(GLASS_KILN.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.glass_kiln").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					MINERAL_TABLE.getId().getPath(),
					() -> new BlockItem(MINERAL_TABLE.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.mineral_table").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					CRYSTALBALL_TABLE.getId().getPath(),
					() -> new BlockItem(CRYSTALBALL_TABLE.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.crystalball_table").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					SQUEEZER.getId().getPath(),
					() -> new BlockItem(SQUEEZER.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.squeezer").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					CONTINUOUS_MINER.getId().getPath(),
					() -> new BlockItem(CONTINUOUS_MINER.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.continuous_miner").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					ICE_MAKER.getId().getPath(),
					() -> new BlockItem(ICE_MAKER.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.ice_maker").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					MELTER.getId().getPath(),
					() -> new BlockItem(MELTER.get(), new Item.Properties()) {
						@Override
						public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
							components.add(Component.translatable("desc.emeraldcraft.melter").withStyle(ChatFormatting.GRAY));
						}
					}, ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					RABBLE_FURNACE.getId().getPath(),
					() -> new BlockItem(RABBLE_FURNACE.get(), new Item.Properties()),
					ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					COOKSTOVE.getId().getPath(),
					() -> new BlockItem(COOKSTOVE.get(), new Item.Properties()),
					ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
			);
			ECItems.ItemEntry.register(
					MEAT_GRINDER.getId().getPath(),
					() -> new BlockItem(MEAT_GRINDER.get(), new Item.Properties()),
					ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS
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
			ECItems.ItemEntry.register(VILLAGER_SCULPTURE.getId().getPath(), () -> new BlockItem(VILLAGER_SCULPTURE.get(), new Item.Properties()));
			ECItems.ItemEntry.register(IRON_GOLEM_SCULPTURE.getId().getPath(), () -> new BlockItem(IRON_GOLEM_SCULPTURE.get(), new Item.Properties()));
			ECItems.ItemEntry.register(WOLF_SCULPTURE.getId().getPath(), () -> new BlockItem(WOLF_SCULPTURE.get(), new Item.Properties()));
			ECItems.ItemEntry.register(CAT_SCULPTURE.getId().getPath(), () -> new BlockItem(CAT_SCULPTURE.get(), new Item.Properties()));
		}
	}
	//*/

	public static final class Decoration {
		public static final Supplier<BlockBehaviour.Properties> AZURE_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.COLOR_LIGHT_BLUE)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> QUARTZ_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.COLOR_LIGHT_GRAY)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> JADEITE_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.COLOR_LIGHT_GREEN)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> EMERY_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SNARE).mapColor(MapColor.COLOR_BLACK)
						.strength(0.5F).sound(SoundType.SAND);
		public static final Supplier<BlockBehaviour.Properties> DARK_SAND_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_BROWN)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.SAND)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> AZURE_SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_LIGHT_BLUE)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> QUARTZ_SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_LIGHT_GRAY)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> JADEITE_SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_LIGHT_GREEN)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> EMERY_SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_BLACK)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> DARK_SANDSTONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_BROWN)
						.strength(0.8F);
		public static final Supplier<BlockBehaviour.Properties> BLUE_NETHER_BRICKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_CYAN)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS);
		public static final Supplier<BlockBehaviour.Properties> PURPLE_NETHER_BRICKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_PURPLE)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS);
		public static final Supplier<BlockBehaviour.Properties> CRIMSON_STONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.CRIMSON_NYLIUM)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE);
		public static final Supplier<BlockBehaviour.Properties> WARPED_STONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.WARPED_NYLIUM)
						.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE);

		public static final Supplier<BlockBehaviour.Properties> STONE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE)
						.requiresCorrectToolForDrops().strength(1.5F, 6.0F);

		public static final Supplier<BlockBehaviour.Properties> RESIN_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW)
						.strength(0.5F).noOcclusion().sound(SoundType.FROGLIGHT);
		public static final Supplier<BlockBehaviour.Properties> REINFORCED_RESIN_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE)
						.requiresCorrectToolForDrops().strength(30.0F, 600.0F).sound(SoundType.FROGLIGHT);

		public static final Supplier<BlockBehaviour.Properties> RAW_BERYL_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)
						.requiresCorrectToolForDrops().strength(1.5F);

		public static final Supplier<BlockBehaviour.Properties> PAPER_BLOCK_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)
						.strength(0.2F).instabreak().sound(SoundType.WOOL);

		public static final Function<DyeColor, Supplier<BlockBehaviour.Properties>> COLORED_REINFORCED_RESIN_PROPERTIES = (color) ->
				() -> BlockBehaviour.Properties.of().mapColor(color)
						.requiresCorrectToolForDrops().strength(30.0F, 600.0F).sound(SoundType.FROGLIGHT);

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
				"azure_sandstone", AZURE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> QUARTZ_SANDSTONE = new BlockEntry<>(
				"quartz_sandstone", QUARTZ_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> JADEITE_SANDSTONE = new BlockEntry<>(
				"jadeite_sandstone", JADEITE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> EMERY_SANDSTONE = new BlockEntry<>(
				"emery_sandstone", EMERY_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> DARK_SANDSTONE = new BlockEntry<>(
				"dark_sandstone", DARK_SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> SMOOTH_AZURE_SANDSTONE = new BlockEntry<>(
				"smooth_azure_sandstone", AZURE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_QUARTZ_SANDSTONE = new BlockEntry<>(
				"smooth_quartz_sandstone", QUARTZ_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_JADEITE_SANDSTONE = new BlockEntry<>(
				"smooth_jadeite_sandstone", JADEITE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_EMERY_SANDSTONE = new BlockEntry<>(
				"smooth_emery_sandstone", EMERY_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> SMOOTH_DARK_SANDSTONE = new BlockEntry<>(
				"smooth_dark_sandstone", DARK_SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> CUT_AZURE_SANDSTONE = new BlockEntry<>(
				"cut_azure_sandstone", AZURE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_QUARTZ_SANDSTONE = new BlockEntry<>(
				"cut_quartz_sandstone", QUARTZ_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_JADEITE_SANDSTONE = new BlockEntry<>(
				"cut_jadeite_sandstone", JADEITE_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_EMERY_SANDSTONE = new BlockEntry<>(
				"cut_emery_sandstone", EMERY_SANDSTONE_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> CUT_DARK_SANDSTONE = new BlockEntry<>(
				"cut_dark_sandstone", DARK_SANDSTONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> BLUE_NETHER_BRICKS = new BlockEntry<>(
				"blue_nether_bricks", BLUE_NETHER_BRICKS_PROPERTIES, Block::new
		);
		public static final BlockEntry<Block> PURPLE_NETHER_BRICKS = new BlockEntry<>(
				"purple_nether_bricks", PURPLE_NETHER_BRICKS_PROPERTIES, Block::new
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

		public static final BlockEntry<Block> MOSSY_STONE = new BlockEntry<>(
				"mossy_stone", STONE_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> PAPER_BLOCK = new BlockEntry<>(
				"paper_block", PAPER_BLOCK_PROPERTIES, Block::new
		);

		public static final BlockEntry<HalfTransparentBlock> RESIN_BLOCK = new BlockEntry<>(
				"resin_block", RESIN_PROPERTIES, HalfTransparentBlock::new
		);
		public static final BlockEntry<Block> REINFORCED_RESIN_BLOCK = new BlockEntry<>(
				"reinforced_resin_block", REINFORCED_RESIN_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> RAW_BERYL_BLOCK = new BlockEntry<>(
				"raw_beryl_block", RAW_BERYL_PROPERTIES, Block::new
		);

		private static void init() {
			ECItems.ItemEntry.register(VITRIFIED_SAND.getId().getPath(), () -> new BlockItem(VITRIFIED_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(AZURE_SAND.getId().getPath(), () -> new BlockItem(AZURE_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(QUARTZ_SAND.getId().getPath(), () -> new BlockItem(QUARTZ_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(JADEITE_SAND.getId().getPath(), () -> new BlockItem(JADEITE_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(EMERY_SAND.getId().getPath(), () -> new BlockItem(EMERY_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(DARK_SAND.getId().getPath(), () -> new BlockItem(DARK_SAND.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(AZURE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(QUARTZ_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(JADEITE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(EMERY_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(DARK_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(SMOOTH_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_AZURE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(SMOOTH_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_QUARTZ_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(SMOOTH_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_JADEITE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(SMOOTH_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_EMERY_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(SMOOTH_DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(SMOOTH_DARK_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(CUT_AZURE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_AZURE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CUT_QUARTZ_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_QUARTZ_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CUT_JADEITE_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_JADEITE_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CUT_EMERY_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_EMERY_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CUT_DARK_SANDSTONE.getId().getPath(), () -> new BlockItem(CUT_DARK_SANDSTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(BLUE_NETHER_BRICKS.getId().getPath(), () -> new BlockItem(BLUE_NETHER_BRICKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPLE_NETHER_BRICKS.getId().getPath(), () -> new BlockItem(PURPLE_NETHER_BRICKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(CRIMSON_STONE.getId().getPath(), () -> new BlockItem(CRIMSON_STONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CRIMSON_COBBLESTONE.getId().getPath(), () -> new BlockItem(CRIMSON_COBBLESTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(WARPED_STONE.getId().getPath(), () -> new BlockItem(WARPED_STONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(WARPED_COBBLESTONE.getId().getPath(), () -> new BlockItem(WARPED_COBBLESTONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(MOSSY_STONE.getId().getPath(), () -> new BlockItem(MOSSY_STONE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PAPER_BLOCK.getId().getPath(), () -> new BlockItem(PAPER_BLOCK.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			ECItems.ItemEntry.register(RESIN_BLOCK.getId().getPath(), () -> new BlockItem(RESIN_BLOCK.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(REINFORCED_RESIN_BLOCK.getId().getPath(), () -> new BlockItem(REINFORCED_RESIN_BLOCK.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(RAW_BERYL_BLOCK.getId().getPath(), () -> new BlockItem(RAW_BERYL_BLOCK.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

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
			registerStairs(Decoration.PURPLE_NETHER_BRICKS);
			registerStairs(Decoration.CRIMSON_STONE);
			registerStairs(Decoration.CRIMSON_COBBLESTONE);
			registerStairs(Decoration.WARPED_STONE);
			registerStairs(Decoration.WARPED_COBBLESTONE);
			registerStairs(Decoration.MOSSY_STONE);
			registerStairs(Decoration.REINFORCED_RESIN_BLOCK);

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
			registerSlab(Decoration.PURPLE_NETHER_BRICKS);
			registerSlab(Decoration.CRIMSON_STONE);
			registerSlab(Decoration.CRIMSON_COBBLESTONE);
			registerSlab(Decoration.WARPED_STONE);
			registerSlab(Decoration.WARPED_COBBLESTONE);
			registerSlab(Decoration.MOSSY_STONE);
			registerSlab(Decoration.REINFORCED_RESIN_BLOCK);

			registerWall(Decoration.AZURE_SANDSTONE);
			registerWall(Decoration.QUARTZ_SANDSTONE);
			registerWall(Decoration.JADEITE_SANDSTONE);
			registerWall(Decoration.EMERY_SANDSTONE);
			registerWall(Decoration.DARK_SANDSTONE);
			registerWall(Decoration.BLUE_NETHER_BRICKS);
			registerWall(Decoration.PURPLE_NETHER_BRICKS);
			registerWall(Decoration.CRIMSON_STONE);
			registerWall(Decoration.CRIMSON_COBBLESTONE);
			registerWall(Decoration.WARPED_STONE);
			registerWall(Decoration.WARPED_COBBLESTONE);
			registerWall(Decoration.REINFORCED_RESIN_BLOCK);

		//	List<ECBlocks.BlockEntry<?>> list = registerAllColors(Decoration.REINFORCED_RESIN_BLOCK, COLORED_REINFORCED_RESIN_PROPERTIES, Block::new);
		//	list.forEach(ECBlocks::registerStairs);
		//	list.forEach(ECBlocks::registerSlab);
		//	list.forEach(ECBlocks::registerWall);
		}
	}

	public static final class Plant {
		public static final Supplier<BlockBehaviour.Properties> FLOWER_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS);
		public static final Supplier<BlockBehaviour.Properties> HIGAN_BANA_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().randomTicks().sound(SoundType.GRASS);
		public static final Supplier<BlockBehaviour.Properties> SAPLING_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
		public static final Supplier<BlockBehaviour.Properties> POTTED_FLOWER_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
		public static final Supplier<BlockBehaviour.Properties> GINKGO_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor((blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MapColor.TERRACOTTA_ORANGE : MapColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> STRIPPED_GINKGO_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_ORANGE)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> GINKGO_WOOD_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PALM_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor((blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MapColor.TERRACOTTA_BROWN : MapColor.COLOR_BLACK)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> STRIPPED_PALM_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PALM_WOOD_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BLACK)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<BlockBehaviour.Properties> PEACH_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor((blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MapColor.COLOR_MAGENTA : MapColor.TERRACOTTA_YELLOW)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> STRIPPED_PEACH_LOG_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_MAGENTA)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PEACH_WOOD_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_YELLOW)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<BlockBehaviour.Properties> PURPURACEUS_STEM_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor((blockState) ->
								blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
										MapColor.COLOR_PURPLE : MapColor.TERRACOTTA_PURPLE)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> STRIPPED_PURPURACEUS_STEM_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_PURPLE)
						.strength(2.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PURPURACEUS_HYPHAE_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_PURPLE)
						.strength(2.0F).sound(SoundType.WOOD);

		public static final Supplier<BlockBehaviour.Properties> LEAVES_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
						.isValidSpawn((blockState, level, pos, entityType) -> entityType == EntityType.OCELOT || entityType == EntityType.PARROT)
						.isSuffocating((blockState, level, pos) -> false)
						.isViewBlocking((blockState, level, pos) -> false)
						.isRedstoneConductor((blockState, level, pos) -> false)
						.ignitedByLava().pushReaction(PushReaction.DESTROY);

		public static final Supplier<BlockBehaviour.Properties> PURPURACEUS_WART_BLOCKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WART_BLOCK);

		public static final Supplier<BlockBehaviour.Properties> GINKGO_PLANKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_ORANGE)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PALM_PLANKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PEACH_PLANKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_MAGENTA)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> PURPURACEUS_PLANKS_PROPERTIES = () ->
				BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_PURPLE)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD);
		public static final Supplier<BlockBehaviour.Properties> CROP_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks()
						.instabreak().sound(SoundType.CROP);
		public static final Supplier<BlockBehaviour.Properties> WILD_CROP_PROPERTIES = () ->
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission()
						.instabreak().sound(SoundType.CROP);


		public static final BlockEntry<WarpedWartBlock> WARPED_WART = new BlockEntry<>(
				"warped_wart", WarpedWartBlock.PROPERTIES, WarpedWartBlock::new
		);

		public static final BlockEntry<ChiliBlock> CHILI = new BlockEntry<>(
				"chili", CROP_PROPERTIES, ChiliBlock::new
		);
		public static final BlockEntry<CabbageBlock> CABBAGE = new BlockEntry<>(
				"cabbage", CROP_PROPERTIES, CabbageBlock::new
		);

		public static final BlockEntry<BushBlock> WILD_CHILI = new BlockEntry<>(
				"wild_chili", WILD_CROP_PROPERTIES, BushBlock::new
		);
		public static final BlockEntry<Block> WILD_CABBAGE = new BlockEntry<>(
				"wild_cabbage", WILD_CROP_PROPERTIES, BushBlock::new
		);

		public static final BlockEntry<FlowerBlock> CYAN_PETUNIA = new BlockEntry<>(
				"cyan_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(MobEffects.WATER_BREATHING, 8, props)
		);
		public static final BlockEntry<FlowerBlock> MAGENTA_PETUNIA = new BlockEntry<>(
				"magenta_petunia", FLOWER_PROPERTIES, (props) -> new FlowerBlock(MobEffects.WATER_BREATHING, 8, props)
		);
		public static final BlockEntry<HiganBanaFlowerBlock> HIGAN_BANA = new BlockEntry<>(
				"higan_bana", HIGAN_BANA_PROPERTIES, (props) -> new HiganBanaFlowerBlock(MobEffects.LEVITATION, 12, props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_CYAN_PETUNIA = new BlockEntry<>(
				"potted_cyan_petunia", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, CYAN_PETUNIA, props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_MAGENTA_PETUNIA = new BlockEntry<>(
				"potted_magenta_petunia", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, MAGENTA_PETUNIA, props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_HIGAN_BANA = new BlockEntry<>(
				"potted_higan_bana", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, HIGAN_BANA, props)
		);

		//GINKGO
		public static final BlockEntry<SaplingBlock> GINKGO_SAPLING = new BlockEntry<>(
				"ginkgo_sapling", SAPLING_PROPERTIES, (props) -> new SaplingBlock(new GinkgoTreeGrower(), props)
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_GINKGO_SAPLING = new BlockEntry<>(
				"potted_ginkgo_sapling", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, GINKGO_SAPLING, props)
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
				"potted_palm_sapling", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, PALM_SAPLING, props)
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
				"potted_peach_sapling", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, PEACH_SAPLING, props)
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

		//PURPURACEUS
		public static final BlockEntry<Block> PURPURACEUS_NYLIUM = new BlockEntry<>(
				"purpuraceus_nylium",
				() -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_PURPLE)
						.requiresCorrectToolForDrops().strength(0.4F).sound(SoundType.NYLIUM).randomTicks(),
				PurpuraceusNyliumBlock::new
		);

		public static final BlockEntry<FungusBlock> PURPURACEUS_FUNGUS = new BlockEntry<>(
				"purpuraceus_fungus",
				() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instabreak().noCollission().sound(SoundType.FUNGUS).pushReaction(PushReaction.DESTROY),
				(props) -> new FungusBlock(props, ECConfiguredFeatureKeys.TreeConfiguredFeatures.PURPURACEUS_FUNGUS_PLANTED, PURPURACEUS_NYLIUM.get())
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_PURPURACEUS_FUNGUS = new BlockEntry<>(
				"potted_purpuraceus_fungus", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, PURPURACEUS_FUNGUS, props)
		);
		public static final BlockEntry<RotatedPillarBlock> PURPURACEUS_STEM = new BlockEntry<>(
				"purpuraceus_stem", PURPURACEUS_STEM_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PURPURACEUS_STEM = new BlockEntry<>(
				"stripped_purpuraceus_stem", STRIPPED_PURPURACEUS_STEM_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> PURPURACEUS_HYPHAE = new BlockEntry<>(
				"purpuraceus_hyphae", PURPURACEUS_HYPHAE_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<RotatedPillarBlock> STRIPPED_PURPURACEUS_HYPHAE = new BlockEntry<>(
				"stripped_purpuraceus_hyphae", STRIPPED_PURPURACEUS_STEM_PROPERTIES, RotatedPillarBlock::new
		);
		public static final BlockEntry<Block> PURPURACEUS_WART_BLOCK = new BlockEntry<>(
				"purpuraceus_wart_block", PURPURACEUS_WART_BLOCKS_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> PURPURACEUS_PLANKS = new BlockEntry<>(
				"purpuraceus_planks", PURPURACEUS_PLANKS_PROPERTIES, Block::new
		);

		public static final BlockEntry<Block> PURPURACEUS_ROOTS = new BlockEntry<>(
				"purpuraceus_roots",
				() -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().instabreak()
						.noCollission().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY),
				RootsBlock::new
		);
		public static final BlockEntry<FlowerPotBlock> POTTED_PURPURACEUS_ROOTS = new BlockEntry<>(
				"potted_purpuraceus_roots", POTTED_FLOWER_PROPERTIES,
				(props) -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, PURPURACEUS_ROOTS, props)
		);

		private static void registerAllPlanksDecorationBlocks(BlockEntry<? extends Block> planks, WoodType woodType) {
			registerStairs(planks);
			registerSlab(planks);
			registerFence(planks);
			registerFenceGate(planks);
			registerDoor(planks, woodType.setType());
			registerTrapDoor(planks, woodType.setType());
			registerWoodPressurePlate(planks, woodType.setType());
			registerWoodButton(planks, woodType.setType());
			registerSign(planks, woodType);
		}

		private static void init() {
			ECItems.ItemEntry.register(WILD_CHILI.getId().getPath(), () -> new BlockItem(WILD_CHILI.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(WILD_CABBAGE.getId().getPath(), () -> new BlockItem(WILD_CABBAGE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(CYAN_PETUNIA.getId().getPath(), () -> new BlockItem(CYAN_PETUNIA.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(MAGENTA_PETUNIA.getId().getPath(), () -> new BlockItem(MAGENTA_PETUNIA.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(HIGAN_BANA.getId().getPath(), () -> new BlockItem(HIGAN_BANA.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(GINKGO_SAPLING.getId().getPath(), () -> new BlockItem(GINKGO_SAPLING.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(GINKGO_LOG.getId().getPath(), () -> new BlockItem(GINKGO_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_GINKGO_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_GINKGO_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(GINKGO_WOOD.getId().getPath(), () -> new BlockItem(GINKGO_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_GINKGO_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_GINKGO_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(GINKGO_LEAVES.getId().getPath(), () -> new BlockItem(GINKGO_LEAVES.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(GINKGO_PLANKS.getId().getPath(), () -> new BlockItem(GINKGO_PLANKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PALM_SAPLING.getId().getPath(), () -> new BlockItem(PALM_SAPLING.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PALM_LOG.getId().getPath(), () -> new BlockItem(PALM_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PALM_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_PALM_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PALM_WOOD.getId().getPath(), () -> new BlockItem(PALM_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PALM_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_PALM_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PALM_LEAVES.getId().getPath(), () -> new BlockItem(PALM_LEAVES.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PALM_PLANKS.getId().getPath(), () -> new BlockItem(PALM_PLANKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PEACH_SAPLING.getId().getPath(), () -> new BlockItem(PEACH_SAPLING.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PEACH_LOG.getId().getPath(), () -> new BlockItem(PEACH_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PEACH_LOG.getId().getPath(), () -> new BlockItem(STRIPPED_PEACH_LOG.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PEACH_WOOD.getId().getPath(), () -> new BlockItem(PEACH_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PEACH_WOOD.getId().getPath(), () -> new BlockItem(STRIPPED_PEACH_WOOD.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PEACH_LEAVES.getId().getPath(), () -> new BlockItem(PEACH_LEAVES.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PEACH_PLANKS.getId().getPath(), () -> new BlockItem(PEACH_PLANKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_FUNGUS.getId().getPath(), () -> new BlockItem(PURPURACEUS_FUNGUS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_STEM.getId().getPath(), () -> new BlockItem(PURPURACEUS_STEM.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PURPURACEUS_STEM.getId().getPath(), () -> new BlockItem(STRIPPED_PURPURACEUS_STEM.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_HYPHAE.getId().getPath(), () -> new BlockItem(PURPURACEUS_HYPHAE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(STRIPPED_PURPURACEUS_HYPHAE.getId().getPath(), () -> new BlockItem(STRIPPED_PURPURACEUS_HYPHAE.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_WART_BLOCK.getId().getPath(), () -> new BlockItem(PURPURACEUS_WART_BLOCK.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_PLANKS.getId().getPath(), () -> new BlockItem(PURPURACEUS_PLANKS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_NYLIUM.getId().getPath(), () -> new BlockItem(PURPURACEUS_NYLIUM.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);
			ECItems.ItemEntry.register(PURPURACEUS_ROOTS.getId().getPath(), () -> new BlockItem(PURPURACEUS_ROOTS.get(), new Item.Properties()), ECItems.ItemEntry.ItemGroupType.BUILDING_BLOCKS);

			registerAllPlanksDecorationBlocks(GINKGO_PLANKS, ECWoodType.GINKGO);
			registerAllPlanksDecorationBlocks(PALM_PLANKS, ECWoodType.PALM);
			registerAllPlanksDecorationBlocks(PEACH_PLANKS, ECWoodType.PEACH);
			registerAllPlanksDecorationBlocks(PURPURACEUS_PLANKS, ECWoodType.PURPURACEUS);
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
	public static final Map<ResourceLocation, List<BlockEntry<?>>> TO_COLORS = new HashMap<>();

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

		@Override
		public Item asItem()
		{
			return get().asItem();
		}
	}
}