package me.srrapero720.embeddiumplus.foundation.fastmodels;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = EmbeddiumPlus.ID)
public class ChestHandler {

    public static boolean canEnable() {
        try {
            return FlwConfig.get().getBackendType() == BackendType.OFF;
        } catch (Error e) { // NO FLYWHEEL
            // EBE conflicts with this.
            return !EmbyTools.isModInstalled("enhancedblockentities");
        }
    }
}
