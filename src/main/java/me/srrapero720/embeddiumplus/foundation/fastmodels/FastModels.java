package me.srrapero720.embeddiumplus.foundation.fastmodels;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = EmbeddiumPlus.ID)
public class FastModels {

    public static boolean canUseOnChests() {
        if (EmbyTools.isModInstalled("flywheel")) {
            return FlwConfig.get().getBackendType() == BackendType.OFF;
        } else {
            return !EmbyTools.isModInstalled("enhancedblockentities");
        }
    }
}
