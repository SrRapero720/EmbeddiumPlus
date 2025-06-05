package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

public class EntityCulling {
    public static final boolean VS_I = Chloride.installed("valkyrienskies");

    public static boolean isWhitelisted(final ResourceLocation entityOrTile, final List<ResourceLocation> configValue) {
        for (final ResourceLocation item: configValue) {
            if (entityOrTile.equals(item)) return true;

            // Wildcard check
            if (entityOrTile.getNamespace().equals(item.getNamespace()) && item.getPath().equals("all"))
                return true;
        }
        return false;
    }

    public static boolean isEntityInRange(final BlockEntity tile, final Vec3 cam, final int maxHeight, final int maxDistanceSqr) {
        return isEntityInRange(tile.getLevel(), tile.getBlockPos().getCenter(), cam, maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(final Entity entity, final double camX, final double camY, final double camZ, final int maxHeight, final int maxDistanceSqr) {
        return isEntityInRange(entity.level(), entity.position(), new Vec3(camX, camY, camZ), maxHeight, maxDistanceSqr);
    }

    private static boolean isEntityInRange(Level level, final Vec3 position, final Vec3 camera, final int maxHeight, final int maxDistanceSquared) {
//        if (VS_I) {
//            return VSGameUtilsKt.squaredDistanceBetweenInclShips(level, position.x, position.y, position.z, camera.x, camera.y, camera.z) < maxDistanceSquared;
//        }
        if (Math.abs(position.y - camera.y - 4) < maxHeight) {
            final double x = position.x - camera.x;
            final double z = position.z - camera.z;

            return x * x + z * z < maxDistanceSquared;
        }

        return false;
    }
}
