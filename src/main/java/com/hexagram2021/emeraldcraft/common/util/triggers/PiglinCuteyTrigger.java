package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PiglinCuteyTrigger extends SimpleCriterionTrigger<PiglinCuteyTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "saved_piglin_cutey");

	@Override @NotNull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @NotNull
	public PiglinCuteyTrigger.TriggerInstance createInstance(@NotNull JsonObject json, @NotNull EntityPredicate.Composite entity, @NotNull DeserializationContext context) {
		return new PiglinCuteyTrigger.TriggerInstance(entity);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		public TriggerInstance(EntityPredicate.Composite entity) {
			super(PiglinCuteyTrigger.ID, entity);
		}
	}
}
