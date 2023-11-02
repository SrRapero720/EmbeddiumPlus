package me.srrapero720.embeddiumplus.mixins.FrameCounter;

import me.srrapero720.embeddiumplus.features.FrameCounter.MinFrameProvider;
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
        if (Objects.equals(EmbeddiumPlusConfig.fpsCounterMode.get(), "OFF")) return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart) return;

        String displayString = null;
        int fps = FpsAccessorMixin.getFPS();

        if (Objects.equals(EmbeddiumPlusConfig.fpsCounterMode.get(), "ADVANCED"))
            displayString = GetAdvancedFPSString(fps);
        else
            displayString = String.valueOf(fps);

        float textPos = EmbeddiumPlusConfig.fpsCounterPosition.get();

        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        float fontScale = 0.75F;

        double guiScale = client.getWindow().getGuiScale();
        if (guiScale > 0) {
            textPos /= guiScale;
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