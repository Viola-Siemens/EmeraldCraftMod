package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredZombifiedPiglinTrigger extends SimpleCriterionTrigger<CuredZombifiedPiglinTrigger.TriggerInstance> {
	public static final ResourceLocation ID = new ResourceLocation(MODID, "cured_zombified_piglin");

	@Override
	public CuredZombifiedPiglinTrigger.TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> entity, DeserializationContext context) {
		Optional<ContextAwarePredicate> zombifiedPiglin = EntityPredicate.fromJson(json, "zombified_piglin", context);
		Optional<ContextAwarePredicate> piglin = EntityPredicate.fromJson(json, "piglin", context);
		return new CuredZombifiedPiglinTrigger.TriggerInstance(entity, zombifiedPiglin, piglin);
	}

	public void trigger(ServerPlayer player, ZombifiedPiglin zombifiedPiglin, AbstractPiglin piglin) {
		LootContext zombifiedPiglinContext = EntityPredicate.createContext(player, zombifiedPiglin);
		LootContext piglinContext = EntityPredicate.createContext(player, piglin);
		this.trigger(player, instance -> instance.matches(zombifiedPiglinContext, piglinContext));
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final Optional<ContextAwarePredicate> zombifiedPiglin;
		private final Optional<ContextAwarePredicate> piglin;

		public TriggerInstance(Optional<ContextAwarePredicate> entity, Optional<ContextAwarePredicate> zombifiedPiglin, Optional<ContextAwarePredicate> piglin) {
			super(entity);
			this.zombifiedPiglin = zombifiedPiglin;
			this.piglin = piglin;
		}

		public boolean matches(LootContext zombifiedPiglinContext, LootContext piglinContext) {
			if (this.zombifiedPiglin.isPresent() && !this.zombifiedPiglin.get().matches(zombifiedPiglinContext)) {
				return false;
			}
			return this.piglin.isPresent() && !this.piglin.get().matches(piglinContext);
		}

		@Override
		public JsonObject serializeToJson() {
			JsonObject jsonobject = super.serializeToJson();
			this.zombifiedPiglin.ifPresent(predicate -> jsonobject.add("zombified_piglin", predicate.toJson()));
			this.piglin.ifPresent(predicate -> jsonobject.add("piglin", predicate.toJson()));
			return jsonobject;
		}
	}
}
