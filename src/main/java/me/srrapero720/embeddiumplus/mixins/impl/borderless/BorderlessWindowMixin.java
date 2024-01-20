package me.srrapero720.embeddiumplus.mixins.impl.borderless;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.embeddiumplus.EmbyConfig;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Window.class)
public class BorderlessWindowMixin {
    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void redirect$glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        if (!EmbyConfig.isLoaded()) EmbyConfig.load();

        if (EmbyConfig.fullScreen.get().isBorderless()) {
            if (monitor != 0L) {
                GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
            }

            GLFW.glfwSetWindowMonitor(window, 0L, xpos, ypos, width, height, refreshRate);
        } else {
            GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
        }
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
    private long redirect$glfwGetWindowMonitor(long window) {
        if (!EmbyConfig.isLoaded()) EmbyConfig.load();
        if (EmbyConfig.fullScreen.get().isBorderless()) {
            return 1L;
        }
        return window;
    }
}