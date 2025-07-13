package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.ArrayList;
import java.util.List;

public class ECBannerPatterns {
    public static final List<BannerEntry> ALL_BANNERS = new ArrayList<>();

    public static final BannerEntry BEE = addBanner("bee", "bee");
    public static final BannerEntry SNOW = addBanner("snow", "snw");
    public static final BannerEntry BOTTLE = addBanner("bottle", "btl");
    public static final BannerEntry POTION = addBanner("potion", "ptn");

    public static void init() {
    }

    private static BannerEntry addBanner(String name, String hashName) {
        ResourceLocation id = EmeraldCraft.id(name);
        BannerPattern pattern = Registry.register(BuiltInRegistries.BANNER_PATTERN, id, new BannerPattern("ec_" + hashName));
        TagKey<BannerPattern> tag = TagKey.create(Registries.BANNER_PATTERN, id);
        ECItems.ItemEntry<BannerPatternItem> item = ECItems.ItemEntry.register(name + "_banner_pattern", new BannerPatternItem(
                tag, new FabricItemSettings().stacksTo(1)
        ), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);
        BannerEntry result = new BannerEntry(pattern, tag, item);
        ALL_BANNERS.add(result);
        return result;
    }

    public record BannerEntry(
            BannerPattern pattern,
            TagKey<BannerPattern> tag,
            ECItems.ItemEntry<BannerPatternItem> item
    ) implements ItemLike {
        @Override
        public Item asItem() {
            return this.item.asItem();
        }
    }
}
