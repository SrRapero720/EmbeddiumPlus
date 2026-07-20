package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.platform.InputConstants;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Zoom {
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

    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Chloride.id("chloride"));
    public static final KeyMapping KEY = new KeyMapping("chloride.zoom",
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
        if (!KEY.isDown()) return false;

        if (value == -1) value = DEFAULT;

        value = Math.max(Math.min((amount > 0) ? value * 1.1 : (amount < 0) ? value * 0.9 : value, ChlorideConfig.zoom.max), 1);
        return true;
    }

    public static boolean canUseZoom() {
        for (final String s: ZOOM_MODS) { // IF ANY ZOOM MOD LISTED IS LOADED, TURN OFF OUR ZOOM
            if (Chloride.installed(s)) return false;
        }
        return true;
    }
}
