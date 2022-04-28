package vice.rubidium_extras;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;

@Mod("rubidium_extras")
public class MagnesiumExtras
{
    public static final String MODID = "rubidium_extras";
    public static final Logger LOGGER = LogManager.getLogger();

    public MagnesiumExtras() {
        MinecraftForge.EVENT_BUS.register(this);

        MagnesiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("rubidium_extras.toml"));

        //MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get()
                .registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));


    }


}