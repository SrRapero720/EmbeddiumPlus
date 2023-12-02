package me.disabled720.dynamiclights;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.disabled720.dynamiclights.api.item.ItemLightSources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class DynLightsResourceListener implements ResourceManagerReloadListener
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create();


    @Override
    public void onResourceManagerReload(ResourceManager manager)
    {
        System.out.println("Reloading Dynamic Lights");

        ItemLightSources.load(manager);
    }
}

