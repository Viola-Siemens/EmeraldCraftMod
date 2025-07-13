package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.fluids.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public final class ECFluids {
    //resin
    public static final ECFluid RESIN = new Resin.Source();
    public static final ECFluid FLOWING_RESIN = new Resin.Flowing();
    //public static final LiquidBlock RESIN_BLOCK = makeLiquidBlock("resin", RESIN);
    public static final ECItems.ItemEntry<BucketItem> RESIN_BUCKET = ECItems.ItemEntry.register("resin_bucket", makeBucket(RESIN), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_emerald
    public static final ECFluid MELTED_EMERALD = new MeltedEmerald.Source();
    public static final ECFluid FLOWING_MELTED_EMERALD = new MeltedEmerald.Flowing();
    //public static final LiquidBlock MELTED_EMERALD_BLOCK = makeLiquidBlock("melted_emerald", MELTED_EMERALD);
    public static final ECItems.ItemEntry<BucketItem> MELTED_EMERALD_BUCKET = ECItems.ItemEntry.register("melted_emerald_bucket", makeBucket(MELTED_EMERALD), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_iron
    public static final ECFluid MELTED_IRON = new MeltedIron.Source();
    public static final ECFluid FLOWING_MELTED_IRON = new MeltedIron.Flowing();
    //public static final LiquidBlock MELTED_IRON_BLOCK = makeLiquidBlock("melted_iron", MELTED_IRON);
    public static final ECItems.ItemEntry<BucketItem> MELTED_IRON_BUCKET = ECItems.ItemEntry.register("melted_iron_bucket", makeBucket(MELTED_IRON), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_gold
    public static final ECFluid MELTED_GOLD = new MeltedGold.Source();
    public static final ECFluid FLOWING_MELTED_GOLD = new MeltedGold.Flowing();
    // public static final LiquidBlock MELTED_GOLD_BLOCK = makeLiquidBlock("melted_gold", MELTED_GOLD);
    public static final ECItems.ItemEntry<BucketItem> MELTED_GOLD_BUCKET = ECItems.ItemEntry.register("melted_gold_bucket", makeBucket(MELTED_GOLD), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_copper
    public static final ECFluid MELTED_COPPER = new MeltedCopper.Source();
    public static final ECFluid FLOWING_MELTED_COPPER = new MeltedCopper.Flowing();
    //public static final LiquidBlock MMELTED_COPPER_BLOCK = makeLiquidBlock("melted_copper", MELTED_COPPER);
    public static final ECItems.ItemEntry<BucketItem> MELTED_COPPER_BUCKET = ECItems.ItemEntry.register("melted_copper_bucket", makeBucket(MELTED_COPPER), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_zinc
    public static final ECFluid MELTED_ZINC = new MeltedZinc.Source();
    public static final ECFluid FLOWING_MELTED_ZINC = new MeltedZinc.Flowing();
    // public static final LiquidBlock MELTED_ZINC_BLOCK = makeLiquidBlock("melted_zinc", MELTED_ZINC);
    public static final ECItems.ItemEntry<BucketItem> MELTED_ZINC_BUCKET = ECItems.ItemEntry.register("melted_zinc_bucket", makeBucket(MELTED_ZINC), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_aluminum
    public static final ECFluid MELTED_ALUMINUM = new MeltedAluminum.Source();
    public static final ECFluid FLOWING_MELTED_ALUMINUM = new MeltedAluminum.Flowing();
    // public static final LiquidBlock MELTED_ALUMINUM_BLOCK = makeLiquidBlock("melted_aluminum", MELTED_ALUMINUM);
    public static final ECItems.ItemEntry<BucketItem> MELTED_ALUMINUM_BUCKET = ECItems.ItemEntry.register("melted_aluminum_bucket", makeBucket(MELTED_ALUMINUM), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_lead
    public static final ECFluid MELTED_LEAD = new MeltedLead.Source();
    public static final ECFluid FLOWING_MELTED_LEAD = new MeltedLead.Flowing();
    //public static final LiquidBlock MELTED_LEAD_BLOCK = makeLiquidBlock("melted_lead", MELTED_LEAD);
    public static final ECItems.ItemEntry<BucketItem> MELTED_LEAD_BUCKET = ECItems.ItemEntry.register("melted_lead_bucket", makeBucket(MELTED_LEAD), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_silver
    public static final ECFluid MELTED_SILVER = new MeltedSilver.Source();
    public static final ECFluid FLOWING_MELTED_SILVER = new MeltedSilver.Flowing();
    // public static final LiquidBlock MELTED_SILVER_BLOCK = makeLiquidBlock("melted_silver", MELTED_SILVER);
    public static final ECItems.ItemEntry<BucketItem> MELTED_SILVER_BUCKET = ECItems.ItemEntry.register("melted_silver_bucket", makeBucket(MELTED_SILVER), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_nickel
    public static final ECFluid MELTED_NICKEL = new MeltedNickel.Source();
    public static final ECFluid FLOWING_MELTED_NICKEL = new MeltedNickel.Flowing();
    //public static final LiquidBlock MELTED_NICKEL_BLOCK = makeLiquidBlock("melted_nickel", MELTED_NICKEL);
    public static final ECItems.ItemEntry<BucketItem> MELTED_NICKEL_BUCKET = ECItems.ItemEntry.register("melted_nickel_bucket", makeBucket(MELTED_NICKEL), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    //melted_uranium
    public static final ECFluid MELTED_URANIUM = new MeltedUranium.Source();
    public static final ECFluid FLOWING_MELTED_URANIUM = new MeltedUranium.Flowing();
    //public static final LiquidBlock MELTED_URANIUM_BLOCK = makeLiquidBlock("melted_uranium", MELTED_URANIUM);
    public static final ECItems.ItemEntry<BucketItem> MELTED_URANIUM_BUCKET = ECItems.ItemEntry.register("melted_uranium_bucket", makeBucket(MELTED_URANIUM), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);

    private static LiquidBlock makeLiquidBlock(String name, FlowingFluid fluid) {
        return Registry.register(BuiltInRegistries.BLOCK, EmeraldCraft.id(name), new LiquidBlock(
                fluid, FabricBlockSettings.copyOf(Blocks.WATER)));
    }

    private static <T extends Fluid> BucketItem makeBucket(T still) {
        return new BucketItem(still, new FabricItemSettings().maxCount(16).recipeRemainder(Items.BUCKET));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <T extends Fluid> void registerFluid(String name, T still, T flowing) {
        Registry.register(BuiltInRegistries.FLUID, EmeraldCraft.id(name), still);
        Registry.register(BuiltInRegistries.FLUID, EmeraldCraft.id("flowing_" + name), flowing);
        ECFluidAttributes fluidAttributes = new ECFluidAttributes();
        FluidVariantAttributes.register(still, fluidAttributes);
        FluidVariantAttributes.register(flowing, fluidAttributes);
    }

    public static void init() {
        registerFluid("resin", RESIN, FLOWING_RESIN);
        registerFluid("melted_emerald", MELTED_EMERALD, FLOWING_MELTED_EMERALD);
        registerFluid("melted_iron", MELTED_IRON, FLOWING_MELTED_IRON);
        registerFluid("melted_gold", MELTED_GOLD, FLOWING_MELTED_GOLD);
        registerFluid("melted_copper", MELTED_COPPER, FLOWING_MELTED_COPPER);
        registerFluid("melted_zinc", MELTED_ZINC, FLOWING_MELTED_ZINC);
        registerFluid("melted_aluminum", MELTED_ALUMINUM, FLOWING_MELTED_ALUMINUM);
        registerFluid("melted_lead", MELTED_LEAD, FLOWING_MELTED_LEAD);
        registerFluid("melted_silver", MELTED_SILVER, FLOWING_MELTED_SILVER);
        registerFluid("melted_nickel", MELTED_NICKEL, FLOWING_MELTED_NICKEL);
        registerFluid("melted_uranium", MELTED_URANIUM, FLOWING_MELTED_URANIUM);
    }
}
