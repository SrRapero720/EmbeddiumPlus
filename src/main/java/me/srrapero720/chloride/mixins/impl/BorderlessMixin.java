package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.platform.Window;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
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

class BorderlessMixin {

    @Mixin(Window.class)
    public static class WindowMixin {
        @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
        private void redirect$glfwSetWindowMonitor(final long window, final long monitor, final int xpos, final int ypos, final int width, final int height, final int refreshRate) {
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
        private long redirect$glfwGetWindowMonitor(final long window) {
            if (ChlorideConfig.fullScreen.isBorderless()) {
                return 1L;
            }
            return window;
        }
    }

    @Mixin(KeyboardHandler.class)
    public static class KeyboardHandlerMixin {
        @Shadow @Final public Minecraft minecraft;

        @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;toggleFullScreen()V"), cancellable = true)
        public void redirect$handleFullScreenToggle(final long pWindowPointer, final int pKey, final int pScanCode, final int pAction, final int pModifiers, final CallbackInfo ci) {
            switch (ChlorideConfig.borderlessAttachModeF11.ordinal()) {
                case 0 -> Borderless.setFullScreenMode(Borderless.Mode.nextOf(fullScreen));
                case 1 -> Borderless.setFullScreenMode(Borderless.Mode.nextBorderless(fullScreen));
                case 2 -> Borderless.setFullScreenMode(Borderless.Mode.nextFullscreen(fullScreen));
            }
            ci.cancel();
        }
    }
}
