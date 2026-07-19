package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT)
public class Fog {
    // BEYOND ANY REASONABLE RENDER DISTANCE = FOG VISUALLY GONE
    private static final float FOG_END = 1_000_000.0F;

    @SubscribeEvent
    public static void onRenderFog(final ViewportEvent.RenderFog e) {
        // ONLY THE ATMOSPHERIC PATH IS TOUCHED; FLUID AND MOB-EFFECT FOGS PICK OTHER ENVIRONMENTS
        if (!(e.getEnvironment() instanceof AtmosphericFogEnvironment)) return;

        final FogData data = e.getFogData();
        if (!ChlorideConfig.fog.enabled) { // FOG IS DISABLED
            override(data, FOG_END, FOG_END);
            return;
        }

        if (ChlorideConfig.fog.custom) { // OVERRIDE FOG AT ALL
            override(data, ChlorideConfig.fog.start, ChlorideConfig.fog.end);
            return;
        }

        // TOGGLE PER LEVEL
        final var dimension = Minecraft.getInstance().level.dimension();
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
