package me.srrapero720.embeddiumplus.mixins.impl.frames;

import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.frames.FPSDisplayBuilder;
import me.srrapero720.embeddiumplus.foundation.frames.MinFrameProvider;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.frames.accessors.IGpuUsage;
import me.srrapero720.embeddiumplus.mixins.impl.dynlights.DebugScreenMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Mixin(ForgeGui.class)
public abstract class FrameCounterMixin {
    @Shadow(remap = false)
    public abstract Minecraft getMinecraft();

    @Unique
    private int embPlus$lastMeasuredFPS;
    @Unique
    private String embPlus$runningAverageFPS;
    @Unique
    private final LinkedList<Integer> embPlus$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(method = "render", at = @At("HEAD"))
    public void inject$render(GuiGraphics matrixStack, float tickDelta, CallbackInfo info) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.options.renderDebug && !minecraft.options.renderFpsChart) return; // No render when F3 is open

        var displayMode = EmbyConfig.fpsDisplayMode.get();
        var displaySystemMode = EmbyConfig.fpsDisplaySystemMode.get();

        if (displayMode.off() && displaySystemMode.off()) return; // NOTHING TO DO HERE, BACK TO WORK


        var displayBuilder = new FPSDisplayBuilder();
        // FPS
        switch (displayMode) {
            case SIMPLE -> displayBuilder.append(EmbyTools.tintByLower(FpsAccessorMixin.getFps()), "FPS" + ChatFormatting.RESET);
            case ADVANCED -> this.embPlus$getFpsStr(displayBuilder);
        }

        // ATTACH NEXT VALUES
        if (!displayBuilder.isEmpty() && !displaySystemMode.off()) displayBuilder.split();

        // GPU + RAM
        switch (displaySystemMode) {
            case GPU -> displayBuilder.append(embPlus$getGpuStr());
            case RAM -> displayBuilder.append(embPlus$getMemoryStr());
            case ON -> displayBuilder.append(embPlus$getGpuStr()).append(embPlus$getMemoryStr());
        }

        if (displayBuilder.isEmpty()) throw new IllegalStateException("Someone screw mod config");

        double guiScale = minecraft.getWindow().getGuiScale();
        float textMargin = EmbyConfig.fpsDisplayMarginCache;
        textMargin = (guiScale > 0) ? textMargin / (float) guiScale : textMargin;

        // Prevent FPS-Display to render outside screenspace
        String displayString = displayBuilder.toString();

        float maxTextPosX = minecraft.getWindow().getGuiScaledWidth() - minecraft.font.width(displayString);
        float maxTextPosY = minecraft.getWindow().getGuiScaledHeight() - minecraft.font.lineHeight;
        float posX, posY;

        // CALCULATE x
        // REMOVED Math.min() because is redundant.
        posX = switch (EmbyConfig.fpsDisplayGravity.get()) {
            case LEFT -> textMargin;
            case CENTER -> (maxTextPosX / 2);
            case RIGHT -> maxTextPosX - textMargin;
        };
        posY = textMargin;

        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

        if (EmbyConfig.fpsDisplayShadowCache) {
            matrixStack.fill((int) posX - 2, (int) posY - 2, (int) posX + minecraft.font.width(displayString) + 2, (int) (posY + minecraft.font.lineHeight) + 1, -1873784752);
        }
        matrixStack.drawString(minecraft.font, displayString, posX, posY, drawColor, true);

        displayBuilder.release();
    }


    @Unique
    private void embPlus$getFpsStr(FPSDisplayBuilder builder) {
        MinFrameProvider.recalculate();
        int fps = FpsAccessorMixin.getFps();

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

        builder.append(EmbyTools.tintByLower(fps) + ChatFormatting.RESET)
                .append("MIN", MinFrameProvider.getLastMinFrame())
                .append("AVG", embPlus$runningAverageFPS);
    }


    @Unique
    private String embPlus$getGpuStr() {
        double usage = ((IGpuUsage) getMinecraft()).embPlus$getGPU();
        if (usage > 0.0d) {
            return "GPU " + EmbyTools.tintByPercent(Math.min(Math.round(usage), 100)) + "%" + ChatFormatting.RESET;
        } else {
            return "GPU --%";
        }
    }

    @Unique
    private String embPlus$getMemoryStr() {
        Runtime runtime = Runtime.getRuntime();

        // JAVA USES STUPID AND CONFUSING NAMES
        // max memory is the assigned memory (ej: -Xmx8G)
        // total memory is the allocated memory (normally isn't much)
        // used memory needs to be calculated using total memory - free memory, same with percent
        long assigned = runtime.maxMemory();
        long allocated = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = allocated - free;

//        return "MEM " + EmbyTools.bytesToMB(used) + "/" + EmbyTools.bytesToMB(assigned) + "MB (" + (used * 100) / assigned + "%)";
        return "MEM " + EmbyTools.tintByPercent((used * 100) / assigned) + "%" + ChatFormatting.RESET;
    }
}