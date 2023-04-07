package com.hexagram2021.emeraldcraft.common.entities;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ECBoat extends Boat implements IECBoat {
	public static final EntityDataAccessor<Integer> DATA_ID_ECTYPE = SynchedEntityData.defineId(ECBoat.class, EntityDataSerializers.INT);

	public ECBoat(EntityType<? extends ECBoat> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public ECBoat(Level level, double x, double y, double z) {
		this(ECEntities.BOAT, level);
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

	@Override @NotNull
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putString("Type", this.getECBoatType().getName());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.contains("Type", Tag.TAG_STRING)) {
			this.entityData.set(DATA_ID_ECTYPE, ECBoat.ECBoatType.byName(nbt.getString("Type")).ordinal());
		}
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
		this.lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (onGround) {
				if (this.fallDistance > 3.0F) {
					if (this.status != Status.ON_LAND) {
						this.resetFallDistance();
						return;
					}

					this.causeFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
					if (!this.level.isClientSide && !this.isRemoved()) {
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

				this.resetFallDistance();
			} else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}
		}

	}

	@Override @NotNull
	public Item getDropItem() {
		return switch (ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE))) {
			case GINKGO -> ECItems.GINKGO_BOAT.get();
			case PALM -> ECItems.PALM_BOAT.get();
			case PEACH -> ECItems.PEACH_BOAT.get();
		};
	}

	@Override
	public void setECBoatType(ECBoatType type) {
		this.entityData.set(DATA_ID_ECTYPE, type.ordinal());
	}

	@Override
	public ECBoatType getECBoatType() {
		return ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE));
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setVariant(@NotNull Boat.Type vanillaType) {
	}

	/** @deprecated */
	@Deprecated
	@Override @NotNull
	public Boat.Type getVariant() {
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
