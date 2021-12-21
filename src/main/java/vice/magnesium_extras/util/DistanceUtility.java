package vice.magnesium_extras.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class DistanceUtility
{
    public static boolean isNearPlayer(World world, BlockPos pos, int maxHeight, int maxDistanceSquare)
    {
        return isNearPlayerInternal(world, pos.getX(), pos.getY(), pos.getZ(), maxHeight, maxDistanceSquare, false);
    }

    private static boolean isNearPlayerInternal(World world, double posx, double posy, double posz, int maxHeight, int maxDistanceSquare, boolean allowNullPlayers)
    {
        List<? extends PlayerEntity> closest = world.players();

        for (PlayerEntity player : closest)
        {
            if (player == null)
                return allowNullPlayers;

            if (Math.abs(player.getY() - posy) < maxHeight)
            {
                double x = player.getX() - posx;
                double z = player.getZ() - posz;

                boolean nearPlayer = x * x + z * z < maxDistanceSquare;

                if (nearPlayer)
                    return true;
            }
        }

        return false;

    }


    public static boolean isEntityWithinDistance(Entity player, Entity entity, int maxHeight, int maxDistanceSquare)
    {
        if (Math.abs(player.getY() - entity.getY() - 4) < maxHeight)
        {
            double x = player.getX() - entity.getX();
            double z = player.getZ() - entity.getZ();

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(BlockPos player, Vector3d entity, int maxHeight, int maxDistanceSquare)
    {
        if (Math.abs(player.getY() - entity.y - 4) < maxHeight)
        {
            double x = player.getX() - entity.x;
            double z = player.getZ() - entity.z;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(Entity player, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare)
    {
        if (Math.abs(player.getY() - cameraY - 4) < maxHeight)
        {
            double x = player.getX() - cameraX;
            double z = player.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

}
