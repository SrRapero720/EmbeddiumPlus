package me.srrapero720.embeddiumplus;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import it.unimi.dsi.fastutil.longs.LongLongMutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EmbyTools {

    public static <T> T getLastValue(T[] value) {
        return value[value.length - 1];
    }

    public static boolean canUseFastChests() {
        try {
            return FlwConfig.get().getBackendType() == BackendType.OFF;
        } catch (Error e) {
            return true; // no flywheel
        }
    }

    private static final LongLongPair[] BENCHTIMES = new LongLongPair[100];
    private static int BENCH_POS = 0;
    public static void benchStart() {
        BENCHTIMES[BENCH_POS] = new LongLongMutablePair(Util.getNanos(), -1);
    }

    public static void benchEnd() {
        BENCHTIMES[BENCH_POS].right(Util.getNanos());
        LOGGER.debug("Current method takes RIGHT NOW {} nanoseconds", BENCHTIMES[BENCH_POS].rightLong() - BENCHTIMES[BENCH_POS].leftLong());

        if (BENCH_POS == BENCHTIMES.length - 1) {
            long totalStart = 0;
            long totalEnd = 0;

            for (int i = 0; i < BENCHTIMES.length; i++) {
                totalStart += BENCHTIMES[i].firstLong();
                totalEnd += BENCHTIMES[i].secondLong();
            }

            LOGGER.info("Current method takes AVG {} nanoseconds", (totalEnd - totalStart) / BENCHTIMES.length);

            BENCH_POS = 0;
        } else {
            BENCH_POS++;
        }
    }

    public static ChatFormatting colorByLow(int usage) {
        return ((usage < 9) ? ChatFormatting.DARK_RED
                : (usage < 16) ? ChatFormatting.RED
                : (usage < 30) ? ChatFormatting.GOLD
                : ChatFormatting.RESET);
    }

    public static ChatFormatting colorByPercent(int usage) {
        return ((usage >= 100) ? ChatFormatting.DARK_RED
                : (usage >= 90) ? ChatFormatting.RED
                : (usage >= 75) ? ChatFormatting.GOLD
                : ChatFormatting.RESET);
    }

    public static String tintByLower(int usage) {
        return ((usage < 9) ? ChatFormatting.DARK_RED
                : (usage < 16) ? ChatFormatting.RED
                : (usage < 30) ? ChatFormatting.GOLD
                : ChatFormatting.RESET).toString() + usage;
    }

    public static String tintByPercent(long usage) {
        return ((usage >= 100) ? ChatFormatting.DARK_RED
                : (usage >= 90) ? ChatFormatting.RED
                : (usage >= 75) ? ChatFormatting.GOLD
                : ChatFormatting.RESET).toString() + usage;
    }

    /**
     * Creates a hexadecimal color based on gave params
     * All values need to be in a range of 0 ~ 255
     * Code copied from WATERMeDIA
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     * @return HEX color
     */
    public static int getColorARGB(int a, int r, int g, int b) { return (a << 24) | (r << 16) | (g << 8) | b; }

    // JAVA USES STUPID AND CONFUSING NAMES
    // max memory is the assigned memory (ej: -Xmx8G)
    // total memory is the allocated memory (normally isn't much)
    // used memory needs to be calculated using total memory - free memory, same with percent
    public static long ramUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static long bytesToMB(long input) {
        return input / 1024 / 1024;
    }

    public static boolean isModInstalled(String modid) { return FMLLoader.getLoadingModList().getModFileById(modid) != null; }

    public static boolean isEntityWithinDistance(Player player, Entity entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.getY() - 4) < maxHeight) {
            double x = player.getX() - entity.getX();
            double z = player.getZ() - entity.getZ();

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(BlockPos bePos, Vec3 camVec, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(bePos.getY() - camVec.y - 4) < maxHeight) {
            double x = bePos.getX() - camVec.x;
            double z = bePos.getZ() - camVec.z;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(Entity entity, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(entity.getY() - cameraY - 4) < maxHeight) {
            double x = entity.getX() - cameraX;
            double z = entity.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }
}