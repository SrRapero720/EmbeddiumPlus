package me.srrapero720.embeddiumplus.mixins.impl.frames;

import me.srrapero720.embeddiumplus.features.frame_overlay.MinFrameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;

import java.util.LinkedList;
import java.util.Objects;

@Mixin(ForgeGui.class)
public class FrameCounterMixin {
    @Unique
    private int embeddiumExtras$lastMeasuredFPS;
    @Unique
    private String embeddiumExtras$runningAverageFPS;
    @Unique
    private final LinkedList<Integer> embeddiumExtras$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics matrixStack, float tickDelta, CallbackInfo info) {
        String displayString;
        switch (EmbeddiumPlusConfig.fpsCounterMode.get()) {
            case SIMPLE -> {
                int fps = FpsAccessorMixin.getFps();
                displayString = String.valueOf(fps);
            }

            case ADVANCED -> {
                int fps = FpsAccessorMixin.getFps();
                displayString = GetAdvancedFPSString(fps);
            }

            default -> {
                return;
            }
        }

        Minecraft client = Minecraft.getInstance();
        if (client.options.renderDebug && !client.options.renderFpsChart) return; // No render when F3 is open

        float textPos = EmbeddiumPlusConfig.fpsCounterPosition.get();

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


    private String GetAdvancedFPSString(int fps) {
        MinFrameProvider.recalculate();

        if (embeddiumExtras$lastMeasuredFPS != fps) {
            embeddiumExtras$lastMeasuredFPS = fps;

            if (embeddiumExtras$fpsRunningAverageQueue.size() > 14)
                embeddiumExtras$fpsRunningAverageQueue.poll();

            embeddiumExtras$fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (var frameTime : embeddiumExtras$fpsRunningAverageQueue) {
                totalFps += frameTime;
                frameCount++;
            }

            int average = (int) (totalFps / frameCount);
            embeddiumExtras$runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + MinFrameProvider.getLastMinFrame() + " | AVG " + embeddiumExtras$runningAverageFPS;
    }
}