package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.debugchart.LocalSampleLogger;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Arrays;

@EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT)
public class Overlay {
    private static final FPSDisplayBuilder DISPLAY = new FPSDisplayBuilder();

    private static final Component MSG_FPS = Component.translatable("chloride.interface.fps");
    private static final Component MSG_MIN = Component.translatable("chloride.interface.fps.min");
    private static final Component MSG_AVG = Component.translatable("chloride.interface.fps.avg");
    private static final Component MSG_GPU = Component.translatable("chloride.interface.fps.gpu");
    private static final Component MSG_MEM = Component.translatable("chloride.interface.fps.mem");

    private static int fps = -1;
    private static int minFPS = -1;
    private static int avgFPS = -1;
    private static int gpuPercent = -1;
    private static int memUsage = -1;

    // AVG
    private static final int[] avgCount = new int[24];
    private static boolean avgFilled = false;
    private static int avgIndex = 0;

    public static void pushAvgFps(final int value) {
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
        for (final int i: avgCount) {
            times += i;
        }

        return times / avgCount.length;
    }

    @SubscribeEvent
    public static void onRenderOverlay(final RenderGuiEvent.Pre event) {
        final var mc = Minecraft.getInstance();

        if (mc.getDebugOverlay().showDebugScreen() || mc.gui.hud.isHidden()) return;

        // PRECALCULATE
        fps = mc.getFps();
        minFPS = minFPS(mc);
        memUsage = (int) ((ramUsed() * 100) / Runtime.getRuntime().maxMemory());
        gpuPercent = Math.min((int) mc.getGpuUtilization(), 100);
        avgFPS = calculateAverage();
        renderFPSChar(mc, event.getGuiGraphics(), mc.font, mc.getWindow().getGuiScale());
    }

