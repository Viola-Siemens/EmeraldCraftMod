package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PiglinCuteyTrigger extends SimpleCriterionTrigger<PiglinCuteyTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "saved_piglin_cutey");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public PiglinCuteyTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate entity, DeserializationContext context) {
		return new PiglinCuteyTrigger.TriggerInstance(entity);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		public TriggerInstance(ContextAwarePredicate entity) {
			super(PiglinCuteyTrigger.ID, entity);
		}
	}
}
