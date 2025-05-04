package me.srrapero720.chloride.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityCulling {
    public static boolean isWhitelisted(final ResourceLocation entityOrTile, final List<ResourceLocation> configValue) {
        for (final ResourceLocation item: configValue) {
            if (entityOrTile.equals(item)) return true;

            // Wildcard check
            if (entityOrTile.getNamespace().equals(item.getNamespace()) && entityOrTile.getPath().equals("all"))
                return true;
        }
        return false;
    }

    public static boolean isEntityInRange(final BlockPos pos, final Vec3 cam, final int maxHeight, final int maxDistanceSqr) {
        return isEntityInRange(pos.getCenter(), cam, maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(final Entity entity, final double camX, final double camY, final double camZ, final int maxHeight, final int maxDistanceSqr) {
        return isEntityInRange(entity.position(), new Vec3(camX, camY, camZ), maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(final Vec3 position, final Vec3 camera, final int maxHeight, final int maxDistanceSqr) {
        if (Math.abs(position.y - camera.y - 4) < maxHeight) {
            final double x = position.x - camera.x;
            final double z = position.z - camera.z;

            return x * x + z * z < maxDistanceSqr;
        }

        return false;
    }
}
