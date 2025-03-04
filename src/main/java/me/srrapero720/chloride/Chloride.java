package me.srrapero720.chloride;

import me.srrapero720.chloride.foundation.zoom.ZoomFeature;
import me.srrapero720.chloride.util.MockerInstalledException;
import me.srrapero720.chloride.util.XenonInstalledException;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(Chloride.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Chloride {
    public static final String ID = "chloride";
    public static final Logger LOGGER = LogManager.getLogger("chloride");
    public static final Marker IT = MarkerManager.getMarker("Main");

    public Chloride() {
        LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");
    }

    public static void earlyLoad() {
        ChlorideConfig.load(FMLLoader.getGamePath().resolve("config"));
    }

    @SubscribeEvent
    public void load(FMLClientSetupEvent event) {
        if (Tools.isModInstalled("xenon")) throw new XenonInstalledException();
        if (Tools.isModInstalled("embeddiumextras")) throw new MockerInstalledException();
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(ZoomFeature.KEY);
    }
}