package me.srrapero720.embeddiumplus;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.longs.LongLongMutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EmbyTools {
    private static final Marker IT = MarkerManager.getMarker("Tools");

    public static <T> T getLastValue(T[] value) {
        return value[value.length - 1];
    }

    public static Pair<String, String> resourceLocationPair(String res) {
        String[] r = res.split(":");
        try {
            return Pair.of(r[0], r[1]);
        } finally {
            r[0] = null;
            r[1] = null;
        }
    }

    public static void setValueInField(Class<?> clazz, String fieldName, Object from, Object value) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            field.set(from, value);
        } catch (Exception e){
            LOGGER.error("Cannot set value of '{}' from {}", fieldName, clazz.getName(), e);
        }
    }

    public static void invokeMethod(Class<?> clazz, String methodName, Object from, Object... values) {
        try {
            Class<?>[] types = new Class[values.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = values[i].getClass();
            }

            Method field = clazz.getMethod(methodName, types);
            field.setAccessible(true);
            field.invoke(from, values);
        } catch (Exception e){
            LOGGER.error("Cannot invoke method '{}' from {}", methodName, clazz.getName(), e);
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

    public static boolean isWhitelisted(ResourceLocation entityOrTile, ForgeConfigSpec.ConfigValue<List<? extends String>> configValue) {
        for (final String item: configValue.get()) {
            final var resLoc = resourceLocationPair(item);
            if (!resLoc.key().equals(entityOrTile.getNamespace())) continue;

            // Wildcard check
            if (resLoc.value().equals("*") || resLoc.value().equals(entityOrTile.getPath())) {
                return true;
            }
        }
        return false;
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
}