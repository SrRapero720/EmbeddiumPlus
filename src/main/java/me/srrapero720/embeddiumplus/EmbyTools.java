package me.srrapero720.embeddiumplus;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;

public class EmbyTools {
    public static <T> T getLastValue(T[] value) {
        return value[value.length - 1];
    }

    public static boolean isFlywheelOff() {
        try {
            return FlwConfig.get().getBackendType() == BackendType.OFF;
        } catch (Error e) {
            return true; // no flywheel
        }
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