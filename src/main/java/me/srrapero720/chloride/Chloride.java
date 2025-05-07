package me.srrapero720.chloride;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(Chloride.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        if (installed("xenon")) throw new RuntimeException("Xenon is incompatible with Chloride, please use Embeddium or Sodium instead");
        if (installed("embeddiumextras")) throw new RuntimeException("Embeddium/Sodium Extras is replaced by cloride, you must remove that mod");
        if (installed("embeddiumplus")) throw new RuntimeException("You have a old-duplicated version of chloride, please remove Embeddium++ (old chloride)");
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