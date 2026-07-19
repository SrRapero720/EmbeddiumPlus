package me.srrapero720.chloride.impl;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.IGameLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.LeavesBlock;

public class LeavesCulling {
    public static boolean fastLeaves() {
        return !SodiumClientMod.options().quality.leavesQuality.isFancy(Minecraft.getInstance().options.graphicsMode().get());
    }

    public static boolean should(final LeavesBlock block, final IGameLeaves casted, final LeavesBlock neighbor, final IGameLeaves castedNeighbor) {
        if (fastLeaves()) return true;
        return switch (ChlorideConfig.world.leavesCulling) {
            case ALL -> casted.chloride$getRL().equals(castedNeighbor.chloride$getRL());
            case OFF -> false;
        };
    }

    public enum LeavesCullingMode {
        ALL, OFF // MORE, HALF, LESS
    }
}
