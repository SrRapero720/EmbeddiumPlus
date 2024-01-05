package me.srrapero720.embeddiumplus;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;

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

    public static String tintByPercent(double usage) {
        return ((usage >= 100) ? ChatFormatting.DARK_RED
                : (usage >= 90) ? ChatFormatting.RED
                : (usage >= 75) ? ChatFormatting.GOLD
                : ChatFormatting.RESET).toString() + usage;
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