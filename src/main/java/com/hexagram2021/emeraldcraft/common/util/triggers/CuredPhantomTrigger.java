package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.storage.loot.LootContext;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredPhantomTrigger extends SimpleCriterionTrigger<CuredPhantomTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "cured_phantom");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public CuredPhantomTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate entity, DeserializationContext context) {
		ContextAwarePredicate phantom = EntityPredicate.fromJson(json, "phantom", context);
		ContextAwarePredicate manta = EntityPredicate.fromJson(json, "manta", context);
		return new CuredPhantomTrigger.TriggerInstance(entity, phantom, manta);
	}

	public void trigger(ServerPlayer player, Phantom phantom, MantaEntity manta) {
		LootContext phantomContext = EntityPredicate.createContext(player, phantom);
		LootContext mantaContext = EntityPredicate.createContext(player, manta);
		this.trigger(player, instance -> instance.matches(phantomContext, mantaContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ContextAwarePredicate phantom;
		private final ContextAwarePredicate manta;

		public TriggerInstance(ContextAwarePredicate entity, ContextAwarePredicate phantom, ContextAwarePredicate manta) {
			super(CuredPhantomTrigger.ID, entity);
			this.phantom = phantom;
			this.manta = manta;
		}

		public boolean matches(LootContext phantomContext, LootContext mantaContext) {
			if (!this.phantom.matches(phantomContext)) {
				return false;
			}
			return this.manta.matches(mantaContext);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("phantom", this.phantom.toJson(context));
			jsonobject.add("manta", this.manta.toJson(context));
			return jsonobject;
		}
	}
}
