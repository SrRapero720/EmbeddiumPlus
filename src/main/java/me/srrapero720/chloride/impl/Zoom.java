package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.platform.InputConstants;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Zoom {
    private static final double EASE_DELTA = 0.15;
    private static final double DEFAULT = 3;

    private static double value = -1;
    private static double mouseSensitivity = -1;
    private static final String[] ZOOM_MODS = {
            "justzoom",
            "zume",
            "ok_zoomer",
            "zoomify",
            "zoomlens"
    };

    public static final KeyMapping KEY = new KeyMapping("chloride.zoom",
                    KeyConflictContext.IN_GAME, KeyModifier.NONE,
                    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, "Chloride"
    );

    public static double zoom(final double fov) {
        final var mouseSetting = Minecraft.getInstance().options.sensitivity();

        if (value == -1) value = DEFAULT;

        if (!KEY.isDown()) {
            value = DEFAULT;

            if (mouseSensitivity != -1) {
                mouseSetting.set(mouseSensitivity);
                mouseSensitivity = -1;
            }

            return fov;
        }

        if (mouseSensitivity == -1)
            mouseSensitivity = mouseSetting.get();

        // ZOOM VALUE AFFECTS MOUSE SENSITIVITY
        mouseSetting.set(mouseSensitivity * (1.0 / value));

        return fov / value;
    }

    public static boolean scroll(final double amount) {
        if(!KEY.isDown()) return false;

        if (value == -1) value = DEFAULT;

        value = Math.max(Math.min((amount > 0) ? value * 1.1 : (amount < 0) ? value * 0.9 : value, ChlorideConfig.zoom.max), 1);
        return true;
    }

    public static boolean canUseZoom() {
        final ModList list = ModList.get();
        for (final String s: ZOOM_MODS) { // IF ANY ZOOM MOD LISTED IS LOADED, TURN OFF OUR ZOOM
            if (list.isLoaded(s)) return false;
        }
        return true;
    }

    @SubscribeEvent
    public static void onMouseScrolling(final InputEvent.MouseScrollingEvent e) {
        if (canUseZoom() && ChlorideConfig.zoom.enabled)
            e.setCanceled(scroll(e.getScrollDelta()));
    }

    @SubscribeEvent
    public static void onGetFovEvent(final ViewportEvent.ComputeFov e) {
        if (canUseZoom() && ChlorideConfig.zoom.enabled)
            e.setFOV(zoom(e.getFOV()));
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Chloride.ID)
    public static final class ModEvents {
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerKeys(final RegisterKeyMappingsEvent event) {
            event.register(KEY);
        }
    }
}
