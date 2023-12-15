package me.srrapero720.embeddiumplus;

import me.srrapero720.dynamiclights.LambDynLights;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EmbeddiumPlus.ID)
public class EmbeddiumPlus {
    public static final String ID = "embeddiumplus";
    public static final Logger LOGGER = LogManager.getLogger();

    public EmbeddiumPlus() {
        MinecraftForge.EVENT_BUS.register(this);
        EmbPlusConfig.loadConfig();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> LambDynLights::init);
//        EmbeddiumPlusConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("embeddium++.toml"));
    }
}