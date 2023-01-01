package vice.rubidium_extras.mixins.FrameCounter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Mixin(ForgeGui.class)
public class FrameCounterMixin
{

    private int lastMeasuredFPS;
    private String runningAverageFPS;
    private final Queue<Integer> fpsRunningAverageQueue = new LinkedList<Integer>();

    @Inject(at = @At("HEAD"), method = "render")
    public void render(PoseStack matrixStack, float tickDelta, CallbackInfo info)
    {
        if (Objects.equals(MagnesiumExtrasConfig.fpsCounterMode.get(), "OFF"))
            return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart)
            return;

        String displayString = null;
        int fps = vice.rubidium_extras.mixins.FrameCounter.FpsAccessorMixin.getFPS();

        if (Objects.equals(MagnesiumExtrasConfig.fpsCounterMode.get(), "ADVANCED"))
            displayString = GetAdvancedFPSString(fps);
        else
            displayString = String.valueOf(fps);

        float textPos = (int)MagnesiumExtrasConfig.fpsCounterPosition.get();

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
            client.font.drawShadow(matrixStack, displayString, textPos, textPos, drawColor);
        //}
    }


    private String GetAdvancedFPSString(int fps)
    {
        vice.rubidium_extras.features.FrameCounter.MinFrameProvider.recalculate();

        if (lastMeasuredFPS != fps)
        {
            lastMeasuredFPS = fps;

            if (fpsRunningAverageQueue.size() > 14)
                fpsRunningAverageQueue.poll();

            fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (var frameTime : fpsRunningAverageQueue)
            {
                totalFps += frameTime;
                frameCount++;
            }

            int average = (int) (totalFps / frameCount);
            runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + vice.rubidium_extras.features.FrameCounter.MinFrameProvider.getLastMinFrame() + " | AVG " + runningAverageFPS;
    }
}

