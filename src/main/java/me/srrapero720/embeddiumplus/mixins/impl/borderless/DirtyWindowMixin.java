package me.srrapero720.embeddiumplus.mixins.impl.borderless;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;
import me.srrapero720.embeddiumplus.foundation.borderless.VideoModeHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Window.class)
public abstract class DirtyWindowMixin implements VideoModeHandler {
    @Shadow private boolean dirty;
    @Shadow protected abstract void setMode();
    @Shadow @Final private WindowEventHandler eventHandler;
    @Shadow private boolean fullscreen;

    @Override
    public void changeVideoMode() {
        dirty = false;
        this.setMode();
        this.eventHandler.resizeDisplay();
    }

    @Override
    public void setFullscreenMode(boolean mode) {
        fullscreen = mode;
    }
}
