package com.hexagram2021.emeraldcraft.common.entities;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ECChestBoat extends ChestBoat implements IECBoat {
	public static final EntityDataAccessor<Integer> DATA_ID_ECTYPE = SynchedEntityData.defineId(ECChestBoat.class, EntityDataSerializers.INT);

	public ECChestBoat(EntityType<? extends ECChestBoat> type, Level level) {
		super(type, level);
	}

	public ECChestBoat(Level level, double x, double y, double z) {
		this(ECEntities.CHEST_BOAT, level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_ID_ECTYPE, ECBoat.ECBoatType.GINKGO.ordinal());
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putString("Type", this.getECBoatType().getName());
		this.addChestVehicleSaveData(nbt);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.contains("Type", Tag.TAG_STRING)) {
			this.entityData.set(DATA_ID_ECTYPE, ECBoat.ECBoatType.byName(nbt.getString("Type")).ordinal());
		}
		this.readChestVehicleSaveData(nbt);
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
		this.lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (onGround) {
				if (this.fallDistance > 3.0F) {
					if (this.status != Status.ON_LAND) {
						this.resetFallDistance();
						return;
					}

					this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
					if (!this.level().isClientSide && !this.isRemoved()) {
						this.kill();
						if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
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
			} else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}
		}
	}

	@Override
	public Item getDropItem() {
		return switch (ECBoat.ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE))) {
			case GINKGO -> ECItems.GINKGO_CHEST_BOAT.get();
			case PALM -> ECItems.PALM_CHEST_BOAT.get();
			case PEACH -> ECItems.PEACH_CHEST_BOAT.get();
		};
	}

	@Override
	public void setECBoatType(ECBoat.ECBoatType type) {
		this.entityData.set(DATA_ID_ECTYPE, type.ordinal());
	}

	@Override
	public ECBoat.ECBoatType getECBoatType() {
		return ECBoat.ECBoatType.byId(this.entityData.get(DATA_ID_ECTYPE));
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setVariant(Boat.Type vanillaType) {
	}

	/** @deprecated */
	@Deprecated
	@Override
	public Boat.Type getVariant() {
		return Type.OAK;
	}
}
