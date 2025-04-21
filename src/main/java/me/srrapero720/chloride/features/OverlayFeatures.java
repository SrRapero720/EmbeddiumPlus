package me.srrapero720.chloride.features;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.Tools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FrameTimer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OverlayFeatures {
    private static final FPSDisplayBuilder DISPLAY = new FPSDisplayBuilder();

    private static final Component MSG_FPS = Component.translatable("chloride.options.displayfps.fps");
    private static final Component MSG_MIN = Component.translatable("chloride.options.displayfps.min");
    private static final Component MSG_AVG = Component.translatable("chloride.options.displayfps.avg");
    private static final Component MSG_GPU = Component.translatable("chloride.options.displayfps.gpu");
    private static final Component MSG_MEM = Component.translatable("chloride.options.displayfps.mem");

    private static int fps = -1;
    private static int minFPS = -1;
    private static int avgFPS = -1;
    private static int gpuPercent = -1;
    private static int memUsage = -1;

    // AVG
    private static final int[] avgCount = new int[24];
    private static boolean avgFilled = false;
    private static int avgIndex = 0;

    public static void pushAvgFps(int value) {
        if (avgIndex == avgCount.length) {
            avgIndex = 0;
            avgFilled = true;
        }

        if (!avgFilled) {
            Arrays.fill(avgCount, avgIndex, avgCount.length, value);
        }

        avgCount[avgIndex++] = value;
    }

    public static int calculateAverage() {
        int times = 0;
        for (int i: avgCount) {
            times += i;
        }

        return times / avgCount.length;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlayItem(RenderGuiOverlayEvent.Pre event) {
        if (!event.getOverlay().id().getPath().equals("debug_text")) return;

        // cancel rendering text if chart is displaying
        if (Minecraft.getInstance().options.renderFpsChart) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        var mc = Minecraft.getInstance();

        // PRECALCULATE
        fps = mc.getFps();
        minFPS = minFPS(mc);
        memUsage = (int) ((Tools.ramUsed() * 100) / Runtime.getRuntime().maxMemory());
        gpuPercent = Math.min((int) mc.getGpuUtilization(), 100);
        avgFPS = calculateAverage();
        renderFPSChar(mc, event.getGuiGraphics(), mc.font, event.getWindow().getGuiScale());
    }

    private static void renderFPSChar(Minecraft mc, GuiGraphics graphics, Font font, double scale) {
        if (mc.options.renderDebug || mc.options.renderFpsChart) return; // No render when F3 is open

        final var mode = ChlorideConfig.fpsDisplayMode;
        final var systemMode = ChlorideConfig.fpsDisplaySystemMode;

        if (mode.off() && systemMode.off()) return; // NOTHING TO DO HERE, BACK TO WORK

        DISPLAY.release();

        // FPS
        switch (mode) {
            case SIMPLE -> DISPLAY.append(Tools.colorByLow(fps)).add(fix(fps)).add(" ").add(MSG_FPS.getString()).add(ChatFormatting.RESET);
            case ADVANCED -> {
                DISPLAY.append(Tools.colorByLow(fps)).add(fix(fps)).add(ChatFormatting.RESET);
                DISPLAY.append(Tools.colorByLow(minFPS)).add(MSG_MIN).add(" ").add(fix(minFPS)).add(ChatFormatting.RESET);
                DISPLAY.append(Tools.colorByLow(avgFPS)).add(MSG_AVG).add(" ").add(fix(avgFPS)).add(ChatFormatting.RESET);
            }
        }
        if (!DISPLAY.isEmpty()) DISPLAY.split();

        // GPU AND RAM
        switch (systemMode) {
            case GPU ->
                    DISPLAY.append(Tools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
            case RAM ->
                    DISPLAY.append(Tools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            case ON -> {
                DISPLAY.append(Tools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                DISPLAY.append(Tools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            }
        }

        if (DISPLAY.isEmpty()) DISPLAY.add("FATAL ERROR");

        float marginX = (scale > 0) ? ChlorideConfig.fpsDisplayMargin / (float) scale : ChlorideConfig.fpsDisplayMargin;
        float marginY = (scale > 0) ? ChlorideConfig.fpsDisplayVMargin / (float) scale : ChlorideConfig.fpsDisplayVMargin;

        // Prevent FPS-Display to render outside screenspace
        String displayString = DISPLAY.toString();
        float maxPosX = graphics.guiWidth() - font.width(displayString);
        float maxPosY = graphics.guiHeight() - font.lineHeight;
        float posX, posY;

        posX = switch (ChlorideConfig.fpsDisplayAlign) {
            case LEFT -> marginX;
            case CENTER -> maxPosX / 2;
            case RIGHT -> maxPosX - marginX;
        };
        posY = switch (ChlorideConfig.fpsDisplayVAlign) {
            case TOP -> marginY;
            case CENTER -> maxPosY / 2;
            case BOTTOM -> maxPosY - marginY;
        };

        graphics.pose().pushPose();
        if (ChlorideConfig.fpsDisplayShadow) {
            graphics.fill((int) posX - 2, (int) posY - 2, (int) posX + font.width(displayString) + 2, (int) (posY + font.lineHeight) + 1, -1873784752);
            graphics.flush();
        }

        graphics.drawString(font, displayString, posX, posY, 0xffffffff, true);
        DISPLAY.release();
        graphics.pose().popPose();
    }

    private static String fix(int value) {
        return (value == -1) ? "--" : "" + value;
    }

    private static int minFPS(Minecraft mc) {
        FrameTimer timer = mc.getFrameTimer();

        int start = timer.getLogStart();
        int end = timer.getLogEnd();

        if (end == start) return minFPS;

        int fps = mc.getFps();
        if (fps <= 0) fps = 1;

        long[] frames = timer.getLog();
        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        int index = Math.floorMod(end - 1, frames.length);
        while (index != start && (double) totalNS < 1000000000) {
            long timeNs = frames[index];
            if (timeNs > maxNS) {
                maxNS = timeNs;
            }

            totalNS += timeNs;
            index = Math.floorMod(index - 1, frames.length);
        }

        return (int) (1 / ((double) maxNS / 1000000000));
    }

    private static class FPSDisplayBuilder {
        private StringBuilder builder = new StringBuilder();
        private boolean split = false;
        private boolean divisor = false;

        public FPSDisplayBuilder append(String param) {
            if (split) builder.append(" - ");
            if (divisor) builder.append(" | ");
            builder.append(param);

            split = false;
            divisor = true;
            return this;
        }

        public FPSDisplayBuilder append(ChatFormatting formatting) {
            return append(formatting.toString());
        }

        public FPSDisplayBuilder add(int param) {
            builder.append(param);
            return this;
        }

        public FPSDisplayBuilder add(String param) {
            builder.append(param);
            return this;
        }

        public FPSDisplayBuilder add(Component component) {
            return add(component.getString());
        }

        public FPSDisplayBuilder add(ChatFormatting formatting) {
            return add(formatting.toString());
        }

        public void split() {
            split = true;
            divisor = false;
        }

        public boolean isEmpty() {
            return builder.isEmpty();
        }

        public void release() {
            builder = new StringBuilder();
            split = false;
            divisor = false;
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}