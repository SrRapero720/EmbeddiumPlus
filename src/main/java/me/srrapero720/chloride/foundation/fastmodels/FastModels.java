package me.srrapero720.chloride.foundation.fastmodels;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.Tools;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Chloride.ID)
public class FastModels {

    public static boolean canUseOnChests() {
        if (Tools.isModInstalled("flywheel")) {
            return false;
        } else {
            return !Tools.isModInstalled("enhancedblockentities");
        }
    }
}
