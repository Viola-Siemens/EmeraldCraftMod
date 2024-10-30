package com.hexagram2021.emeraldcraft.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings({"deprecation", "unused", "UnusedReturnValue", "OptionalGetWithoutIsPresent", "DataFlowIssue"})
public interface RegistryChecker {
	Set<Block> WHITELIST_NO_LOOT_TABLE_BLOCKS = ImmutableSet.of(
			Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR, Blocks.WATER, Blocks.LAVA, Blocks.LIGHT,
			Blocks.PISTON_HEAD, Blocks.MOVING_PISTON, Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.END_PORTAL,
			Blocks.FROSTED_ICE, Blocks.END_GATEWAY, Blocks.END_PORTAL_FRAME, Blocks.COMMAND_BLOCK,
			Blocks.REPEATING_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.STRUCTURE_BLOCK, Blocks.STRUCTURE_VOID,
			Blocks.BUBBLE_COLUMN, Blocks.JIGSAW, Blocks.BARRIER
	);
	Set<Block> WHITELIST_NOT_IN_MINEABLE_TAGS = ImmutableSet.of(
			Blocks.COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK,
			Blocks.STRUCTURE_BLOCK, Blocks.JIGSAW
	);
	Set<Block> WHITELIST_NO_ITEM_BLOCKS = ImmutableSet.of(
			Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR, Blocks.WATER, Blocks.LAVA, Blocks.PISTON_HEAD,
			Blocks.MOVING_PISTON, Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.END_PORTAL, Blocks.NETHER_PORTAL,
			Blocks.CANDLE_CAKE, Blocks.END_GATEWAY, Blocks.FROSTED_ICE, Blocks.BUBBLE_COLUMN
	);

