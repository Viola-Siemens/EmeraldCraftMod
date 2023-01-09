package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredPhantomTrigger extends SimpleCriterionTrigger<CuredPhantomTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "cured_phantom");

	@Override @NotNull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @NotNull
	public CuredPhantomTrigger.TriggerInstance createInstance(@NotNull JsonObject json, @NotNull EntityPredicate.Composite entity, @NotNull DeserializationContext context) {
		EntityPredicate.Composite phantom = EntityPredicate.Composite.fromJson(json, "phantom", context);
		EntityPredicate.Composite manta = EntityPredicate.Composite.fromJson(json, "manta", context);
		return new CuredPhantomTrigger.TriggerInstance(entity, phantom, manta);
	}

	public void trigger(ServerPlayer player, Phantom phantom, MantaEntity manta) {
		LootContext phantomContext = EntityPredicate.createContext(player, phantom);
		LootContext mantaContext = EntityPredicate.createContext(player, manta);
		this.trigger(player, instance -> instance.matches(phantomContext, mantaContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite phantom;
		private final EntityPredicate.Composite manta;

		public TriggerInstance(EntityPredicate.Composite entity, EntityPredicate.Composite phantom, EntityPredicate.Composite manta) {
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

		@Override @NotNull
		public JsonObject serializeToJson(@NotNull SerializationContext context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("phantom", this.phantom.toJson(context));
			jsonobject.add("manta", this.manta.toJson(context));
			return jsonobject;
		}
	}
}
