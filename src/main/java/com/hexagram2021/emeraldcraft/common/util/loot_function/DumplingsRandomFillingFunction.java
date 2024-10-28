package com.hexagram2021.emeraldcraft.common.util.loot_function;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.items.capabilities.FarciFoodStorage;
import com.hexagram2021.emeraldcraft.common.register.ECItemTags;
import com.hexagram2021.emeraldcraft.common.register.ECLootItemFunctions;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "deprecation"})
public class DumplingsRandomFillingFunction extends LootItemConditionalFunction {
	private static final Codec<List<Item>> ITEMS_CODEC = ForgeRegistries.ITEMS.getCodec().listOf();

	public static final Codec<DumplingsRandomFillingFunction> CODEC = RecordCodecBuilder.create(
			instance -> commonFields(instance).and(ExtraCodecs.strictOptionalField(ITEMS_CODEC, "fillings").forGetter(function -> function.fillings))
					.apply(instance, DumplingsRandomFillingFunction::new)
	);

	private final Optional<List<Item>> fillings;

	protected DumplingsRandomFillingFunction(List<LootItemCondition> predicates, Optional<List<Item>> fillings) {
		super(predicates);
		this.fillings = fillings;
	}

	private static final List<Item> DEFAULT_ALL_MEAT = List.of();
	private static final List<Item> DEFAULT_ALL_VEGETABLES = List.of();

	private static final List<Item> CACHED_ALL_MEAT = Lists.newArrayList();
	private static final List<Item> CACHED_ALL_VEGETABLES = Lists.newArrayList();

	private static final int POSSIBILITY_VEGETARIAN_DUMPLINGS_EGG = 30;
	private static final int POSSIBILITY_SECONDARY_VEGETABLE = 0;
	private static final int POSSIBILITY_MEATY_DUMPLINGS_VEGETABLE = 80;
	private static final int POSSIBILITY_MEATY_DUMPLINGS_EGG = 20;

	@Override
	protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
		return buildFillings(
				itemStack, lootContext.getRandom(), this.fillings,
				POSSIBILITY_VEGETARIAN_DUMPLINGS_EGG, POSSIBILITY_SECONDARY_VEGETABLE, POSSIBILITY_MEATY_DUMPLINGS_VEGETABLE, POSSIBILITY_MEATY_DUMPLINGS_EGG,
				() -> ECLogger.warn("Couldn't find a compatible filling for %s from loot table %s.".formatted(itemStack, lootContext.getQueriedLootTableId()))
		);
	}

	@Override
	public LootItemFunctionType getType() {
		return ECLootItemFunctions.DUMPLINGS_RANDOM_FILLINGS;
	}

	private static <T> T getRandomElement(RandomSource random, List<? extends T> list) {
		int index = random.nextInt(list.size());
		return list.get(index);
	}

	public static ItemStack buildFillings(ItemStack itemStack, RandomSource random, Optional<List<Item>> fillingsOptional,
										  int possibilityVegetarianDumplingsEgg, int possibilitySecondaryVegetable,
										  int possibilityMeatyDumplingsVegetable, int possibilityMeatyDumplingsEgg,
										  Runnable onFailure) {
		List<Item> meats, vegetables;
		if(fillingsOptional.isPresent()) {
			List<Item> fillings = fillingsOptional.get();
			meats = fillings.stream().filter(item -> item.builtInRegistryHolder().is(ECItemTags.MINCE)).toList();
			vegetables = fillings.stream().filter(item -> item.builtInRegistryHolder().is(ECItemTags.VEGETABLE_FILLINGS)).toList();
		} else {
			if(CACHED_ALL_MEAT.isEmpty()) {
				meats = BuiltInRegistries.ITEM.getTag(ECItemTags.MINCE)
						.map(named -> named.stream().map(Holder::value).toList()).orElse(DEFAULT_ALL_MEAT);
				if(!meats.isEmpty()) {
					CACHED_ALL_MEAT.addAll(meats);
				}
			} else {
				meats = CACHED_ALL_MEAT;
			}
			if(CACHED_ALL_VEGETABLES.isEmpty()) {
				vegetables = BuiltInRegistries.ITEM.getTag(ECItemTags.VEGETABLE_FILLINGS)
						.map(named -> named.stream().map(Holder::value).toList()).orElse(DEFAULT_ALL_VEGETABLES);
				if(!vegetables.isEmpty()) {
					CACHED_ALL_VEGETABLES.addAll(vegetables);
				}
			} else {
				vegetables = CACHED_ALL_VEGETABLES;
			}
		}
		boolean egg = vegetables.contains(Items.EGG);
		boolean meatsEmpty = meats.isEmpty();
		boolean vegetablesEmpty = vegetables.isEmpty();
		if(meatsEmpty && vegetablesEmpty) {
			onFailure.run();
		} else {
			ListTag list = new ListTag();
			StringTag eggTag = StringTag.valueOf(getRegistryName(Items.EGG).toString());
			if(meatsEmpty || (!vegetablesEmpty && random.nextInt(meats.size() + 1) == 0)) {
				//vegetarian
				ResourceLocation randomVegetable = getRegistryName(getRandomElement(random, vegetables));
				list.add(StringTag.valueOf(randomVegetable.toString()));
				if(egg && random.nextInt(100) < possibilityVegetarianDumplingsEgg) {
					list.add(eggTag);
				}
			} else {
				//meaty
				ResourceLocation randomMeat = getRegistryName(getRandomElement(random, meats));
				list.add(StringTag.valueOf(randomMeat.toString()));
				if(!vegetablesEmpty && random.nextInt(100) < possibilityMeatyDumplingsVegetable) {
					ResourceLocation randomVegetable = getRegistryName(getRandomElement(random, vegetables));
					list.add(StringTag.valueOf(randomVegetable.toString()));
					if(egg && random.nextInt(100) < possibilityMeatyDumplingsEgg) {
						list.add(eggTag);
					}
				}
			}
			if(vegetables.size() >= 4 && random.nextInt(100) < possibilitySecondaryVegetable) {
				//secondary vegetable
				StringTag secondary;
				do {
					ResourceLocation randomVegetable = getRegistryName(getRandomElement(random, vegetables));
					secondary = StringTag.valueOf(randomVegetable.toString());
				} while(!eggTag.equals(secondary) && list.contains(secondary));
				// Probably dumplings with three egg? It's acceptable lol
				list.add(secondary);
			}
			itemStack.getOrCreateTag().put(FarciFoodStorage.TAG_FILLINGS, list);
		}
		return itemStack;
	}
}
