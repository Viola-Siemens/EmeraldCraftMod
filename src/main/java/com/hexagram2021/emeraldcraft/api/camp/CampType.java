package com.hexagram2021.emeraldcraft.api.camp;

import com.hexagram2021.emeraldcraft.api.codec.APIEnumCodec;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public interface CampType extends StringRepresentable {
	Codec<CampType> CODEC = APIEnumCodec.instance(CampType::values, CampTypes.ALL_CAMP_IDS);

	@Override @NotNull
	default String getSerializedName() {
		return this.toString();
	}

	@Override
	String toString();

	static CampType[] values() {
		return CampTypes.ALL_CAMPS.toArray(new CampType[0]);
	}
}