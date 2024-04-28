package com.hexagram2021.emeraldcraft.common.util.loot_function;

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

	private static final int POSSIBILITY_VEGETARIAN_DUMPLINGS_EGG = 30;
	private static final int POSSIBILITY_MEATY_DUMPLINGS_VEGETABLE = 80;
	private static final int POSSIBILITY_MEATY_DUMPLINGS_EGG = 20;

	@Override
	protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
		RandomSource randomSource = lootContext.getRandom();
		List<Item> meats, vegetables;
		if(this.fillings.isPresent()) {
			List<Item> fillings = this.fillings.get();
			meats = fillings.stream().filter(item -> item.builtInRegistryHolder().is(ECItemTags.MINCE)).toList();
			vegetables = fillings.stream().filter(item -> item.builtInRegistryHolder().is(ECItemTags.VEGETABLE_FILLINGS)).toList();
		} else {
			meats = BuiltInRegistries.ITEM.getTag(ECItemTags.MINCE)
					.map(named -> named.stream().map(Holder::value).toList()).orElse(DEFAULT_ALL_MEAT);
			vegetables = BuiltInRegistries.ITEM.getTag(ECItemTags.VEGETABLE_FILLINGS)
					.map(named -> named.stream().map(Holder::value).toList()).orElse(DEFAULT_ALL_VEGETABLES);
		}
		boolean egg = vegetables.contains(Items.EGG);
		boolean meatsEmpty = meats.isEmpty();
		boolean vegetablesEmpty = vegetables.isEmpty();
		if(meatsEmpty && vegetablesEmpty) {
			ECLogger.warn("Couldn't find a compatible filling for %s.".formatted(itemStack));
		} else {
			ListTag list = new ListTag();
			if(meatsEmpty || (!vegetablesEmpty && randomSource.nextInt(meats.size() + 1) == 0)) {
				//vegetarian
				ResourceLocation randomVegetable = getRegistryName(getRandomElement(randomSource, vegetables));
				list.add(StringTag.valueOf(randomVegetable.toString()));
				if(egg && randomSource.nextInt(100) < POSSIBILITY_VEGETARIAN_DUMPLINGS_EGG) {
					list.add(StringTag.valueOf(getRegistryName(Items.EGG).toString()));
				}
			} else {
				//meaty
				ResourceLocation randomMeat = getRegistryName(getRandomElement(randomSource, meats));
				list.add(StringTag.valueOf(randomMeat.toString()));
				if(!vegetablesEmpty && randomSource.nextInt(100) < POSSIBILITY_MEATY_DUMPLINGS_VEGETABLE) {
					ResourceLocation randomVegetable = getRegistryName(getRandomElement(randomSource, vegetables));
					list.add(StringTag.valueOf(randomVegetable.toString()));
					if(egg && randomSource.nextInt(100) < POSSIBILITY_MEATY_DUMPLINGS_EGG) {
						list.add(StringTag.valueOf(getRegistryName(Items.EGG).toString()));
					}
				}
			}
			itemStack.getOrCreateTag().put(FarciFoodStorage.TAG_FILLINGS, list);
		}
		return itemStack;
	}

	@Override
	public LootItemFunctionType getType() {
		return ECLootItemFunctions.DUMPLINGS_RANDOM_FILLINGS;
	}

	private static <T> T getRandomElement(RandomSource random, List<? extends T> list) {
		int index = random.nextInt(list.size());
		return list.get(index);
	}
}
