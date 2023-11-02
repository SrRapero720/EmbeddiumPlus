package me.srrapero720.embeddiumplus.mixins.BorderlessFullscreen;

import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;

@Mixin(Window.class)
public class WindowMixin {
    @Redirect(method = "setMode", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        if (!EmbeddiumPlusConfig.ConfigSpec.isLoaded()) {
            GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
            return;
        }

        if (EmbeddiumPlusConfig.fullScreenMode.get() == EmbeddiumPlusConfig.FullscreenMode.BORDERLESS) {
            if (monitor != 0L) {
                GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
            }

            GLFW.glfwSetWindowMonitor(window, 0L, xpos, ypos, width, height, refreshRate);
        } else {
            GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
        }
    }
}