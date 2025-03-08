package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.srrapero720.chloride.ChlorideConfig.fullScreen;

public class BorderlessMixin {
    @Mixin(Window.class)
    public static class WindowMixin {
        @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
        private void redirect$glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
            if (ChlorideConfig.fullScreen.isBorderless()) {
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
            if (ChlorideConfig.fullScreen.isBorderless()) {
                return 1L;
            }
            return window;
        }
    }

    @Mixin(KeyboardHandler.class)
    public static class KeyboardHandlerMixin {
        @Shadow
        @Final
        public Minecraft minecraft;

        @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;toggleFullScreen()V"), cancellable = true)
        public void redirect$handleFullScreenToggle(long pWindowPointer, int pKey, int pScanCode, int pAction, int pModifiers, CallbackInfo ci) {
            switch (ChlorideConfig.borderlessAttachModeF11) {
                case ATTACH -> ChlorideConfig.setFullScreenMode(minecraft.options, ChlorideConfig.FullScreenMode.nextOf(fullScreen));
                case REPLACE -> ChlorideConfig.setFullScreenMode(minecraft.options, ChlorideConfig.FullScreenMode.nextBorderless(fullScreen));
                case OFF -> ChlorideConfig.setFullScreenMode(minecraft.options, ChlorideConfig.FullScreenMode.nextFullscreen(fullScreen));
            }
            ci.cancel();
        }
    }
}
