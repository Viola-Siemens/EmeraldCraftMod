package com.hexagram2021.emeraldcraft.common.items;

import com.hexagram2021.emeraldcraft.common.dispenser.ECBoatDispenseItemBehaviour;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.entities.ECChestBoat;
import com.hexagram2021.emeraldcraft.common.entities.IECBoat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class ECBoatItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	private final ECBoat.ECBoatType type;
	private final boolean hasChest;

	public ECBoatItem(boolean hasChest, ECBoat.ECBoatType type, Item.Properties properties) {
		super(properties);
		this.hasChest = hasChest;
		this.type = type;
		DispenserBlock.registerBehavior(this, new ECBoatDispenseItemBehaviour(type, hasChest));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (hitresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			Vec3 vec3 = player.getViewVector(1.0F);
			List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec31 = player.getEyePosition();

				for(Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec31)) {
						return InteractionResultHolder.pass(itemstack);
					}
				}
			}

			if (hitresult.getType() == HitResult.Type.BLOCK) {
				IECBoat boat = this.hasChest ?
						new ECChestBoat(level, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z) :
						new ECBoat(level, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z);
				boat.setECBoatType(this.type);
				Entity boatEntity = (Entity)boat;
				boatEntity.setYRot(player.getYRot());
				if (!level.noCollision(boatEntity, boatEntity.getBoundingBox())) {
					return InteractionResultHolder.fail(itemstack);
				} else {
					if (!level.isClientSide) {
						level.addFreshEntity(boatEntity);
						level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
						if (!player.getAbilities().instabuild) {
							itemstack.shrink(1);
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
				}
			} else {
				return InteractionResultHolder.pass(itemstack);
			}
		}
	}
}
