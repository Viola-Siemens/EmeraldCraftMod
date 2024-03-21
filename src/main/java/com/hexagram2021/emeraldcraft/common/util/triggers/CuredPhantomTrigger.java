package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredPhantomTrigger extends SimpleCriterionTrigger<CuredPhantomTrigger.TriggerInstance> {
	public static final ResourceLocation ID = new ResourceLocation(MODID, "cured_phantom");

	@Override
	public CuredPhantomTrigger.TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> entity, DeserializationContext context) {
		Optional<ContextAwarePredicate> phantom = EntityPredicate.fromJson(json, "phantom", context);
		Optional<ContextAwarePredicate> manta = EntityPredicate.fromJson(json, "manta", context);
		return new CuredPhantomTrigger.TriggerInstance(entity, phantom, manta);
	}

	public void trigger(ServerPlayer player, Phantom phantom, MantaEntity manta) {
		LootContext phantomContext = EntityPredicate.createContext(player, phantom);
		LootContext mantaContext = EntityPredicate.createContext(player, manta);
		this.trigger(player, instance -> instance.matches(phantomContext, mantaContext));
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final Optional<ContextAwarePredicate> phantom;
		private final Optional<ContextAwarePredicate> manta;

		public TriggerInstance(Optional<ContextAwarePredicate> entity, Optional<ContextAwarePredicate> phantom, Optional<ContextAwarePredicate> manta) {
			super(entity);
			this.phantom = phantom;
			this.manta = manta;
		}

		public boolean matches(LootContext phantomContext, LootContext mantaContext) {
			if (this.phantom.isPresent() && !this.phantom.get().matches(phantomContext)) {
				return false;
			}
			return this.manta.isPresent() && !this.manta.get().matches(mantaContext);
		}

		@Override
		public JsonObject serializeToJson() {
			JsonObject jsonobject = super.serializeToJson();
			this.phantom.ifPresent(predicate -> jsonobject.add("phantom", predicate.toJson()));
			this.manta.ifPresent(predicate -> jsonobject.add("manta", predicate.toJson()));
			return jsonobject;
		}
	}
}
