package me.srrapero720.embeddium_extras;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import me.srrapero720.embeddium_extras.config.EmbeddiumExtrasConfig;

@Mod(EmbeddiumExtras.ID)
public class EmbeddiumExtras {
    public static final String ID = "embeddium_extras";
    public static final Logger LOGGER = LogManager.getLogger();

    public EmbeddiumExtras() {
        MinecraftForge.EVENT_BUS.register(this);
        EmbeddiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("embeddium_extras.toml"));
        //MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}