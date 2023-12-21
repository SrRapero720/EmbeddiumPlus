package me.srrapero720.embeddiumplus;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;

public class EmbPlusTools {
    public static <T> T getLastValue(T[] value) {
        return value[value.length - 1];
    }

    public static boolean flwIsOff() {
        try {
            return FlwConfig.get().getBackendType() == BackendType.OFF;
        } catch (Error e) {
            return true; // no flywheel
        }
    }

    public static boolean isNearPlayer(Level world, BlockPos pos, int maxHeight, int maxDistanceSquare) {
        return isNearPlayerInternal(world, pos.getX(), pos.getY(), pos.getZ(), maxHeight, maxDistanceSquare, false);
    }

    private static boolean isNearPlayerInternal(Level world, double posx, double posy, double posz, int maxHeight, int maxDistanceSquare, boolean allowNullPlayers) {
        List<? extends Player> closest = world.players();

        for (Player player : closest) {
            if (player == null)
                return allowNullPlayers;

            if (Math.abs(player.getY() - posy) < maxHeight) {
                double x = player.getX() - posx;
                double z = player.getZ() - posz;

                boolean nearPlayer = x * x + z * z < maxDistanceSquare;

                if (nearPlayer)
                    return true;
            }
        }

        return false;
    }


    public static boolean isEntityWithinDistance(Entity player, Entity entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.getY() - 4) < maxHeight) {
            double x = player.getX() - entity.getX();
            double z = player.getZ() - entity.getZ();

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(BlockPos player, Vec3 entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.y - 4) < maxHeight) {
            double x = player.getX() - entity.x;
            double z = player.getZ() - entity.z;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(Entity player, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - cameraY - 4) < maxHeight) {
            double x = player.getX() - cameraX;
            double z = player.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isPresent(String modid) { return FMLLoader.getLoadingModList().getModFileById(modid) != null; }
}