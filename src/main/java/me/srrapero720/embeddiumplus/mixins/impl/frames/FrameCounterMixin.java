package me.srrapero720.embeddiumplus.mixins.impl.frames;

import me.srrapero720.embeddiumplus.features.frame_overlay.MinFrameProvider;
import me.srrapero720.embeddiumplus.internal.EmbyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Mixin(ForgeGui.class)
public class FrameCounterMixin {
    @Unique private int embPlus$lastMeasuredFPS;
    @Unique private String embPlus$runningAverageFPS;
    @Unique private final LinkedList<Integer> embPlus$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics matrixStack, float tickDelta, CallbackInfo info) {
        String displayString;
        switch (EmbyConfig.fpsDisplayMode.get()) {
            case SIMPLE -> {
                int fps = FpsAccessorMixin.getFps();
                displayString = String.valueOf(fps);
            }

            case COMPLETE -> {
                int fps = FpsAccessorMixin.getFps();
                displayString = embPlus$getAdvancedFPSString(fps);
            }

            default -> {
                return;
            }
        }

        Minecraft client = Minecraft.getInstance();
        if (client.options.renderDebug && !client.options.renderFpsChart) return; // No render when F3 is open

        float textPos = EmbyConfig.fpsDisplayMarginCache;

        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        float fontScale = 0.75F;

        double guiScale = client.getWindow().getGuiScale();
        if (guiScale > 0) {
            textPos /= (float) guiScale;
        }

        // Prevent FPS-Display to render outside screenspace
        float maxTextPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString);
        float maxTextPosY = client.getWindow().getGuiScaledHeight() - client.font.lineHeight;
        textPos = Math.min(textPos, maxTextPosX);

        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

//        if (client.getWindow().getGuiScale() > 3)
//        {
//            GL11.glPushMatrix();
//            GL11.glScalef(fontScale, fontScale, fontScale);
//            client.font.drawShadow(matrixStack, displayString, textPos, textPos, drawColor);
//            GL11.glPopMatrix();
//        }
//        else
//        {
            matrixStack.drawString(client.font, displayString, textPos, textPos, drawColor, true);
        //}
    }


    @Unique
    private String embPlus$getAdvancedFPSString(int fps) {
        MinFrameProvider.recalculate();

        if (embPlus$lastMeasuredFPS != fps) {
            embPlus$lastMeasuredFPS = fps;

            if (embPlus$fpsRunningAverageQueue.size() > 14)
                embPlus$fpsRunningAverageQueue.poll();

            embPlus$fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (var frameTime : embPlus$fpsRunningAverageQueue) {
                totalFps += frameTime;
                frameCount++;
            }

            int average = (int) (totalFps / frameCount);
            embPlus$runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + MinFrameProvider.getLastMinFrame() + " | AVG " + embPlus$runningAverageFPS;
    }
}