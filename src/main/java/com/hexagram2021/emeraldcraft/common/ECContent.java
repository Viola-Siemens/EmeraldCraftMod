package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.blocks.entity.*;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.crafting.display.CookstoveDisplayTypes;
import com.hexagram2021.emeraldcraft.common.entities.mobs.WombatEntity;
import com.hexagram2021.emeraldcraft.common.register.*;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.biome.BiomeModifiers;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

public class ECContent {
    public static void modConstruction() {
        ModsLoadedEventSubscriber.compatModLoaded();

        CookstoveDisplayTypes.init();

        ECBlockSetTypes.init();
        ECWoodType.init();
        ECFluids.init();
        ECBlocks.init();
        ECItems.init();
        ECEntities.init();
        ECMemoryModuleTypes.init();
        ECBannerPatterns.init();
        Villages.Registers.init();
        Villages.Events.registerTrades();
        Villages.Events.registerWandererTrades();
        ECLootModifiers.init();
        ECRecipes.init();
        ECRecipeSerializer.init();
        ECContainerTypes.init();
        ECBlockEntity.init();
        ECCreativeModeTabs.init();
        ECPlacementModifierType.init();
        ECEnchantments.init();
        ECConfiguredFeatureKeys.init();
        ECPlacedFeatureKeys.init();
        BiomeModifiers.init();
        ECVillagePlacedFeatureKeys.init();
        ECStructureTypes.init();
        ECStructureKeys.init();
        ECStructureSetKeys.init();
        ModsLoadedEventSubscriber.solveCompat();
        onRegister();
        registerEntitySpawnPlacement();
        registerStorages();
    }

    public static void init() {
        ModsLoadedEventSubscriber.solveTerraBlender();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void registerStorages() {
        //Fluids Bucket
        FluidStorage.combinedItemApiProvider(ECFluids.RESIN_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.RESIN), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_ALUMINUM_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_ALUMINUM), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_COPPER_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_COPPER), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_EMERALD_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_EMERALD), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_GOLD_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_GOLD), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_IRON_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_IRON), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_LEAD_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_LEAD), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_NICKEL_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_NICKEL), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_SILVER_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_SILVER), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_URANIUM_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_URANIUM), FluidConstants.BUCKET)
                );
        FluidStorage.combinedItemApiProvider(ECFluids.MELTED_ZINC_BUCKET.get())
                .register(context ->
                        new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(ECFluids.MELTED_ZINC), FluidConstants.BUCKET)
                );

        //Continuous Miner
        FluidStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<FluidVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof ContinuousMinerBlockEntity tank) {
                    return tank.getFluidStorage(ContinuousMinerBlockEntity.TANK_INPUT);
                }
                return null;
            }
        }, ECBlockEntity.CONTINUOUS_MINER);
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof ContinuousMinerBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.CONTINUOUS_MINER);

        //Cookstove
        FluidStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<FluidVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof CookstoveBlockEntity tank) {
                    return tank.getFluidStorage(CookstoveBlockEntity.TANK_INPUT);
                }
                return null;
            }
        }, ECBlockEntity.COOKSTOVE);
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof CookstoveBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.COOKSTOVE);

        //Meat Grinder
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof MeatGrinderBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.MEAT_GRINDER);

        //Melter
        FluidStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<FluidVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof MelterBlockEntity tank) {
                    return tank.getFluidStorage(0);
                }
                return null;
            }
        }, ECBlockEntity.MELTER);
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof MelterBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.MELTER);

        //Mineral Table
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof MineralTableBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.MINERAL_TABLE);

        //Rabble Furnace
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof RabbleFurnaceBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.RABBLE_FURNACE);

        //Ice Maker
        FluidStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<FluidVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof IceMakerBlockEntity tank) {
                    if (direction == Direction.UP || direction == Direction.DOWN) {
                        return tank.getFluidStorage(IceMakerBlockEntity.TANK_INPUT);
                    }
                    return tank.getFluidStorage(IceMakerBlockEntity.TANK_CONDENSATE);
                }
                return null;
            }
        }, ECBlockEntity.ICE_MAKER);
        ItemStorage.SIDED.registerForBlockEntities(new BlockApiLookup.BlockEntityApiProvider<>() {
            @Override
            public @Nullable Storage<ItemVariant> find(BlockEntity blockEntity, Direction direction) {
                if (!blockEntity.isRemoved() && blockEntity instanceof IceMakerBlockEntity tank) {
                    return tank.getItemStorage(direction);
                }
                return null;
            }
        }, ECBlockEntity.ICE_MAKER);
    }

    private static void onRegister() {
        ECSounds.init();
        ECFeatures.init();
        ECPotions.init();
        ECStructurePieceTypes.init();
        ECLootItemFunctions.init();
    }

    private static void registerEntitySpawnPlacement() {
        SpawnPlacements.register(ECEntities.HERRING, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules/*, SpawnPlacementRegisterEvent.Operation.OR*/);
        SpawnPlacements.register(ECEntities.PURPLE_SPOTTED_BIGEYE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules/*, SpawnPlacementRegisterEvent.Operation.OR*/);
        SpawnPlacements.register(ECEntities.SNAKEHEAD, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules/*, SpawnPlacementRegisterEvent.Operation.OR*/);
        SpawnPlacements.register(ECEntities.WOMBAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WombatEntity::checkWombatSpawnRules/*, SpawnPlacementRegisterEvent.Operation.OR*/);
        SpawnPlacements.register(ECEntities.WRAITH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules/*, SpawnPlacementRegisterEvent.Operation.OR*/);
    }
}
