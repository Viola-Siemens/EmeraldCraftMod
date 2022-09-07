package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PiglinCuteyData {
	private final int level;

	public static final int MIN_LEVEL = 1;
	public static final int MAX_LEVEL = 5;
	private static final int[] NEXT_LEVEL_XP_THRESHOLDS = new int[]{0, 15, 75, 180, 300};
	public static final Codec<PiglinCuteyData> CODEC = RecordCodecBuilder.create(
			(in) -> in.group(Codec.INT.fieldOf("level").orElse(1).forGetter(
					(data) -> data.level)
			).apply(in, PiglinCuteyData::new)
	);

	public PiglinCuteyData(int level) {
		this.level = Math.max(MIN_LEVEL, level);
	}

	public int getLevel() {
		return this.level;
	}

	public PiglinCuteyData setLevel(int newLevel) {
		return new PiglinCuteyData(newLevel);
	}

	public static int getMinXpPerLevel(int level) {
		return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level - 1] : 0;
	}

	public static int getMaxXpPerLevel(int level) {
		return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level] : 0;
	}

	public static boolean canLevelUp(int level) {
		return level >= MIN_LEVEL && level < MAX_LEVEL;
	}
}
