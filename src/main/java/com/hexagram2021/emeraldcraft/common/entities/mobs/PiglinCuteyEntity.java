package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.ECTrades;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class PiglinCuteyEntity extends AbstractVillagerEntity implements PiglinCuteyDataHolder {
	public static final IDataSerializer<PiglinCuteyData> PIGLIN_CUTEY_DATA = new IDataSerializer<PiglinCuteyData>() {
		@Override
		public void write(PacketBuffer buf, PiglinCuteyData data) {
			buf.writeVarInt(data.getLevel());
		}

		@Override @Nonnull
		public PiglinCuteyData read(PacketBuffer buf) {
			return new PiglinCuteyData(buf.readVarInt());
		}

		@Override @Nonnull
		public PiglinCuteyData copy(@Nonnull PiglinCuteyData data) {
			return data;
		}
	};

	private static final DataParameter<PiglinCuteyData> DATA_PIGLIN_CUTEY_DATA = EntityDataManager.defineId(
			PiglinCuteyEntity.class, PIGLIN_CUTEY_DATA
	);
	private static final DataParameter<Boolean> DATA_IMMUNE_TO_DESPAWN = EntityDataManager.defineId(PiglinCuteyEntity.class, DataSerializers.BOOLEAN);
	public static final int MULTIPLIER_FOOD_THRESHOLD = 200;
	public static final Map<Item, Integer> FOOD_POINTS = ImmutableMap.of(Items.COOKED_PORKCHOP, 4, Items.PORKCHOP, 1);
	private static final int TRADES_PER_LEVEL = 3;
	private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.COOKED_PORKCHOP, Items.PORKCHOP);
	@VisibleForTesting
	public static final double SPEED_MODIFIER = 0.8D;
	private int updateMerchantTimer;
	private boolean increaseProfessionLevelOnUpdate;
	@Nullable
	private PlayerEntity lastTradedPlayer;
	private int foodLevel;
	private int cuteyXp;

	@Nullable
	protected BlockPos portalTarget = null;

	public PiglinCuteyEntity(EntityType<? extends PiglinCuteyEntity> entityType, World level) {
		super(entityType, level);
		((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PiglinCuteyEntity.RushToPortalGoal(this, 1.0D, 50.0D, SPEED_MODIFIER));
		this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
		this.goalSelector.addGoal(1, new LookAtCustomerGoal(this));
		this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZoglinEntity.class, 12.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(1, new PanicGoal(this, SPEED_MODIFIER));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, ZombifiedPiglinEntity.class, 8.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, HoglinEntity.class, 8.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(3, new SwimGoal(this));
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, SPEED_MODIFIER * 0.6D));
		this.goalSelector.addGoal(5, new RandomWalkingGoal(this, SPEED_MODIFIER * 0.5D));
		this.goalSelector.addGoal(6, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));
	}

	@Override
	protected void customServerAiStep() {
		if (!this.isTrading() && this.updateMerchantTimer > 0) {
			--this.updateMerchantTimer;
			if (this.updateMerchantTimer <= 0) {
				if (this.increaseProfessionLevelOnUpdate) {
					this.increaseMerchantCareer();
					this.increaseProfessionLevelOnUpdate = false;
				}

				this.addEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
			}
		}

		if (!this.isNoAi() && this.random.nextInt(100) == 0) {
			Raid raid = ((ServerWorld)this.level).getRaidAt(this.blockPosition());
			if (raid != null && raid.isActive() && !raid.isOver()) {
				this.level.broadcastEntityEvent(this, (byte)42);
			}
		}

		super.customServerAiStep();
	}

	@Nullable
	@Override
	public AgeableEntity getBreedOffspring(@Nonnull ServerWorld level, @Nonnull AgeableEntity mob) { return null; }

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_PIGLIN_CUTEY_DATA, new PiglinCuteyData(1));
		this.entityData.define(DATA_IMMUNE_TO_DESPAWN, false);
	}

	@Override @Nonnull
	public ActionResultType mobInteract(@Nonnull PlayerEntity player, @Nonnull Hand hand) {
		if (this.isAlive() && !this.isTrading() && !this.isBaby()) {
			if (this.getOffers().isEmpty()) {
				return ActionResultType.sidedSuccess(this.level.isClientSide);
			}
			if (!this.level.isClientSide) {
				this.startTrading(player);
			}
			return ActionResultType.sidedSuccess(this.level.isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	private boolean shouldIncreaseLevel() {
		int i = this.getPiglinCuteyData().getLevel();
		return PiglinCuteyData.canLevelUp(i) && this.cuteyXp >= PiglinCuteyData.getMaxXpPerLevel(i);
	}

	private void increaseMerchantCareer() {
		this.setPiglinCuteyData(this.getPiglinCuteyData().setLevel(this.getPiglinCuteyData().getLevel() + 1));
		this.updateTrades();
	}

	@Override
	public int getVillagerXp() {
		return this.cuteyXp;
	}

	@Override
	protected void rewardTradeXp(MerchantOffer merchantOffer) {
		int i = 3 + this.random.nextInt(4);
		this.cuteyXp += merchantOffer.getXp();
		this.lastTradedPlayer = this.getTradingPlayer();
		if (this.shouldIncreaseLevel()) {
			this.updateMerchantTimer = 40;
			this.increaseProfessionLevelOnUpdate = true;
			i += 5;
		}

		if (merchantOffer.shouldRewardExp()) {
			this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
		}
	}

	@Override
	protected void updateTrades() {
		PiglinCuteyData cuteyData = this.getPiglinCuteyData();
		Int2ObjectMap<VillagerTrades.ITrade[]> allTrades = ECTrades.PIGLIN_CUTEY_TRADES;
		if (!allTrades.isEmpty()) {
			VillagerTrades.ITrade[] trades = allTrades.get(cuteyData.getLevel());
			if (trades != null) {
				MerchantOffers merchantoffers = this.getOffers();
				this.addOffersFromItemListings(merchantoffers, trades, TRADES_PER_LEVEL);
			}
		}
	}

	@Override
	public PiglinCuteyData getPiglinCuteyData() {
		return this.entityData.get(DATA_PIGLIN_CUTEY_DATA);
	}

	@Override
	public void setPiglinCuteyData(PiglinCuteyData data) {
		this.entityData.set(DATA_PIGLIN_CUTEY_DATA, data);
	}

	@Override
	public boolean wantsToPickUp(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return WANTED_ITEMS.contains(item) && this.getInventory().canAddItem(itemStack);
	}

	private boolean hungry() { return this.foodLevel < MULTIPLIER_FOOD_THRESHOLD; }

	private int countFoodPointsInInventory() {
		Inventory simplecontainer = this.getInventory();
		return FOOD_POINTS.entrySet().stream().mapToInt((food) -> simplecontainer.countItem(food.getKey()) * food.getValue()).sum();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level.isClientSide) {
			this.maybeDespawn();
		}
	}

	private boolean isImmuneToDespawn() {
		return this.getEntityData().get(DATA_IMMUNE_TO_DESPAWN);
	}

	private void maybeDespawn() {
		if (!this.level.dimensionType().piglinSafe() && !isImmuneToDespawn() && !this.isNoAi()) {
			this.remove();
		}
	}

	private void startTrading(PlayerEntity player) {
		this.eatUntilFull();
		this.updateSpecialPrices(player);
		this.setTradingPlayer(player);
		this.openTradingScreen(player, this.getDisplayName(), this.getPiglinCuteyData().getLevel());
	}

	@Override
	public void setTradingPlayer(@Nullable PlayerEntity player) {
		boolean flag = this.getTradingPlayer() != null && player == null;
		super.setTradingPlayer(player);
		if (flag) {
			this.stopTrading();
		}
	}

	@Override
	protected void stopTrading() {
		super.stopTrading();
		this.resetSpecialPrices();
	}

	private void resetSpecialPrices() {
		for(MerchantOffer merchantoffer : this.getOffers()) {
			merchantoffer.resetSpecialPriceDiff();
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTrading() ? ECSounds.PIGLIN_CUTEY_TRADE : ECSounds.PIGLIN_CUTEY_AMBIENT;
	}

	@Override
	public void playCelebrateSound() {
		this.playSound(ECSounds.PIGLIN_CUTEY_CELEBRATE, this.getSoundVolume(), this.getVoicePitch());
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
		return ECSounds.PIGLIN_CUTEY_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.PIGLIN_CUTEY_DEATH;
	}

	@Override @Nonnull
	protected SoundEvent getTradeUpdatedSound(boolean correct) {
		return correct ? ECSounds.PIGLIN_CUTEY_YES : ECSounds.PIGLIN_CUTEY_NO;
	}

	@Override @Nonnull
	public SoundEvent getNotifyTradeSound() {
		return ECSounds.PIGLIN_CUTEY_YES;
	}

	private void eatUntilFull() {
		if (this.hungry() && this.countFoodPointsInInventory() != 0) {
			for(int i = 0; i < this.getInventory().getContainerSize(); ++i) {
				ItemStack itemstack = this.getInventory().getItem(i);
				if (!itemstack.isEmpty()) {
					Integer integer = FOOD_POINTS.get(itemstack.getItem());
					if (integer != null) {
						int j = itemstack.getCount();

						for(int k = j; k > 0; --k) {
							this.foodLevel = this.foodLevel + integer;
							this.getInventory().removeItem(i, 1);
							if (!this.hungry()) {
								return;
							}
						}
					}
				}
			}
		}
	}

	private void updateSpecialPrices(PlayerEntity pPlayer) {
		int i = this.getPlayerReputation(pPlayer);
		if (i != 0) {
			for(MerchantOffer merchantoffer : this.getOffers()) {
				merchantoffer.addToSpecialPriceDiff(-MathHelper.floor((float)i * merchantoffer.getPriceMultiplier()));
			}
		}

		if (pPlayer.hasEffect(Effects.HERO_OF_THE_VILLAGE)) {
			EffectInstance mobeffectinstance = pPlayer.getEffect(Effects.HERO_OF_THE_VILLAGE);
			int k = mobeffectinstance.getAmplifier();

			for(MerchantOffer merchantoffer : this.getOffers()) {
				double d0 = 0.1D + 0.05D * (double)k;
				int j = (int)Math.floor(d0 * (double)merchantoffer.getBaseCostA().getCount());
				merchantoffer.addToSpecialPriceDiff(-Math.max(j, 1));
			}
		}
	}

	public int getPlayerReputation(PlayerEntity player) {
		return (int)Math.floor(foodLevel / (MULTIPLIER_FOOD_THRESHOLD * 0.025D));
	}

	private static final int SearchRange = 30;
	private static final int VerticalSearchRange = 10;
	protected void findNearestPortal() {
		BlockPos blockpos = this.blockPosition();
		BlockPos.Mutable mutableblockpos = new BlockPos.Mutable();

		for(int k = 0; k <= VerticalSearchRange; k = k > 0 ? -k : 1 - k) {
			for(int l = 0; l < SearchRange; ++l) {
				for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
					for(int j1 = 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
						mutableblockpos.setWithOffset(blockpos, i1, k, j1);
						if (this.isWithinRestriction(mutableblockpos) && level.getBlockState(mutableblockpos).is(Blocks.NETHER_PORTAL)) {
							this.portalTarget = mutableblockpos;
							return;
						}
					}
				}
			}
		}

		this.portalTarget = null;
	}

	@Nullable
	private BlockPos getPortalTarget() {
		return portalTarget;
	}

	@Override
	public void addAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
		super.addAdditionalSaveData(pCompound);
		PiglinCuteyData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.getPiglinCuteyData()).resultOrPartial(ECLogger::error).ifPresent(
				(inbt) -> pCompound.put("PiglinCuteyData", inbt)
		);
		pCompound.putInt("FoodLevel", this.foodLevel);
		pCompound.putInt("Xp", this.cuteyXp);
	}

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("PiglinCuteyData", 10)) {
			DataResult<PiglinCuteyData> dataResult = PiglinCuteyData.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, pCompound.get("PiglinCuteyData")));
			dataResult.resultOrPartial(ECLogger::error).ifPresent(this::setPiglinCuteyData);
		}

		if (pCompound.contains("FoodLevel", 1)) {
			this.foodLevel = pCompound.getInt("FoodLevel");
		}

		if (pCompound.contains("Xp", 3)) {
			this.cuteyXp = pCompound.getInt("Xp");
		}

		this.setCanPickUpLoot(true);
	}

	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) { return false; }

	class RushToPortalGoal extends Goal {
		final PiglinCuteyEntity cutey;
		final double stopDistance;
		final double giveupDistance;
		final double speedModifier;
		private static final int INTERVAL_TICKS = 20;
		protected int nextStartTick;

		RushToPortalGoal(PiglinCuteyEntity cutey, double stopDistance, double giveupDistance, double speedModifier) {
			this.cutey = cutey;
			this.stopDistance = stopDistance;
			this.giveupDistance = giveupDistance;
			this.speedModifier = speedModifier;
		}

		@Override
		public void start() {
			this.cutey.playCelebrateSound();
		}

		@Override
		public void stop() {
			this.cutey.navigation.stop();

			if(this.cutey.lastTradedPlayer != null && !isTooFarAway(this.cutey.lastTradedPlayer, this.giveupDistance)) {
				this.cutey.level.addFreshEntity(new ItemEntity(
						this.cutey.level,
						this.cutey.lastTradedPlayer.getX(),
						this.cutey.lastTradedPlayer.getY() + 0.5D,
						this.cutey.lastTradedPlayer.getZ(),
						new ItemStack(Items.GOLD_BLOCK, 16)
				));
			}

			this.cutey.remove();
		}

		@Override
		public boolean canUse() {
			if (this.nextStartTick > 0) {
				--this.nextStartTick;
				return false;
			}

			this.nextStartTick = this.nextStartTick(this.cutey);
			this.cutey.findNearestPortal();
			BlockPos blockpos = this.cutey.getPortalTarget();
			return blockpos != null && this.isTooFarAway(blockpos, this.stopDistance);
		}

		protected int nextStartTick(LivingEntity mob) { return INTERVAL_TICKS + mob.getRandom().nextInt(INTERVAL_TICKS); }

		@Override
		public boolean canContinueToUse() {
			return true;
		}

		@Override
		public void tick() {
			BlockPos blockpos = this.cutey.getPortalTarget();
			if (blockpos != null && PiglinCuteyEntity.this.navigation.isDone()) {
				if (this.isTooFarAway(blockpos, 10.0D)) {
					Vector3d vec3 = (new Vector3d(
							(double)blockpos.getX() - this.cutey.getX(),
							(double)blockpos.getY() - this.cutey.getY(),
							(double)blockpos.getZ() - this.cutey.getZ()
					)).normalize();
					Vector3d vec31 = vec3.scale(10.0D).add(this.cutey.getX(), this.cutey.getY(), this.cutey.getZ());
					PiglinCuteyEntity.this.navigation.moveTo(vec31.x, vec31.y, vec31.z, this.speedModifier);
				} else if(this.isTooFarAway(blockpos, this.stopDistance)) {
					PiglinCuteyEntity.this.navigation.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.speedModifier);
				} else {
					this.stop();
				}
			}
		}

		private boolean isTooFarAway(BlockPos blockpos, double dist) {
			Vector3d targetPos = new Vector3d(blockpos.getX(), this.cutey.position().y, blockpos.getZ());
			return !targetPos.closerThan(this.cutey.position(), dist);
		}

		private boolean isTooFarAway(Entity entity, double dist) {
			return !entity.closerThan(this.cutey, dist);
		}
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 25.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	static {
		DataSerializers.registerSerializer(PIGLIN_CUTEY_DATA);
	}
}
