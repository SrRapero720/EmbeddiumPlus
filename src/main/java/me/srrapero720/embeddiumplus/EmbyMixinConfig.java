package me.srrapero720.embeddiumplus;

import net.minecraftforge.fml.loading.FMLPaths;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EmbyMixinConfig {
    public static boolean isLoaded() {
        return false;
    }

    public static void load() {
        if (isLoaded()) return;

        EmbeddiumPlus.LOGGER.warn("Embeddium++Mixin.toml was obsolete, removing it");

        var file = FMLPaths.CONFIGDIR.get().resolve("embeddium++mixin.toml").toFile();
        if (file.exists()) {
            if (file.delete()) {
                LOGGER.info("Deleted old embeddium++mixin.toml file");
            } else {
                LOGGER.warn("Cannot delete embeddium++mixin.toml file");
            }
        }
    }
}
