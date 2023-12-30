package me.srrapero720.embeddiumplus;

import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import me.srrapero720.embeddiumplus.internal.EmbyConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EmbeddiumPlus.ID)
public class EmbeddiumPlus {
    public static final String ID = "embeddiumplus";
    public static final Logger LOGGER = LogManager.getLogger();

    public EmbeddiumPlus() {
        if (FMLLoader.getDist().isClient()) initClient();
    }

    public void initClient() {
        DynLightsPlus.init();
        EmbyConfig.load();
    }
}