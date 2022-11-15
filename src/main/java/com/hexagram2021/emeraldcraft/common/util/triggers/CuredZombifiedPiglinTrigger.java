package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredZombifiedPiglinTrigger extends SimpleCriterionTrigger<CuredZombifiedPiglinTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "cured_zombified_piglin");

	@Override @NotNull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @NotNull
	public CuredZombifiedPiglinTrigger.TriggerInstance createInstance(@NotNull JsonObject json, @NotNull EntityPredicate.Composite entity, @NotNull DeserializationContext context) {
		EntityPredicate.Composite zombifiedPiglin = EntityPredicate.Composite.fromJson(json, "zombified_piglin", context);
		EntityPredicate.Composite piglin = EntityPredicate.Composite.fromJson(json, "piglin", context);
		return new CuredZombifiedPiglinTrigger.TriggerInstance(entity, zombifiedPiglin, piglin);
	}

	public void trigger(ServerPlayer player, ZombifiedPiglin zombifiedPiglin, AbstractPiglin piglin) {
		LootContext zombifiedPiglinContext = EntityPredicate.createContext(player, zombifiedPiglin);
		LootContext piglinContext = EntityPredicate.createContext(player, piglin);
		this.trigger(player, instance -> instance.matches(zombifiedPiglinContext, piglinContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite zombifiedPiglin;
		private final EntityPredicate.Composite piglin;

		public TriggerInstance(EntityPredicate.Composite entity, EntityPredicate.Composite zombifiedPiglin, EntityPredicate.Composite piglin) {
			super(CuredZombifiedPiglinTrigger.ID, entity);
			this.zombifiedPiglin = zombifiedPiglin;
			this.piglin = piglin;
		}

		public boolean matches(LootContext zombifiedPiglinContext, LootContext piglinContext) {
			if (!this.zombifiedPiglin.matches(zombifiedPiglinContext)) {
				return false;
			}
			return this.piglin.matches(piglinContext);
		}

		@Override @NotNull
		public JsonObject serializeToJson(@NotNull SerializationContext context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("zombified_piglin", this.zombifiedPiglin.toJson(context));
			jsonobject.add("piglin", this.piglin.toJson(context));
			return jsonobject;
		}
	}
}
