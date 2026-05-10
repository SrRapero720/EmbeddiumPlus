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
            final boolean wasBorderless = Borderless.previousMode.isBorderless();
            // GATE THE BORDERLESS BRANCH ON VANILLA ACTUALLY WANTING FULLSCREEN (MONITOR != 0L). IF
            // VANILLA PASSES MONITOR=0L IT WANTS WINDOWED (E.G. STARTUP WITH OPTIONS.FULLSCREEN=FALSE
            // BUT CHLORIDECONFIG.FULLSCREEN=BORDERLESS DESYNCED) AND WE MUST KEEP DECORATION.
            final boolean targetBorderless = fullScreen.isBorderless() && monitor != 0L;

            if (!targetBorderless) {
                // BORDERLESS -> FULLSCREEN: GLFW CAN TREAT SETWINDOWMONITOR AS A NO-OP BECAUSE THE
                // WINDOW ALREADY COVERS THE MONITOR RECT. FORCE AN INTERMEDIATE DETACH WITH DIFFERENT
                // DIMENSIONS SO GLFW RECOGNIZES THE MODE CHANGE.
                if (wasBorderless && monitor != 0L) {
                    GLFW.glfwSetWindowMonitor(window, 0L, xpos, ypos, Math.max(1, width - 1), Math.max(1, height - 1), GLFW.GLFW_DONT_CARE);
                }

                // DECORATED IS IGNORED IN EXCLUSIVE FULLSCREEN, SO SET IT AFTER SETWINDOWMONITOR TO
                // AVOID THE INTERMEDIATE WINDOWS RE-LAYOUT THAT PRODUCES FLICKER / "WANTS TO SHRINK".
                GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
                GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
                return;
            }

            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

            // RESOLVE REAL MONITOR COORDINATES FOR MULTI-MONITOR SETUPS.
            int realX = xpos, realY = ypos;
            if (monitor != 0L) {
                final int[] mx = new int[1];
                final int[] my = new int[1];
                GLFW.glfwGetMonitorPos(monitor, mx, my);
                realX = mx[0];
                realY = my[0];
            }

            // DETACH FROM MONITOR (EXITS EXCLUSIVE FULLSCREEN). GLFW_DONT_CARE PREVENTS REFRESH
            // RATE RENEGOTIATION.
            GLFW.glfwSetWindowMonitor(window, 0L, realX, realY, width, height, GLFW.GLFW_DONT_CARE);
            GLFW.glfwSetWindowSizeLimits(window, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);
        }

        @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
        private long redirect$glfwGetWindowMonitor(final long window) {
            // VANILLA'S `FLAG = MONITOR != 0L` MEANS "WAS THE PREVIOUS STATE REAL FULLSCREEN?".
            // BORDERLESS MUST ALSO COUNT AS FULLSCREEN SO SETMODE DOESN'T OVERWRITE WINDOWEDX/Y/W/H
            // WITH THE CURRENT (BORDERLESS) COORDS. DECISION IS BASED ON THE PREVIOUS MODE, NOT THE
            // NEW ONE: THE NEW MODE IS ALREADY SET BEFORE SETMODE RUNS.
            return Borderless.previousMode.isBorderless() ? 1L : GLFW.glfwGetWindowMonitor(window);
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
