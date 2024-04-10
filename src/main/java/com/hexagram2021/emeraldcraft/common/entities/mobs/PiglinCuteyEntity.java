package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.menu.PiglinCuteyMerchantMenu;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

public class PiglinCuteyEntity extends AbstractVillager implements PiglinCuteyDataHolder {
	public static final EntityDataSerializer<PiglinCuteyData> PIGLIN_CUTEY_DATA = new EntityDataSerializer.ForValueType<>() {
		@Override
		public void write(FriendlyByteBuf buf, PiglinCuteyData data) {
			buf.writeVarInt(data.level());
		}

		@Override
		public PiglinCuteyData read(FriendlyByteBuf buf) {
			return new PiglinCuteyData(buf.readVarInt());
		}
	};

	private static final EntityDataAccessor<PiglinCuteyData> DATA_PIGLIN_CUTEY_DATA = SynchedEntityData.defineId(
			PiglinCuteyEntity.class, PIGLIN_CUTEY_DATA
	);
	private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_DESPAWN = SynchedEntityData.defineId(PiglinCuteyEntity.class, EntityDataSerializers.BOOLEAN);
	public static final int MULTIPLIER_FOOD_THRESHOLD = 200;
	public static final Map<Item, Integer> FOOD_POINTS = ImmutableMap.of(Items.COOKED_PORKCHOP, 4, Items.PORKCHOP, 1);
	private static final int TRADES_PER_LEVEL = 3;
	private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.COOKED_PORKCHOP, Items.PORKCHOP);
	@VisibleForTesting
	public static final double SPEED_MODIFIER = 0.8D;
	private int updateMerchantTimer;
	private boolean increaseProfessionLevelOnUpdate;
	@Nullable
	private Player lastTradedPlayer;
	private int foodLevel;
	private int cuteyXp;

	public PiglinCuteyEntity(EntityType<? extends PiglinCuteyEntity> entityType, Level level) {
		super(entityType, level);
		((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	public int getPortalWaitTime() {
		return 40;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new RushToPortalGoal(this, 2.0D, 50.0D, SPEED_MODIFIER));
		this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
		this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
		this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 12.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(1, new PanicGoal(this, SPEED_MODIFIER));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, ZombifiedPiglin.class, 8.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Hoglin.class, 8.0F, SPEED_MODIFIER, SPEED_MODIFIER));
		this.goalSelector.addGoal(3, new FloatGoal(this));
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, SPEED_MODIFIER * 0.6D));
		this.goalSelector.addGoal(5, new RandomStrollGoal(this, SPEED_MODIFIER * 0.5D));
		this.goalSelector.addGoal(6, new InteractGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0F));
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

				this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
			}
		}

		if (!this.isNoAi() && this.random.nextInt(100) == 0) {
			Raid raid = ((ServerLevel)this.level()).getRaidAt(this.blockPosition());
			if (raid != null && raid.isActive() && !raid.isOver()) {
				this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_SWEAT);
			}
		}

		if (!this.hasRestriction()) {
			this.restrictTo(this.findNearestAnchor(), 12);
		}

		super.customServerAiStep();
	}

	@Override
	public void handleEntityEvent(byte event) {
		if (event == EntityEvent.VILLAGER_SWEAT) {
			this.addParticlesAroundSelf(ParticleTypes.SPLASH);
		} else {
			super.handleEntityEvent(event);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float v) {
		boolean flag = super.hurt(source, v);
		if (this.level().isClientSide) {
			return false;
		} else {
			if (flag && source.getEntity() instanceof Player) {
				PiglinAi.angerNearbyPiglins((Player)source.getEntity(), true);
			}

			return flag;
		}
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) { return null; }

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_PIGLIN_CUTEY_DATA, new PiglinCuteyData(1));
		this.entityData.define(DATA_IMMUNE_TO_DESPAWN, false);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(ECItems.PIGLIN_CUTEY_SPAWN_EGG.asItem()) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
			//TODO: player.awardStat
			if (this.getOffers().isEmpty()) {
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
			if (!this.level().isClientSide) {
				this.startTrading(player);
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	private boolean shouldIncreaseLevel() {
		int i = this.getPiglinCuteyData().level();
		return PiglinCuteyData.canLevelUp(i) && this.cuteyXp >= PiglinCuteyData.getMaxXpPerLevel(i);
	}

	private void increaseMerchantCareer() {
		this.setPiglinCuteyData(this.getPiglinCuteyData().setLevel(this.getPiglinCuteyData().level() + 1));
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
			this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5D, this.getZ(), i));
		}
	}

	@Override
	protected void updateTrades() {
		PiglinCuteyData cuteyData = this.getPiglinCuteyData();
		Int2ObjectMap<VillagerTrades.ItemListing[]> allTrades = ECTrades.PIGLIN_CUTEY_TRADES;
		if (!allTrades.isEmpty()) {
			VillagerTrades.ItemListing[] trades = allTrades.get(cuteyData.level());
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
		SimpleContainer simplecontainer = this.getInventory();
		return FOOD_POINTS.entrySet().stream().mapToInt((food) -> simplecontainer.countItem(food.getKey()) * food.getValue()).sum();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.maybeDespawn();
		}
	}

	private boolean isImmuneToDespawn() {
		return this.getEntityData().get(DATA_IMMUNE_TO_DESPAWN);
	}

	private void maybeDespawn() {
		if (!this.level().dimensionType().piglinSafe() && !isImmuneToDespawn() && !this.isNoAi()) {
			this.discard();
		}
	}

	private void startTrading(Player player) {
		this.eatUntilFull();
		this.updateSpecialPrices(player);
		this.setTradingPlayer(player);
		this.openTradingScreen(player, this.getDisplayName(), this.getPiglinCuteyData().level());
	}

	@Override
	public void openTradingScreen(Player player, Component title, int level) {
		OptionalInt menu = player.openMenu(
				new SimpleMenuProvider((id, inventory, merchant) -> new PiglinCuteyMerchantMenu(id, inventory, this), title)
		);
		if (menu.isPresent()) {
			MerchantOffers merchantoffers = this.getOffers();
			if (!merchantoffers.isEmpty()) {
				player.sendMerchantOffers(menu.getAsInt(), merchantoffers, level, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
			}
		}
	}

	@Override
	public void setTradingPlayer(@Nullable Player player) {
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
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return ECSounds.PIGLIN_CUTEY_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.PIGLIN_CUTEY_DEATH;
	}

	@Override
	protected SoundEvent getTradeUpdatedSound(boolean correct) {
		return correct ? ECSounds.PIGLIN_CUTEY_YES : ECSounds.PIGLIN_CUTEY_NO;
	}

	@Override
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

	private void updateSpecialPrices(Player player) {
		int i = this.getPlayerReputation(player);
		if (i != 0) {
			for(MerchantOffer merchantoffer : this.getOffers()) {
				merchantoffer.addToSpecialPriceDiff(-Mth.floor((float)i * merchantoffer.getPriceMultiplier()));
			}
		}

		if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
			MobEffectInstance mobeffectinstance = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
			if(mobeffectinstance != null) {
				int k = mobeffectinstance.getAmplifier();

				for (MerchantOffer merchantoffer : this.getOffers()) {
					double d0 = 0.1D + 0.05D * (double) k;
					int j = (int) Math.floor(d0 * (double) merchantoffer.getBaseCostA().getCount());
					merchantoffer.addToSpecialPriceDiff(-Math.max(j, 1));
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public int getPlayerReputation(Player player) {
		return (int)Math.floor(foodLevel / (MULTIPLIER_FOOD_THRESHOLD * 0.025D));
	}

	private static final int SearchRange = 30;
	private static final int VerticalSearchRange = 12;

	protected BlockPos findNearestAnchor() {
		BlockPos blockpos = this.blockPosition();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for(int k = 0; k <= VerticalSearchRange; k = k > 0 ? -k : 1 - k) {
			for(int l = 0; l < SearchRange; ++l) {
				for(int x = 0; x <= l; x = x > 0 ? -x : 1 - x) {
					for(int z = 0; z <= l; z = z > 0 ? -z : 1 - z) {
						mutable.setWithOffset(blockpos, x, k, z);
						if (this.isWithinRestriction(mutable) && this.level().getBlockState(mutable).is(Blocks.RESPAWN_ANCHOR)) {
							return mutable;
						}
					}
				}
			}
		}

		return blockpos;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		PiglinCuteyData.CODEC.encodeStart(NbtOps.INSTANCE, this.getPiglinCuteyData()).resultOrPartial(ECLogger::error).ifPresent(
				piglinCuteyData -> nbt.put("PiglinCuteyData", piglinCuteyData)
		);
		nbt.putInt("FoodLevel", this.foodLevel);
		nbt.putInt("Xp", this.cuteyXp);
		if(this.lastTradedPlayer != null) {
			nbt.putUUID("lastTradedPlayer", this.lastTradedPlayer.getUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("PiglinCuteyData", Tag.TAG_COMPOUND)) {
			DataResult<PiglinCuteyData> dataResult = PiglinCuteyData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.get("PiglinCuteyData")));
			dataResult.resultOrPartial(ECLogger::error).ifPresent(this::setPiglinCuteyData);
		}

		if (nbt.contains("FoodLevel", Tag.TAG_INT)) {
			this.foodLevel = nbt.getInt("FoodLevel");
		}

		if (nbt.contains("Xp", Tag.TAG_INT)) {
			this.cuteyXp = nbt.getInt("Xp");
		}

		if(nbt.contains("lastTradedPlayer", Tag.TAG_INT_ARRAY)) {
			this.lastTradedPlayer = this.level().getPlayerByUUID(nbt.getUUID("lastTradedPlayer"));
		}

		this.setCanPickUpLoot(true);
	}

	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) { return false; }

	static class RushToPortalGoal extends MoveToBlockGoal {
		final double stopDistance;
		final double giveupDistance;

		RushToPortalGoal(PiglinCuteyEntity cutey, double stopDistance, double giveupDistance, double speedModifier) {
			super(cutey, speedModifier, PiglinCuteyEntity.SearchRange, PiglinCuteyEntity.VerticalSearchRange);
			this.stopDistance = stopDistance;
			this.giveupDistance = giveupDistance;
		}

		@Override
		public double acceptedDistance() {
			return this.stopDistance;
		}

		@Override
		protected BlockPos getMoveToTarget() {
			return this.blockPos;
		}

		@Override
		public void start() {
			super.start();
			if(this.mob instanceof PiglinCuteyEntity cutey) {
				cutey.playCelebrateSound();
			}
		}

		@Override
		public void tick() {
			super.tick();
			if(this.isReachedTarget()) {
				this.stop();
			}
		}

		@Override
		public void stop() {
			this.mob.getNavigation().stop();

			if(this.mob instanceof PiglinCuteyEntity cutey && this.isReachedTarget()) {
				if (cutey.lastTradedPlayer != null && !isTooFarAway(cutey.lastTradedPlayer, this.giveupDistance) && !cutey.level().isClientSide) {
					cutey.level().addFreshEntity(new ItemEntity(
							cutey.lastTradedPlayer.level(),
							cutey.lastTradedPlayer.getX(),
							cutey.lastTradedPlayer.getY() + 0.5D,
							cutey.lastTradedPlayer.getZ(),
							new ItemStack(Items.GOLD_BLOCK, ECCommonConfig.PIGLIN_CUTEY_GIFT.get())
					));
					ECTriggers.PIGLIN_CUTEY.trigger((ServerPlayer) cutey.lastTradedPlayer);
				}

				cutey.discard();
			}
		}

		@Override
		protected boolean isValidTarget(LevelReader level, BlockPos blockPos) {
			return level.getBlockState(blockPos).is(Blocks.NETHER_PORTAL);
		}

		private boolean isTooFarAway(Entity entity, double dist) {
			return !entity.closerThan(this.mob, dist);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	static {
		EntityDataSerializers.registerSerializer(PIGLIN_CUTEY_DATA);
	}
}
