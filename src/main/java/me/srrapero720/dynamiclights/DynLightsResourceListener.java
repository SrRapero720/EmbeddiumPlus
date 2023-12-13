package me.srrapero720.dynamiclights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.srrapero720.dynamiclights.api.item.ItemLightSources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class DynLightsResourceListener implements ResourceManagerReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        LOGGER.warn("Reloading Dynamic lights");
        ItemLightSources.load(manager);
    }
}

