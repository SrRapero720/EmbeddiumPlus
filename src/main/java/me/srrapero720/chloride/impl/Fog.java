package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class Fog {
    // BEYOND ANY REASONABLE RENDER DISTANCE = FOG VISUALLY GONE
    private static final float FOG_END = 1_000_000.0F;

    public static void apply(final FogData data, final ResourceKey<Level> dimension) {
        if (!ChlorideConfig.fog.enabled) { // FOG IS DISABLED
            override(data, FOG_END, FOG_END);
            return;
        }

        if (ChlorideConfig.fog.custom) { // OVERRIDE FOG AT ALL
            override(data, ChlorideConfig.fog.start, ChlorideConfig.fog.end);
            return;
        }

        // TOGGLE PER LEVEL
        if ((dimension == Level.OVERWORLD && !ChlorideConfig.fog.onOverworld)
                || (dimension == Level.NETHER && !ChlorideConfig.fog.onNether)
                || (dimension == Level.END && !ChlorideConfig.fog.onEnd)) {
            override(data, FOG_END, FOG_END);
        }
    }

    // 1.21.11 SPLITS FOG INTO ENVIRONMENTAL/RENDER-DISTANCE/SKY/CLOUD RANGES; THE OLD SINGLE FOG RULED THEM ALL
    private static void override(final FogData data, final float start, final float end) {
        data.environmentalStart = start;
        data.environmentalEnd = end;
        data.renderDistanceStart = start;
        data.renderDistanceEnd = end;
        data.skyEnd = end;
        data.cloudEnd = end;
    }
}
