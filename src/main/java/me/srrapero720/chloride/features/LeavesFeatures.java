package me.srrapero720.chloride.features;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.features.accessors.IGameLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.LeavesBlock;

public class LeavesFeatures {
    public static boolean fastLeaves() {
        return !SodiumClientMod.options().quality.leavesQuality.isFancy(Minecraft.getInstance().options.graphicsMode().get());
    }

    public static boolean should(final LeavesBlock block, final IGameLeaves casted, final LeavesBlock neighbor, final IGameLeaves castedNeighbor) {
        if (fastLeaves()) return true;
        return switch (ChlorideConfig.leavesCulling) {
            case ALL -> casted.chloride$getRL().equals(castedNeighbor.chloride$getRL());
            case OFF -> false;
        };
    }
}
