package com.hexagram2021.emeraldcraft.common.items;

import com.hexagram2021.emeraldcraft.common.dispenser.ECBoatDispenseItemBehaviour;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class ECBoatItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntityPredicates.NO_SPECTATORS.and(Entity::isPickable);;
	private final ECBoat.ECBoatType type;

	public ECBoatItem(ECBoat.ECBoatType type, Item.Properties properties) {
		super(properties);
		this.type = type;
		DispenserBlock.registerBehavior(this, new ECBoatDispenseItemBehaviour(type));
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> use(@Nonnull World level, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		RayTraceResult rayTraceResult = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.ANY);
		if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.pass(itemstack);
		} else {
			Vector3d vec3 = player.getViewVector(1.0F);
			List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vector3d vec31 = player.getEyePosition(1.0F);

				for(Entity entity : list) {
					AxisAlignedBB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec31)) {
						return ActionResult.pass(itemstack);
					}
				}
			}

			if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
				ECBoat boat = new ECBoat(level, rayTraceResult.getLocation().x, rayTraceResult.getLocation().y, rayTraceResult.getLocation().z);
				boat.setECBoatType(this.type);
				boat.yRot = player.yRot;
				if (!level.noCollision(boat, boat.getBoundingBox())) {
					return ActionResult.fail(itemstack);
				} else {
					if (!level.isClientSide) {
						level.addFreshEntity(boat);
						if (!player.abilities.instabuild) {
							itemstack.shrink(1);
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					return ActionResult.sidedSuccess(itemstack, level.isClientSide());
				}
			} else {
				return ActionResult.pass(itemstack);
			}
		}
	}
}
