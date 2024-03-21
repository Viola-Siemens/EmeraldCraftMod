package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PiglinCuteyTrigger extends SimpleCriterionTrigger<PiglinCuteyTrigger.TriggerInstance> {
	public static final ResourceLocation ID = new ResourceLocation(MODID, "saved_piglin_cutey");

	@Override
	public PiglinCuteyTrigger.TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> entity, DeserializationContext context) {
		return new PiglinCuteyTrigger.TriggerInstance(entity);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		public TriggerInstance(Optional<ContextAwarePredicate> entity) {
			super(entity);
		}
	}
}