	static void registryCheck(LootDataManager lootDataManager) {
		ForgeRegistries.BLOCKS.forEach(block -> {
			ResourceLocation id = getRegistryName(block);
			Item blockItem = block.asItem();
			if(!WHITELIST_NO_ITEM_BLOCKS.contains(block) && (!id.getPath().startsWith("potted_") && !id.getPath().endsWith("_candle_cake")) && blockItem.equals(Items.AIR)) {
				ECLogger.warn("[Registry Check] No BlockItem registered for block %s.".formatted(id));
			}
			//vanilla tags
			tagCheckSuffix(id, block, blockItem, "_slab", BlockTags.SLABS, ItemTags.SLABS);
			tagCheckSuffix(id, block, blockItem, "_stairs", BlockTags.STAIRS, ItemTags.STAIRS);
			tagCheckSuffix(id, block, blockItem, "_wall", BlockTags.WALLS, ItemTags.WALLS);
			tagCheckSuffix(id, block, blockItem, "_planks", BlockTags.PLANKS, ItemTags.PLANKS);
			tagCheckSuffix(id, block, blockItem, "_button", BlockTags.BUTTONS, ItemTags.BUTTONS);
			tagCheckSuffix(id, block, blockItem, "_door", BlockTags.DOORS, ItemTags.DOORS);
			tagCheckSuffix(id, block, blockItem, "_trapdoor", BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
			tagCheckSuffix(id, block, blockItem, "_fence", BlockTags.FENCES, ItemTags.FENCES);
			tagCheckSuffix(id, block, blockItem, "_fence_gate", BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
			tagCheckSuffix(id, block, "_pressure_plates", BlockTags.PRESSURE_PLATES);
			tagCheckSuffix(id, block, blockItem, "_leaves", BlockTags.LEAVES, ItemTags.LEAVES);
			tagCheckSuffix(id, block, "_wall_hanging_sign", BlockTags.WALL_HANGING_SIGNS).ifFailed(
					() -> tagCheckSuffix(id, block, blockItem, "_hanging_sign", BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS).or(
							tagCheckSuffix(id, block, "_wall_sign", BlockTags.WALL_SIGNS)
					)
			).ifFailed(
					() -> tagCheckSuffix(id, block, blockItem, "_sign", BlockTags.STANDING_SIGNS, ItemTags.SIGNS)
			);
			tagCheckSuffix(id, block, blockItem, "_bed", BlockTags.BEDS, ItemTags.BEDS);
			tagCheckPrefix(id, block, "potted_", BlockTags.FLOWER_POTS).ifFailed(
					() -> tagCheckSuffix(id, block, blockItem, "_sapling", BlockTags.SAPLINGS, ItemTags.SAPLINGS)
			);
			tagCheckSuffix(id, block, blockItem, "_rail", BlockTags.RAILS, ItemTags.RAILS);
			tagCheckSuffix(id, block, "_cauldron", BlockTags.CAULDRONS);

			tagCheckSuffix(id, block, blockItem, "_wool", BlockTags.WOOL, ItemTags.WOOL);
			tagCheckSuffix(id, block, blockItem, "_log", BlockTags.LOGS, ItemTags.LOGS);
			tagCheckSuffix(id, block, blockItem, "_wood", BlockTags.LOGS, ItemTags.LOGS);
			tagCheckSuffix(id, block, blockItem, "_stem", BlockTags.LOGS, ItemTags.LOGS);
			tagCheckSuffix(id, block, blockItem, "_hyphae", BlockTags.LOGS, ItemTags.LOGS);
			tagCheckSuffix(id, block, "_nylium", BlockTags.NYLIUM);
			tagCheckSuffix(id, block, blockItem, "_sand", BlockTags.SAND, ItemTags.SAND);
			tagCheckSuffix(id, "_glazed_terracotta").ifFailed(
					() -> tagCheckSuffix(id, block, blockItem, "_terracotta", BlockTags.TERRACOTTA, ItemTags.TERRACOTTA)
			);

			tagCheckSubstr(id, block, blockItem, "gold", BlockTags.GUARDED_BY_PIGLINS, ItemTags.PIGLIN_LOVED);

			//forge tags
			tagCheckSuffix(id, block, blockItem, "_ore", Tags.Blocks.ORES, Tags.Items.ORES).ifFailed(
					() -> tagCheckPrefix(id, block, blockItem, "ore_", Tags.Blocks.ORES, Tags.Items.ORES)
			);
			tagCheckSuffix(id, blockItem, "_seed", Tags.Items.SEEDS).ifFailed(
					() -> tagCheckPrefix(id, blockItem, "seed_", Tags.Items.SEEDS)
			);

			if(!WHITELIST_NO_LOOT_TABLE_BLOCKS.contains(block)) {
				if(block.getLootTable().equals(BuiltInLootTables.EMPTY) || lootDataManager.getLootTable(block.getLootTable()).equals(LootTable.EMPTY)) {
					ECLogger.warn("[Registry Check] Missing loot table for block %s.".formatted(id));
				}
				if(block.defaultBlockState().requiresCorrectToolForDrops()) {
					checkIn(id, block, "mineable", BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_HOE, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL);
				}
			}
		});
	}

	Set<Item> ENTRANCES = Util.make(Sets.newIdentityHashSet(), set -> set.addAll(ImmutableSet.of(
			Items.AIR, Items.BARRIER, Items.COMMAND_BLOCK, Items.END_PORTAL_FRAME, Items.JIGSAW, Items.LIGHT,
			Items.CHAIN_COMMAND_BLOCK, Items.STRUCTURE_BLOCK, Items.REPEATING_COMMAND_BLOCK, Items.STRUCTURE_VOID,

			Items.SUSPICIOUS_SAND, Items.SUSPICIOUS_GRAVEL, Items.BUNDLE, Items.FILLED_MAP,
			Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.POWDER_SNOW_BUCKET, Items.MILK_BUCKET,
			Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET,
			Items.AXOLOTL_BUCKET, Items.TADPOLE_BUCKET,
			Items.EGG, Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION,
			Items.DRAGON_EGG, Items.DRAGON_BREATH, Items.ENCHANTED_BOOK, Items.HONEYCOMB, Items.SCUTE, Items.NETHER_STAR,
			Items.CHIPPED_ANVIL, Items.DAMAGED_ANVIL,

			Items.CREEPER_HEAD, Items.PIGLIN_HEAD, Items.PLAYER_HEAD, Items.ZOMBIE_HEAD, Items.SKELETON_SKULL,
			Items.ENDER_DRAGON_SPAWN_EGG, Items.WITHER_SPAWN_EGG
	)));

	/**
	 * API for recipe check.
	 * @param item  Natural resource item, not crafted by any other items.
	 */
	static void addEntranceItem(Item item) {
		ENTRANCES.add(item);
	}

	List<Pair<Item, Item>> EXTRA_RELATIONS = Util.make(Lists.newArrayList(), list -> list.addAll(ImmutableList.of(
			Pair.of(Items.COPPER_BLOCK, Items.EXPOSED_COPPER),
			Pair.of(Items.EXPOSED_COPPER, Items.WEATHERED_COPPER),
			Pair.of(Items.WEATHERED_COPPER, Items.OXIDIZED_COPPER)
	)));

	/**
	 * API for recipe check.
	 * @param from  For example, copper_block in copper_block -> exposed_copper relation.
	 * @param to    For example, exposed_copper in copper_block -> exposed_copper relation.
	 */
	static void addRelation(Item from, Item to) {
		EXTRA_RELATIONS.add(Pair.of(from, to));
	}

	@SuppressWarnings("ConstantValue")
	static void recipeCheck(LootDataManager lootDataManager, RecipeManager recipeManager, RegistryAccess registryAccess) {
		ItemGraph graph = new ItemGraph();
		recipeManager.getRecipes().forEach(recipeHolder ->  {
			Recipe<?> recipe = recipeHolder.value();
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			if(recipe instanceof SmithingTransformRecipe smithingTransformRecipe)  {
				ingredients = NonNullList.create();
				ingredients.add(smithingTransformRecipe.template);
				ingredients.add(smithingTransformRecipe.base);
				ingredients.add(smithingTransformRecipe.addition);
			}
			ItemStack result = recipe.getResultItem(registryAccess);
			if(result != null) {
				ingredients.forEach(
						ingredient -> Arrays.stream(ingredient.getItems()).forEach(itemStack -> graph.addEdge(itemStack.getItem(), result.getItem()))
				);
			}
		});
		ForgeRegistries.ENTITY_TYPES.forEach(entityType -> {
			if (!entityType.getDefaultLootTable().equals(BuiltInLootTables.EMPTY)) {
				LootTable table = lootDataManager.getLootTable(entityType.getDefaultLootTable());
				if (!table.equals(LootTable.EMPTY)) {
					table.pools.forEach(pool -> pool.entries.forEach(entry -> {
						if (entry instanceof LootItem lootItem) {
							addEntranceItem(lootItem.item.value());
						}
					}));
				}
			}
		});
		lootDataManager.elements.forEach((id, o) -> {
			if(id.type().equals(LootDataType.TABLE)) {
				LootTable table = (LootTable)o;
				LootContextParamSet type = table.getParamSet();
				if(type.equals(LootContextParamSets.CHEST) ||
						type.equals(LootContextParamSets.ARCHAEOLOGY) ||
						type.equals(LootContextParamSets.FISHING)) {
					if (!table.equals(LootTable.EMPTY)) {
						table.pools.forEach(pool -> pool.entries.forEach(entry -> {
							if (entry instanceof LootItem lootItem) {
								addEntranceItem(lootItem.item.value());
							}
						}));
					}
				}
			}
		});
		EXTRA_RELATIONS.forEach(pair -> graph.addEdge(pair.getLeft(), pair.getRight()));
		Objects.requireNonNull(registryAccess.registry(Registries.CREATIVE_MODE_TAB).get().get(CreativeModeTabs.NATURAL_BLOCKS))
				.displayItemsGenerator.accept(null, (itemStack, ignored) -> addEntranceItem(itemStack.getItem()));
		Objects.requireNonNull(registryAccess.registry(Registries.CREATIVE_MODE_TAB).get().get(CreativeModeTabs.SPAWN_EGGS))
				.displayItemsGenerator.accept(null, (itemStack, ignored) -> addEntranceItem(itemStack.getItem()));

		ForgeRegistries.ITEMS.forEach(item -> {
			addEdgesForLoot(item, graph, lootDataManager);
			if(item instanceof BlockItem blockItem) {
				Block block = blockItem.getBlock();
				BlockState blockState;
				if((blockState = AxeItem.getAxeStrippingState(block.defaultBlockState())) != null) {
					graph.addEdge(item, blockState.getBlock().asItem());
				}
				if((blockState = ShovelItem.getShovelPathingState(block.defaultBlockState())) != null) {
					graph.addEdge(item, blockState.getBlock().asItem());
				}
				if(InfestedBlock.isCompatibleHostBlock(block.defaultBlockState())) {
					graph.addEdge(item, InfestedBlock.infestedStateByHost(block.defaultBlockState()).getBlock().asItem());
				}
				if(block instanceof ConcretePowderBlock concretePowderBlock) {
					graph.addEdge(item, concretePowderBlock.concrete.getBlock().asItem());
				}
			}
		});

		ENTRANCES.forEach(graph::visit);

		ForgeRegistries.ITEMS.forEach(item -> graph.degreeIfNotVisited(item).ifPresent(
				degree -> ECLogger.warn("[Recipe Check] Found a non-natural item %s without any recipes or loot tables (degree = %d).".formatted(getRegistryName(item), degree))
		));
	}

	Set<Item> ITEMS_ALREADY_ADDED_EDGES = Sets.newIdentityHashSet();
	private static void addEdgesForLoot(Item item, ItemGraph graph, LootDataManager lootDataManager) {
		if(ITEMS_ALREADY_ADDED_EDGES.contains(item)) {
			return;
		}
		ITEMS_ALREADY_ADDED_EDGES.add(item);
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (!block.getLootTable().equals(BuiltInLootTables.EMPTY)) {
				LootTable table = lootDataManager.getLootTable(block.getLootTable());
				if (!table.equals(LootTable.EMPTY)) {
					table.pools.forEach(pool -> pool.entries.forEach(entry -> addEdgeForLootTableEntry(item, entry, graph, lootDataManager)));
				}
			}
		}
	}

	private static void addEdgeForLootTableEntry(Item item, LootPoolEntryContainer entryContainer, ItemGraph graph, LootDataManager lootDataManager) {
		if (entryContainer instanceof LootItem lootItem) {
			graph.addEdge(item, lootItem.item.value());
		} else if(entryContainer instanceof CompositeEntryBase compositeEntry) {
			compositeEntry.children.forEach(entry -> addEdgeForLootTableEntry(item, entry, graph, lootDataManager));
		} else if(entryContainer instanceof LootTableReference lootTableReference) {
			LootTable table = lootDataManager.getLootTable(lootTableReference.name);
			table.pools.forEach(pool -> pool.entries.forEach(entry -> addEdgeForLootTableEntry(item, entry, graph, lootDataManager)));
		}
	}

	@OnlyIn(Dist.CLIENT)
	static void i18nCheck() {
		ForgeRegistries.BLOCKS.forEach(block -> {
			ResourceLocation id = getRegistryName(block);
			if(!I18n.exists(block.getDescriptionId())) {
				ECLogger.warn("[I18n Check] Missing I18n for block %s.".formatted(id));
			}
		});
		ForgeRegistries.ITEMS.forEach(item -> {
			ResourceLocation id = getRegistryName(item);
			if(!I18n.exists(item.getDescriptionId())) {
				ECLogger.warn("[I18n Check] Missing I18n for item %s.".formatted(id));
			}
		});
		ForgeRegistries.ENTITY_TYPES.forEach(entityType -> {
			ResourceLocation id = getRegistryName(entityType);
			if(!I18n.exists(entityType.getDescriptionId())) {
				ECLogger.warn("[I18n Check] Missing I18n for entity type %s.".formatted(id));
			}
		});
		ForgeRegistries.SOUND_EVENTS.forEach(soundEvent -> {
			ResourceLocation id = soundEvent.getLocation();
			WeighedSoundEvents weighedSoundEvents = Minecraft.getInstance().getSoundManager().getSoundEvent(id);
			if(weighedSoundEvents != null &&
					weighedSoundEvents.getSubtitle() instanceof MutableComponent translatableComponent &&
					translatableComponent.getContents() instanceof TranslatableContents translatableContents &&
					!I18n.exists(translatableContents.getKey())) {
				ECLogger.warn("[I18n Check] Missing I18n for subtitle of sound %s.".formatted(id));
			}
		});
	}

	static CheckResult tagCheckSubstr(ResourceLocation id, String substr) {
		return new CheckResult(id.getPath().contains(substr));
	}
	static CheckResult tagCheckSubstr(ResourceLocation id, Block block, Item blockItem, String substr, TagKey<Block> blockTag, TagKey<Item> itemTag) {
		if(id.getPath().contains(substr)) {
			boolean error = false;
			substr = substr.replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(substr, id, blockTag));
				error = true;
			}
			if(!blockItem.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(substr, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckSubstr(ResourceLocation id, Block block, String substr, TagKey<Block> blockTag) {
		if(id.getPath().contains(substr)) {
			boolean error = false;
			substr = substr.replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(substr, id, blockTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckSubstr(ResourceLocation id, Item item, String substr, TagKey<Item> itemTag) {
		if(id.getPath().contains(substr)) {
			boolean error = false;
			substr = substr.replace('_', ' ');
			if(!item.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(substr, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}

	static CheckResult tagCheckSuffix(ResourceLocation id, String suffix) {
		return new CheckResult(id.getPath().endsWith(suffix));
	}
	static CheckResult tagCheckSuffix(ResourceLocation id, Block block, Item blockItem, String suffix, TagKey<Block> blockTag, TagKey<Item> itemTag) {
		if(id.getPath().endsWith(suffix)) {
			boolean error = false;
			suffix = suffix.substring(1).replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(suffix, id, blockTag));
				error = true;
			}
			if(!blockItem.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(suffix, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckSuffix(ResourceLocation id, Block block, String suffix, TagKey<Block> blockTag) {
		if(id.getPath().endsWith(suffix)) {
			boolean error = false;
			suffix = suffix.substring(1).replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(suffix, id, blockTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckSuffix(ResourceLocation id, Item item, String suffix, TagKey<Item> itemTag) {
		if(id.getPath().endsWith(suffix)) {
			boolean error = false;
			suffix = suffix.substring(1).replace('_', ' ');
			if(!item.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(suffix, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}

	static CheckResult tagCheckPrefix(ResourceLocation id, String prefix) {
		return new CheckResult(id.getPath().startsWith(prefix));
	}
	static CheckResult tagCheckPrefix(ResourceLocation id, Block block, Item blockItem, String prefix, TagKey<Block> blockTag, TagKey<Item> itemTag) {
		if(id.getPath().startsWith(prefix)) {
			boolean error = false;
			prefix = prefix.substring(0, prefix.length() - 1).replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(prefix, id, blockTag));
				error = true;
			}
			if(!blockItem.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(prefix, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckPrefix(ResourceLocation id, Block block, String prefix, TagKey<Block> blockTag) {
		if(id.getPath().startsWith(prefix)) {
			boolean error = false;
			prefix = prefix.substring(0, prefix.length() - 1).replace('_', ' ');
			if(!block.builtInRegistryHolder().is(blockTag)) {
				ECLogger.warn("[Registry Check] Likely %s block %s is not in tag %s.".formatted(prefix, id, blockTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}
	static CheckResult tagCheckPrefix(ResourceLocation id, Item item, String prefix, TagKey<Item> itemTag) {
		if(id.getPath().startsWith(prefix)) {
			boolean error = false;
			prefix = prefix.substring(0, prefix.length() - 1).replace('_', ' ');
			if(!item.builtInRegistryHolder().is(itemTag)) {
				ECLogger.warn("[Registry Check] Likely %s item %s is not in tag %s.".formatted(prefix, id, itemTag));
				error = true;
			}
			return new CheckResult(true, error);
		}
		return new CheckResult(false);
	}

	@SafeVarargs
	static CheckResult checkIn(ResourceLocation id, Block block, String tagDesc, TagKey<Block>... blockTags) {
		boolean error = true;
		Holder.Reference<Block> holder = block.builtInRegistryHolder();
		for(TagKey<Block> blockTag: blockTags) {
			if(holder.is(blockTag)) {
				error = false;
				break;
			}
		}
		if(error) {
			ECLogger.warn("[Registry Check] Block %s is not in any of the `%s` tags.".formatted(id, tagDesc));
		}
		return new CheckResult(true, error);
	}

	@SafeVarargs
	static CheckResult checkNotIn(ResourceLocation id, Block block, TagKey<Block>... blockTags) {
		boolean error = false;
		Holder.Reference<Block> holder = block.builtInRegistryHolder();
		for(TagKey<Block> blockTag: blockTags) {
			if(holder.is(blockTag)) {
				error = true;
				ECLogger.warn("[Registry Check] Block %s is not supposed to be in tag %s.".formatted(id, blockTag));
				break;
			}
		}
		return new CheckResult(true, error);
	}

	class CheckResult {
		private final boolean matches;
		private final boolean error;

		public CheckResult(boolean matches) {
			this(matches, false);
		}

		public CheckResult(boolean matches, boolean error) {
			this.matches = matches;
			this.error = error;
		}

		public CheckResult ifFailed(Supplier<CheckResult> next) {
			return this.matches ? this : next.get();
		}

		public CheckResult ifError(Supplier<CheckResult> next) {
			return this.error ? this : next.get();
		}

		public CheckResult or(CheckResult other) {
			return new CheckResult(this.matches || other.matches, this.error || other.error);
		}
	}
}
