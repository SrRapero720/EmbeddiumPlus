package me.srrapero720.chloride;

import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.FastBlocks;
import me.srrapero720.chloride.impl.Overlay;
import me.srrapero720.chloride.impl.Zoom;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Chloride implements ClientModInitializer {
    public static final String ID = "chloride";
    public static final Logger LOGGER = LogManager.getLogger("chloride");
    public static final Marker IT = MarkerManager.getMarker("Main");

    @Override
    public void onInitializeClient() {
        LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");

        KeyMappingHelper.registerKeyMapping(Zoom.KEY);
        HudElementRegistry.addLast(id("fps"), Overlay::onRenderOverlay);
        FastBlocks.registerPacks();

        ClientLifecycleEvents.CLIENT_STARTED.register(mc -> {
            final boolean optsFullscreen = mc.options.fullscreen().get();
            final boolean configFullscreen = ChlorideConfig.fullscreen.mode != Borderless.Mode.WINDOWED;
            if (optsFullscreen != configFullscreen) {
                Borderless.setFullScreenMode(ChlorideConfig.fullscreen.mode);
            }
        });
    }

    public static void earlyLoad() {
        ChlorideConfig.load(FabricLoader.getInstance().getGameDir().resolve("config"));
    }

    public static boolean installed(final String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
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