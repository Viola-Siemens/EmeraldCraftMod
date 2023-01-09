package com.hexagram2021.emeraldcraft.client;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECMapDecorationTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MapCustomIcons {
	private static final RenderType EC_MAP_ICONS = RenderType.text(new ResourceLocation(MODID, "textures/map/map_icons.png"));
	public static final Map<MapDecoration.Type, RenderType> RENDER_TYPES = ImmutableMap.of(ECMapDecorationTypes.SHELTER, EC_MAP_ICONS, ECMapDecorationTypes.ENTRENCHMENT, EC_MAP_ICONS);
	public static final Map<MapDecoration.Type, Byte> ORDINARIES = ImmutableMap.of(ECMapDecorationTypes.SHELTER, (byte)0, ECMapDecorationTypes.ENTRENCHMENT, (byte)1);
}
