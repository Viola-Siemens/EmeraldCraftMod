package com.hexagram2021.emeraldcraft.common.util.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CuredZombifiedPiglinTrigger extends AbstractCriterionTrigger<CuredZombifiedPiglinTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(MODID, "cured_zombified_piglin");

	@Override @Nonnull
	public ResourceLocation getId() {
		return ID;
	}

	@Override @Nonnull
	public CuredZombifiedPiglinTrigger.TriggerInstance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate entity, @Nonnull ConditionArrayParser context) {
		EntityPredicate.AndPredicate zombifiedPiglin = EntityPredicate.AndPredicate.fromJson(json, "zombified_piglin", context);
		EntityPredicate.AndPredicate piglin = EntityPredicate.AndPredicate.fromJson(json, "piglin", context);
		return new CuredZombifiedPiglinTrigger.TriggerInstance(entity, zombifiedPiglin, piglin);
	}

	public void trigger(ServerPlayerEntity player, ZombifiedPiglinEntity zombifiedPiglin, AbstractPiglinEntity piglin) {
		LootContext zombifiedPiglinContext = EntityPredicate.createContext(player, zombifiedPiglin);
		LootContext piglinContext = EntityPredicate.createContext(player, piglin);
		this.trigger(player, instance -> instance.matches(zombifiedPiglinContext, piglinContext));
	}

	public static class TriggerInstance extends CriterionInstance {
		private final EntityPredicate.AndPredicate zombifiedPiglin;
		private final EntityPredicate.AndPredicate piglin;

		public TriggerInstance(EntityPredicate.AndPredicate entity, EntityPredicate.AndPredicate zombifiedPiglin, EntityPredicate.AndPredicate piglin) {
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

		@Override @Nonnull
		public JsonObject serializeToJson(@Nonnull ConditionArraySerializer context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("zombified_piglin", this.zombifiedPiglin.toJson(context));
			jsonobject.add("piglin", this.piglin.toJson(context));
			return jsonobject;
		}
	}
}