    private static void renderFPSChar(final Minecraft mc, final GuiGraphicsExtractor graphics, final Font font, final double scale) {
        if (Minecraft.getInstance().getDebugOverlay().showDebugScreen() || Minecraft.getInstance().getDebugOverlay().showProfilerChart()) return; // No render when F3 is open

        final var mode = ChlorideConfig.fpsDisplay.mode;
        final var systemMode = ChlorideConfig.fpsDisplay.systemDetails;

        if (mode.off() && systemMode.off()) return; // NOTHING TO DO HERE, BACK TO WORK

        DISPLAY.release();

        // FPS
        switch (mode) {
            case SIMPLE -> DISPLAY.append(colorByLow(fps)).add(fix(fps)).add(" ").add(MSG_FPS.getString()).add(ChatFormatting.RESET);
            case ADVANCED -> {
                DISPLAY.append(colorByLow(fps)).add(fix(fps)).add(ChatFormatting.RESET);
                DISPLAY.append(colorByLow(minFPS)).add(MSG_MIN).add(" ").add(fix(minFPS)).add(ChatFormatting.RESET);
                DISPLAY.append(colorByLow(avgFPS)).add(MSG_AVG).add(" ").add(fix(avgFPS)).add(ChatFormatting.RESET);
            }
        }
        if (!DISPLAY.isEmpty()) DISPLAY.split();

        // GPU AND RAM
        switch (systemMode) {
            case GPU_ONLY ->
                    DISPLAY.append(colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
            case RAM_ONLY ->
                    DISPLAY.append(colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            case ALL -> {
                DISPLAY.append(colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                DISPLAY.append(colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            }
        }

        if (DISPLAY.isEmpty()) DISPLAY.add("FATAL ERROR");

        final float marginX = (scale > 0) ? ChlorideConfig.fpsDisplay.margin / (float) scale : ChlorideConfig.fpsDisplay.margin;
        final float marginY = (scale > 0) ? ChlorideConfig.fpsDisplay.verticalMargin / (float) scale : ChlorideConfig.fpsDisplay.verticalMargin;

        // Prevent FPS-Display to render outside screenspace
        final String displayString = DISPLAY.toString();
        final float maxPosX = graphics.guiWidth() - font.width(displayString);
        final float maxPosY = graphics.guiHeight() - font.lineHeight;
        final float posX;
        final float posY;

        posX = switch (ChlorideConfig.fpsDisplay.align) {
            case LEFT -> marginX;
            case CENTER -> maxPosX / 2;
            case RIGHT -> maxPosX - marginX;
        };
        posY = switch (ChlorideConfig.fpsDisplay.verticalAlign) {
            case TOP -> marginY;
            case CENTER -> maxPosY / 2;
            case BOTTOM -> maxPosY - marginY;
        };

        graphics.pose().pushMatrix();
        if (ChlorideConfig.fpsDisplay.shadow) {
            graphics.fill((int) posX - 2, (int) posY - 2, (int) posX + font.width(displayString) + 2, (int) (posY + font.lineHeight) + 1, -1873784752);
        }

        graphics.text(font, displayString, (int) posX, (int) posY, 0xffffffff, true);
        DISPLAY.release();
        graphics.pose().popMatrix();
    }

    private static String fix(final int value) {
        return (value == -1) ? "--" : "" + value;
    }

    private static int minFPS(final Minecraft mc) {

        final LocalSampleLogger timer = mc.getDebugOverlay().frameTimeLogger;
        final int size = timer.size();

        if (size == 0) return minFPS;

        int fps = mc.getFps();
        if (fps <= 0) fps = 1;

        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        for (int i = size - 1; i >= 0 && totalNS < 1_000_000_000L; i--) {
            final long timeNs = timer.get(i, 0); // dim 0 = tiempo total del frame
            if (timeNs > maxNS) maxNS = timeNs;
            totalNS += timeNs;
        }

        return (int) (1 / ((double) maxNS / 1000000000));
    }

    public static ChatFormatting colorByLow(final int usage) {
        return ((usage < 9) ? ChatFormatting.DARK_RED
                : (usage < 16) ? ChatFormatting.RED
                : (usage < 30) ? ChatFormatting.GOLD
                : ChatFormatting.RESET);
    }

    public static ChatFormatting colorByPercent(final int usage) {
        return ((usage >= 100) ? ChatFormatting.DARK_RED
                : (usage >= 90) ? ChatFormatting.RED
                : (usage >= 75) ? ChatFormatting.GOLD
                : ChatFormatting.RESET);
    }

    // JAVA USES STUPID AND CONFUSING NAMES
    // max memory is the assigned memory (ej: -Xmx8G)
    // total memory is the allocated memory (normally isn't much)
    // used memory needs to be calculated using total memory - free memory, same with percent
    public static long ramUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static long bytesToMB(final long input) {
        return input / 1024 / 1024;
    }

    /* CONFIG VALUES */
    public enum FPS {
        OFF, SIMPLE, ADVANCED;

        public boolean off() {
            return this == OFF;
        }
    }

    public enum FPSAlign { LEFT, CENTER, RIGHT}

    public enum FPSVAlign { TOP, CENTER, BOTTOM}

    public enum FPSDetails {
        OFF, ALL, GPU_ONLY, RAM_ONLY;

        public boolean ram() { return this == RAM_ONLY || this == ALL; }
        public boolean gpu() { return this == GPU_ONLY || this == ALL; }
        public boolean off() { return this == OFF; }
    }

    private static class FPSDisplayBuilder {
        private StringBuilder builder = new StringBuilder();
        private boolean split = false;
        private boolean divisor = false;

        public FPSDisplayBuilder append(final String param) {
            if (this.split) this.builder.append(" - ");
            if (this.divisor) this.builder.append(" | ");
            this.builder.append(param);

            this.split = false;
            this.divisor = true;
            return this;
        }

        public FPSDisplayBuilder append(final ChatFormatting formatting) {
            return this.append(formatting.toString());
        }

        public FPSDisplayBuilder add(final int param) {
            this.builder.append(param);
            return this;
        }

        public FPSDisplayBuilder add(final String param) {
            this.builder.append(param);
            return this;
        }

        public FPSDisplayBuilder add(final Component component) {
            return this.add(component.getString());
        }

        public FPSDisplayBuilder add(final ChatFormatting formatting) {
            return this.add(formatting.toString());
        }

        public void split() {
            this.split = true;
            this.divisor = false;
        }

        public boolean isEmpty() {
            return this.builder.isEmpty();
        }

        public void release() {
            this.builder = new StringBuilder();
            this.split = false;
            this.divisor = false;
        }

        @Override
        public String toString() {
            return this.builder.toString();
        }
    }
}