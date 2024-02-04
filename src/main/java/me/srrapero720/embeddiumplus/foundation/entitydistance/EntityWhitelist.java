package me.srrapero720.embeddiumplus.foundation.entitydistance;

import it.unimi.dsi.fastutil.Pair;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.List;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EntityWhitelist {
    private static final Marker e$IT = MarkerManager.getMarker("EntityWhitelist");
    static {
        EmbyConfig.load();
    }

    public static boolean isAllowed(ResourceLocation entityOrTile, ForgeConfigSpec.ConfigValue<List<? extends String>> configValue) {
        for (final String item: configValue.get()) {
            final var resLoc = EmbyTools.resourceLocationPair(item);
            if (!resLoc.key().equals(entityOrTile.getNamespace())) continue;

            // Wildcard check
            if (resLoc.value().equals("*") || resLoc.value().equals(entityOrTile.getPath())) {
                return true;
            }
        }
        LOGGER.debug(e$IT,"Whitelist checked for {}", entityOrTile);
        return false;
    }
}
