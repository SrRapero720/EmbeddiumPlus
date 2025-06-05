package me.srrapero720.chloride;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(Chloride.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Chloride {
    public static final String ID = "chloride";
    public static final Logger LOGGER = LogManager.getLogger("chloride");
    public static final Marker IT = MarkerManager.getMarker("Main");

    public Chloride() {
        if (FMLLoader.getDist().isClient()) {
            LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");
        } else {
            LOGGER.info(IT, "Chloride is not intended to be on servers, loaded in inner mode");
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void load(final FMLClientSetupEvent event) {
        LOGGER.info("LOADED CHLORIDE");
    }

    @OnlyIn(Dist.CLIENT)
    public static void earlyLoad() {
        ChlorideConfig.load(FMLLoader.getGamePath().resolve("config"));
    }

    public static boolean installed(final String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }
}