package me.srrapero720.chloride.features.leaves_culling;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.LeavesBlock;

public class LeavesCulling {
    public static boolean fastLeaves() {
        return !SodiumClientMod.options().quality.leavesQuality.isFancy(Minecraft.getInstance().options.graphicsMode().get());
    }

    public static boolean should(LeavesBlock block, ICulleableLeaves casted, LeavesBlock neighbor, ICulleableLeaves castedNeighbor) {
        if (fastLeaves()) return true;
        return switch (ChlorideConfig.leavesCulling) {
            case ALL -> casted.embplus$getResourceLocation().equals(castedNeighbor.embplus$getResourceLocation());
            case OFF -> false;
        };
    }
}
