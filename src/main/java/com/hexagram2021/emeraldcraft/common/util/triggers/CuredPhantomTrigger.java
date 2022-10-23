package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredPhantomTrigger extends AbstractCriterionTrigger<CuredPhantomTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "cured_phantom");

	@Override @Nonnull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @Nonnull
	public CuredPhantomTrigger.TriggerInstance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate entity, @Nonnull ConditionArrayParser context) {
		EntityPredicate.AndPredicate phantom = EntityPredicate.AndPredicate.fromJson(json, "phantom", context);
		EntityPredicate.AndPredicate manta = EntityPredicate.AndPredicate.fromJson(json, "manta", context);
		return new CuredPhantomTrigger.TriggerInstance(entity, phantom, manta);
	}

	public void trigger(ServerPlayerEntity player, PhantomEntity phantom, MantaEntity manta) {
		LootContext phantomContext = EntityPredicate.createContext(player, phantom);
		LootContext mantaContext = EntityPredicate.createContext(player, manta);
		this.trigger(player, instance -> instance.matches(phantomContext, mantaContext));
	}

	public static class TriggerInstance extends CriterionInstance {
		private final EntityPredicate.AndPredicate phantom;
		private final EntityPredicate.AndPredicate manta;

		public TriggerInstance(EntityPredicate.AndPredicate entity, EntityPredicate.AndPredicate phantom, EntityPredicate.AndPredicate manta) {
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

		@Override @Nonnull
		public JsonObject serializeToJson(@Nonnull ConditionArraySerializer context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("phantom", this.phantom.toJson(context));
			jsonobject.add("manta", this.manta.toJson(context));
			return jsonobject;
		}
	}
}
