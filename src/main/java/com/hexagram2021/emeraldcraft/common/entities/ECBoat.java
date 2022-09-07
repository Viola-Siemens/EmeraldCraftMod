package com.hexagram2021.emeraldcraft.common.entities;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ECBoat extends BoatEntity {
	public static final DataParameter<Integer> DATA_ID_ECTYPE = EntityDataManager.defineId(ECBoat.class, DataSerializers.INT);

	public ECBoat(EntityType<? extends ECBoat> type, World level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public ECBoat(World level, double x, double y, double z) {
		this(ECEntities.BOAT.get(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_ID_ECTYPE, ECBoatType.GINKGO.ordinal());
	}

	@Override @Nonnull
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putString("Type", this.getECBoatType().getName());
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		if (nbt.contains("model", 8)) {
			this.entityData.set(DATA_ID_ECTYPE, ECBoat.ECBoatType.byName(nbt.getString("model")).ordinal());
		}

	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, @Nonnull BlockState state, @Nonnull BlockPos pos) {
		this.lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (onGround) {
				if (this.fallDistance > 3.0F) {
					if (this.status != Status.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}

					this.causeFallDamage(this.fallDistance, 1.0F);
					if (!this.level.isClientSide && !this.removed) {
						this.kill();
						if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							int j;
							for(j = 0; j < 3; ++j) {
								this.spawnAtLocation(this.getECBoatType().getPlanks());
							}

							for(j = 0; j < 2; ++j) {
								this.spawnAtLocation(Items.STICK);
							}
						}
					}
				}

				this.fallDistance = 0.0F;
			} else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}
		}

	}

	@Override @Nonnull
	public Item getDropItem() {
		switch (ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE))) {
			case GINKGO:
				return ECItems.GINKGO_BOAT.get();
			case PALM:
				return ECItems.PALM_BOAT.get();
			case PEACH:
				return ECItems.PEACH_BOAT.get();
			default:
				return Items.OAK_BOAT;
		}
	}

	public void setECBoatType(ECBoatType type) {
		this.entityData.set(DATA_ID_ECTYPE, type.ordinal());
	}

	public ECBoatType getECBoatType() {
		return ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE));
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setType(@Nonnull BoatEntity.Type vanillaType) {
	}

	/** @deprecated */
	@Deprecated
	@Override
	@Nonnull
	public BoatEntity.Type getBoatType() {
		return Type.OAK;
	}

	public enum ECBoatType {
		GINKGO("ginkgo", ECBlocks.Plant.GINKGO_PLANKS.get()),
		PALM("palm", ECBlocks.Plant.PALM_PLANKS.get()),
		PEACH("peach", ECBlocks.Plant.PEACH_PLANKS.get());

		private final String name;
		private final Block planks;

		ECBoatType(String name, Block planks) {
			this.name = name;
			this.planks = planks;
		}

		public String getName() {
			return this.name;
		}

		public Block getPlanks() {
			return this.planks;
		}

		public String toString() {
			return this.name;
		}

		public static ECBoatType byId(int id) {
			ECBoatType[] type = values();
			return type[id >= 0 && id < type.length ? id : 0];
		}

		public static ECBoatType byName(String aName) {
			ECBoatType[] types = values();
			return Arrays.stream(types).filter((t) -> t.getName().equals(aName)).findFirst().orElse(types[0]);
		}
	}
}
