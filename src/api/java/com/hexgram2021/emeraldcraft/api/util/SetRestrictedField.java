package com.hexgram2021.emeraldcraft.api.util;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.ArrayList;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class SetRestrictedField<T> {
	private static final InitializationTracker CLIENT_FIELDS = new InitializationTracker();
	private static final InitializationTracker COMMON_FIELDS = new InitializationTracker();

	private final InitializationTracker tracker;
	private T value;

	private SetRestrictedField(InitializationTracker tracker) {
		this.tracker = tracker;
	}

	public static <T> SetRestrictedField<T> client() {
		return CLIENT_FIELDS.make();
	}

	public static <T> SetRestrictedField<T> common() {
		return COMMON_FIELDS.make();
	}

	public static void lock(boolean client) {
		if(client)
			CLIENT_FIELDS.lock();
		else
			COMMON_FIELDS.lock();
	}

	public static void startInitializing(boolean client) {
		if(client)
			CLIENT_FIELDS.startInitialization();
		else
			COMMON_FIELDS.startInitialization();
	}

	public void setValue(T value) {
		Preconditions.checkState(tracker.state==TrackerState.INITIALIZING);
		String currentMod = ModLoadingContext.get().getActiveNamespace();
		Preconditions.checkState(
				MODID.equals(currentMod),
				"Restricted fields may only be set by Emerald Craft, current mod is %s", currentMod
		);
		this.value = value;
	}

	public T getValue() {
		return Preconditions.checkNotNull(value);
	}

	public boolean isInitialized() {
		return value!=null;
	}

	private static class InitializationTracker {
		private final List<Pair<Exception, SetRestrictedField<?>>> fields = new ArrayList<>();
		private TrackerState state = TrackerState.OPEN;

		<T> SetRestrictedField<T> make() {
			Preconditions.checkState(state!=TrackerState.LOCKED);
			SetRestrictedField<T> result = new SetRestrictedField<>(this);
			fields.add(Pair.of(new RuntimeException("Field created here"), result));
			return result;
		}

		public void startInitialization() {
			Preconditions.checkState(state==TrackerState.OPEN);
			state = TrackerState.INITIALIZING;
		}

		void lock() {
			Preconditions.checkState(state==TrackerState.INITIALIZING);
			for(Pair<Exception, SetRestrictedField<?>> field : fields)
				if(!field.getSecond().isInitialized())
					throw new RuntimeException(field.getFirst());
			state = TrackerState.LOCKED;
		}
	}

	private enum TrackerState {
		OPEN,
		INITIALIZING,
		LOCKED
	}
}