package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.fluids.ECFluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECFluids {
	public static final DeferredRegister<Fluid> REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
	public static final DeferredRegister<FluidType> TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MODID);

	public static final FluidEntry<ECFluid> RESIN = FluidEntry.register(
			"resin",
			new ResourceLocation(MODID, "block/fluid/resin_still"), new ResourceLocation(MODID, "block/fluid/resin_flowing"),
			ECFluidTags.RESIN, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_EMERALD = FluidEntry.register(
			"melted_emerald",
			new ResourceLocation(MODID, "block/fluid/melted_emerald_still"), new ResourceLocation(MODID, "block/fluid/melted_emerald_flowing"),
			ECFluidTags.MELTED_EMERALD, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_IRON = FluidEntry.register(
			"melted_iron",
			new ResourceLocation(MODID, "block/fluid/melted_iron_still"), new ResourceLocation(MODID, "block/fluid/melted_iron_flowing"),
			ECFluidTags.MELTED_IRON, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_GOLD = FluidEntry.register(
			"melted_gold",
			new ResourceLocation(MODID, "block/fluid/melted_gold_still"), new ResourceLocation(MODID, "block/fluid/melted_gold_flowing"),
			ECFluidTags.MELTED_GOLD, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_COPPER = FluidEntry.register(
			"melted_copper",
			new ResourceLocation(MODID, "block/fluid/melted_copper_still"), new ResourceLocation(MODID, "block/fluid/melted_copper_flowing"),
			ECFluidTags.MELTED_COPPER, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_ZINC = FluidEntry.register(
			"melted_zinc",
			new ResourceLocation(MODID, "block/fluid/melted_zinc_still"), new ResourceLocation(MODID, "block/fluid/melted_zinc_flowing"),
			ECFluidTags.MELTED_ZINC, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_ALUMINUM = FluidEntry.register(
			"melted_aluminum",
			new ResourceLocation(MODID, "block/fluid/melted_aluminum_still"), new ResourceLocation(MODID, "block/fluid/melted_aluminum_flowing"),
			ECFluidTags.MELTED_ALUMINUM, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_LEAD = FluidEntry.register(
			"melted_lead",
			new ResourceLocation(MODID, "block/fluid/melted_lead_still"), new ResourceLocation(MODID, "block/fluid/melted_lead_flowing"),
			ECFluidTags.MELTED_LEAD, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_SILVER = FluidEntry.register(
			"melted_silver",
			new ResourceLocation(MODID, "block/fluid/melted_silver_still"), new ResourceLocation(MODID, "block/fluid/melted_silver_flowing"),
			ECFluidTags.MELTED_SILVER, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_NICKEL = FluidEntry.register(
			"melted_nickel",
			new ResourceLocation(MODID, "block/fluid/melted_nickel_still"), new ResourceLocation(MODID, "block/fluid/melted_nickel_flowing"),
			ECFluidTags.MELTED_NICKEL, ECFluid.Source::new, ECFluid.Flowing::new
	);
	public static final FluidEntry<ECFluid> MELTED_URANIUM = FluidEntry.register(
			"melted_uranium",
			new ResourceLocation(MODID, "block/fluid/melted_uranium_still"), new ResourceLocation(MODID, "block/fluid/melted_uranium_flowing"),
			ECFluidTags.MELTED_URANIUM, ECFluid.Source::new, ECFluid.Flowing::new
	);

	public record FluidEntry<T extends Fluid>(RegistryObject<T> still, RegistryObject<T> flowing, ECItems.ItemEntry<BucketItem> bucket,
											  RegistryObject<FluidType> type) {
		public T getFlowing() {
			return this.flowing.get();
		}

		public T getStill() {
			return this.still.get();
		}

		public BucketItem getBucket() {
			return this.bucket.get();
		}

		public static <T extends Fluid> FluidEntry<T> register(String name, ResourceLocation stillTex, ResourceLocation flowingTex,
															   TagKey<Fluid> fluidTag, BiFunction<FluidEntry<T>, TagKey<Fluid>, T> stillMaker, BiFunction<FluidEntry<T>, TagKey<Fluid>, T> flowingMaker) {
			FluidType.Properties builder = FluidType.Properties.create()
					.descriptionId("fluid.%s.%s".formatted(MODID, name))
					.fallDistanceModifier(0F)
					.canPushEntity(false).canSwim(false).canDrown(false)
					.canExtinguish(true).canConvertToSource(true).supportsBoating(true).canHydrate(true)
					.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
					.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
					.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH);
			RegistryObject<FluidType> type = TYPE_REGISTER.register(name, () -> buildFluidTypeWithTextures(builder, stillTex, flowingTex));
			Mutable<FluidEntry<T>> thisMutable = new MutableObject<>();
			RegistryObject<T> still = REGISTER.register(name, () -> makeFluid(
					stillMaker, thisMutable.getValue(), fluidTag
			));
			RegistryObject<T> flowing = REGISTER.register("flowing_" + name, () -> makeFluid(
					flowingMaker, thisMutable.getValue(), fluidTag
			));
			ECItems.ItemEntry<BucketItem> bucket = ECItems.ItemEntry.register(name+"_bucket", () -> makeBucket(still), ECItems.ItemEntry.ItemGroupType.FUNCTIONAL_BLOCKS_AND_MATERIALS);
			FluidEntry<T> entry = new FluidEntry<>(still, flowing, bucket, type);
			thisMutable.setValue(entry);
			return entry;
		}

		private static FluidType buildFluidTypeWithTextures(FluidType.Properties builder, ResourceLocation stillTex, ResourceLocation flowingTex) {
			return new FluidType(builder) {
				@Override
				public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
					consumer.accept(new IClientFluidTypeExtensions() {
						@Override
						public ResourceLocation getStillTexture() {
							return stillTex;
						}
						@Override
						public ResourceLocation getFlowingTexture() {
							return flowingTex;
						}
					});
				}
			};
		}

		private static <T extends Fluid> T makeFluid(BiFunction<FluidEntry<T>, TagKey<Fluid>, T> maker, FluidEntry<T> entry, TagKey<Fluid> fluidTag) {
			return maker.apply(entry, fluidTag);
		}

		private static <T extends Fluid> BucketItem makeBucket(RegistryObject<T> still) {
			return new BucketItem(still, new Item.Properties().stacksTo(16).craftRemainder(Items.BUCKET)) {
				@Override
				public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
					return new FluidBucketWrapper(stack);
				}
			};
		}
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
		TYPE_REGISTER.register(bus);
	}
}
