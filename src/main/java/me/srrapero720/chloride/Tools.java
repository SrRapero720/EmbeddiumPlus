package me.srrapero720.chloride;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.List;

public class Tools {
    private static final Marker IT = MarkerManager.getMarker("Tools");

    public static List<ResourceLocation> toId(String... ids) {
        List<ResourceLocation> result = new ArrayList<>();
        for (String id: ids) {
            if (id.endsWith(":*")) id = id.replace(":*", ":all");
            result.add(ResourceLocation.tryParse(id));
        }

        return result;
    }

    public static Pair<String, String> resourceLocationPair(String res) {
        String[] r = res.split(":");
        try {
            return Pair.of(r[0], r[1]);
        } finally {
            r[0] = null;
            r[1] = null;
            r = null;
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

    public static boolean isWhitelisted(ResourceLocation entityOrTile, List<ResourceLocation> configValue) {
        for (final ResourceLocation item: configValue) {
            if (entityOrTile.equals(item)) return true;

            // Wildcard check
            if (entityOrTile.getNamespace().equals(item.getNamespace()) && entityOrTile.getPath().equals("all"))
                return true;
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

    public static boolean isEntityInRange(BlockPos pos, Vec3 cam, int maxHeight, int maxDistanceSqr) {
        return isEntityInRange(pos.getCenter(), cam, maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(Entity entity, double camX, double camY, double camZ, int maxHeight, int maxDistanceSqr) {
        return isEntityInRange(entity.position(), new Vec3(camX, camY, camZ), maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(Vec3 position, Vec3 camera, int maxHeight, int maxDistanceSqr) {
        if (Math.abs(position.y - camera.y - 4) < maxHeight) {
            double x = position.x - camera.x;
            double z = position.z - camera.z;

            return x * x + z * z < maxDistanceSqr;
        }

        return false;
    }
}