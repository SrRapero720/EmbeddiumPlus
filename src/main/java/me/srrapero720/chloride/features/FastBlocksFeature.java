package me.srrapero720.chloride.features;

import me.srrapero720.chloride.Tools;

public class FastBlocksFeature {

    public static boolean canUseOnChests() {
        if (Tools.isModInstalled("flywheel")) {
            return false;
        } else {
            return !Tools.isModInstalled("enhancedblockentities");
        }
    }
}
