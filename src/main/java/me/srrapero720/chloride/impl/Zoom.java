package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.platform.InputConstants;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT)
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

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Chloride.id("chloride"));
    public static final KeyMapping KEY = new KeyMapping("chloride.zoom",
                    KeyConflictContext.IN_GAME, KeyModifier.NONE,
                    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY
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
            e.setCanceled(scroll(e.getScrollDeltaY()));
    }

    @SubscribeEvent
    public static void onGetFovEvent(final ViewportEvent.ComputeFov e) {
        if (canUseZoom() && ChlorideConfig.zoom.enabled)
            e.setFOV((float) zoom(e.getFOV()));
    }

    @SubscribeEvent
    public static void registerKeys(final RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(KEY);
    }
}
