package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class Borderless {
    public static void setFullScreenMode(final Mode value) {
        final Minecraft client = Minecraft.getInstance();
        final Options opts = client.options;

        ChlorideConfig.fullScreen = value;
        opts.fullscreen().set(value != Mode.WINDOWED);

        final Window window = client.getWindow();

        if (window.isFullscreen() != opts.fullscreen().get()) {
            window.toggleFullScreen();
            opts.fullscreen().set(window.isFullscreen());
        }

        if (opts.fullscreen().get()) {
            window.dirty = true;
            window.changeFullscreenVideoMode();
        }
    }

    public enum AttachMode {
        ATTACH, REPLACE, OFF
    }

    public enum Mode {
        WINDOWED, BORDERLESS, FULLSCREEN;

        public static Mode nextOf(final Mode current) {
            return switch (current) {
                case WINDOWED -> BORDERLESS;
                case BORDERLESS -> FULLSCREEN;
                case FULLSCREEN -> WINDOWED;
            };
        }

        public static Mode nextBorderless(final Mode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> BORDERLESS;
            };
        }

        public static Mode nextFullscreen(final Mode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> FULLSCREEN;
            };
        }

        public static Mode getVanillaConfig() {
            return Minecraft.getInstance().options.fullscreen().get() ? BORDERLESS : WINDOWED;
        }

        public boolean isBorderless() {
            return this == BORDERLESS;
        }
    }
}
