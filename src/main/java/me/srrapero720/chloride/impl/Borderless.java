package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class Borderless {
    public static Mode previousMode = Mode.WINDOWED;

    public static void reloadFullscreenMode() {
        setFullScreenMode(ChlorideConfig.fullScreen);
    }

    public static void setFullScreenMode(final Mode value) {
        final Minecraft client = Minecraft.getInstance();
        final Options opts = client.options;
        final Window window = client.getWindow();

        previousMode = ChlorideConfig.fullScreen;
        ChlorideConfig.fullScreen = value;
        opts.fullscreen().set(value != Mode.WINDOWED);

        // options.fullscreen.set() ALREADY CALLS window.toggleFullscreen() AS A SIDE-EFFECT WHEN THE
        // BOOLEAN CHANGES, SO THIS BRANCH USUALLY NO-OPS. IT ONLY FIRES FOR BORDERLESS <-> FULLSCREEN
        // (BOTH SHARE WINDOW.FULLSCREEN=TRUE) WHERE VANILLA'S UPDATEDISPLAY PATH WON'T TRIGGER SETMODE.
        final boolean toggled;
        if (window.isFullscreen() != opts.fullscreen().get()) {
            window.toggleFullScreen();
            opts.fullscreen().set(window.isFullscreen());
            toggled = true;
        } else {
            toggled = false;
        }

        if (!toggled && opts.fullscreen().get()) {
            window.dirty = true;
            window.changeFullscreenVideoMode();
        }

        // BUMP PREVIOUSMODE TO THE NEW MODE SO THE NEXT SETMODE (THE DUPLICATE ONE FIRED BY
        // UPDATEDISPLAY -> UPDATEFULLSCREEN ONE FRAME LATER) SEES THE POST-TRANSITION STATE AND
        // WON'T OVERWRITE WINDOWEDX/Y/W/H WITH THE ALREADY-TRANSITIONED COORDS.
        previousMode = ChlorideConfig.fullScreen;
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
