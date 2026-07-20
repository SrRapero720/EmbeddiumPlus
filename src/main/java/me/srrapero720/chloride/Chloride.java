package me.srrapero720.chloride;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
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
@EventBusSubscriber(value = Dist.CLIENT)
public class Chloride {
    public static final String ID = "chloride";
    public static final Logger LOGGER = LogManager.getLogger("chloride");
    public static final Marker IT = MarkerManager.getMarker("Main");

    public Chloride() {
        if (FMLLoader.getCurrent().getDist().isClient()) {
            LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");
        } else {
            LOGGER.info(IT, "Chloride is not intended to be on servers, loaded in inner mode");
        }
    }

    @SubscribeEvent
    public static void load(final FMLClientSetupEvent event) {
        LOGGER.info("LOADED CHLORIDE");
    }

    public static void earlyLoad() {
        ChlorideConfig.load(FMLLoader.getCurrent().getGameDir().resolve("config"));
    }

    public static boolean installed(final String modid) {
        return FMLLoader.getCurrent().getLoadingModList().getModFileById(modid) != null;
    }

    /** Builds a stable, unique option id from a config field name (camelCase -&gt; chloride:snake_case). */
    public static Identifier id(final String field) {
        final String path = field.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
        return Identifier.fromNamespaceAndPath(ID, path);
    }

    /** Builds a unique option id for a per-registry-entry option (e.g. one toggle per particle type). */
    public static Identifier id(final String prefix, final Identifier key) {
        return Identifier.fromNamespaceAndPath(ID, prefix + "/" + key.getNamespace() + "/" + key.getPath());
    }
}