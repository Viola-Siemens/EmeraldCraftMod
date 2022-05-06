package com.hexagram2021.emeraldcraft.client.models.entities;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.world.ECTrades;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Set;

public class NetherPigmanEntity extends AbstractVillager {
	private static final int NUMBER_OF_TRADE_OFFERS = 2;

	private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(
			Items.COOKED_PORKCHOP, Items.PORKCHOP,
			Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE,
			Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO,
			Items.DIAMOND, ECItems.DIAMOND_NUGGET.get()
	);

	public NetherPigmanEntity(EntityType<? extends NetherPigmanEntity> entityType, Level level) {
		super(entityType, level);
		((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractSkeleton.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35D));
		this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	@Override
	@Nullable
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return null;
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	public boolean wantsToPickUp(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return WANTED_ITEMS.contains(item) && this.getInventory().canAddItem(itemStack);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(ECItems.NETHER_PIGMAN_SPAWN_EGG.get()) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
			//if (hand == InteractionHand.MAIN_HAND) {
			//	player.awardStat(Stats.TALKED_TO_VILLAGER);
			//}

			if (this.getOffers().isEmpty()) {
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			if (!this.level.isClientSide) {
				this.setTradingPlayer(player);
				this.openTradingScreen(player, this.getDisplayName(), 1);
			}

			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void updateTrades() {
		VillagerTrades.ItemListing[] list1 = ECTrades.NETHER_PIGMAN_TRADES.get(1);
		VillagerTrades.ItemListing[] list2 = ECTrades.NETHER_PIGMAN_TRADES.get(2);
		VillagerTrades.ItemListing[] list3 = ECTrades.NETHER_PIGMAN_TRADES.get(3);
		if (list1 != null && list2 != null) {
			MerchantOffers merchantoffers = this.getOffers();
			this.addOffersFromItemListings(merchantoffers, list1, NUMBER_OF_TRADE_OFFERS);
			int i = this.random.nextInt(list2.length);
			VillagerTrades.ItemListing itemListing1 = list2[i];
			int j = this.random.nextInt(list3.length);
			VillagerTrades.ItemListing itemListing2 = list3[j];
			MerchantOffer merchantoffer1 = itemListing1.getOffer(this, this.random);
			if (merchantoffer1 != null) {
				merchantoffers.add(merchantoffer1);
			}
			MerchantOffer merchantoffer2 = itemListing2.getOffer(this, this.random);
			if (merchantoffer2 != null) {
				merchantoffers.add(merchantoffer2);
			}
		}
	}

	@Override
	public boolean removeWhenFarAway(double p_35886_) {
		return false;
	}

	@Override
	protected void rewardTradeXp(MerchantOffer offer) {
		if (offer.shouldRewardExp()) {
			int i = 3 + this.random.nextInt(4);
			this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
		}
	}

	@Override
	public void setLastHurtByMob(@Nullable LivingEntity entity) {
		if (entity != null && this.level instanceof ServerLevel) {
			if (this.isAlive() && entity instanceof Player) {
				for(int i = 0; i < this.getInventory().getContainerSize(); ++i) {
					ItemStack itemStack = this.getInventory().getItem(i);
					if(itemStack.getCount() != 0) {
						int cnt = Math.max(itemStack.getCount(), this.random.nextInt(3));
						this.level.addFreshEntity(new ItemEntity(
								this.level, (this.getX() + entity.getX() * 2) / 3, this.getY() + 0.5D, (this.getZ() + entity.getZ() * 2) / 3, new ItemStack(itemStack.getItem(), cnt)
						));
						itemStack.shrink(cnt);
					}
				}
			}
		}

		super.setLastHurtByMob(entity);
	}

	protected SoundEvent getAmbientSound() {
		return this.isTrading() ? ECSounds.NETHER_PIGMAN_TRADE : ECSounds.NETHER_PIGMAN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return ECSounds.NETHER_PIGMAN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return ECSounds.NETHER_PIGMAN_DEATH;
	}

	protected SoundEvent getTradeUpdatedSound(boolean correct) {
		return correct ? ECSounds.NETHER_PIGMAN_YES : ECSounds.NETHER_PIGMAN_NO;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 25.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}
}
