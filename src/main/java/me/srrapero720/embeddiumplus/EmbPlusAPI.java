package me.srrapero720.embeddiumplus;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.embeddiumplus.mixins.impl.borderless.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class EmbPlusAPI {
    public static void setFullScreenMode(Options opts, EmbPlusConfig.FullScreenMode value) {
        EmbPlusConfig.fullScreenMode.set(value);
        opts.fullscreen.set(value != EmbPlusConfig.FullScreenMode.WINDOWED);

        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();
        if (window.isFullscreen() != opts.fullscreen.get()) {
            window.toggleFullScreen();
            opts.fullscreen.set(window.isFullscreen());
        }

        if (opts.fullscreen.get()) {
            ((MainWindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }
}
