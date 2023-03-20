package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBannerPatterns {
	public static final DeferredRegister<BannerPattern> REGISTER = DeferredRegister.create(Registries.BANNER_PATTERN, MODID);

	public static final List<BannerEntry> ALL_BANNERS = new ArrayList<>();

	public static final BannerEntry BEE = addBanner("bee", "bee");
	public static final BannerEntry SNOW = addBanner("snow", "snw");
	public static final BannerEntry BOTTLE = addBanner("bottle", "btl");
	public static final BannerEntry POTION = addBanner("potion", "ptn");

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}

	private static BannerEntry addBanner(String name, String hashName) {
		RegistryObject<BannerPattern> pattern = REGISTER.register(name, () -> new BannerPattern("ec_"+hashName));
		TagKey<BannerPattern> tag = TagKey.create(Registries.BANNER_PATTERN, pattern.getId());
		ECItems.ItemEntry<BannerPatternItem> item = ECItems.ItemEntry.register(name + "_banner_pattern", () -> new BannerPatternItem(
				tag, new Item.Properties().stacksTo(1)
		), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);
		BannerEntry result = new BannerEntry(pattern, tag, item);
		ALL_BANNERS.add(result);
		return result;
	}

	public record BannerEntry(
			RegistryObject<BannerPattern> pattern,
			TagKey<BannerPattern> tag,
			ECItems.ItemEntry<BannerPatternItem> item
	) implements ItemLike {
		@Override @NotNull
		public Item asItem() {
			return this.item.asItem();
		}
	}
}
