package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PiglinCuteyTrigger extends AbstractCriterionTrigger<PiglinCuteyTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "saved_piglin_cutey");

	@Override @Nonnull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @Nonnull
	public PiglinCuteyTrigger.TriggerInstance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate entity, @Nonnull ConditionArrayParser context) {
		return new TriggerInstance(entity);
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, instance -> true);
	}

	public static class TriggerInstance extends CriterionInstance {
		public TriggerInstance(EntityPredicate.AndPredicate entity) {
			super(PiglinCuteyTrigger.ID, entity);
		}
	}
}
