package me.srrapero720.chloride;

import me.srrapero720.chloride.features.ZoomFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        if (FMLLoader.getDist().isClient()) {
            LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");
        } else {
            LOGGER.info(IT, "Chloride is not intended to be on servers, loaded in inner mode");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void earlyLoad() {
        ChlorideConfig.load(FMLLoader.getGamePath().resolve("config"));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void load(final FMLClientSetupEvent event) {
        if (Tools.isModInstalled("xenon")) throw new RuntimeException("Xenon is incompatible with Chloride, please use Embeddium or Sodium instead");
        if (Tools.isModInstalled("embeddiumextras")) throw new RuntimeException("Embeddium/Sodium Extras is incompatible with Chloride, chloride replaces it");
        if (Tools.isModInstalled("embeddiumplus")) throw new RuntimeException("You have a old-duplicated version of chloride, please remove Embeddium++ (old chloride)");
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerKeys(final RegisterKeyMappingsEvent event) {
        event.register(ZoomFeature.KEY);
    }
}