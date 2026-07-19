package me.srrapero720.chloride;

import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.FastBlocks;
import me.srrapero720.chloride.impl.Overlay;
import me.srrapero720.chloride.impl.Zoom;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
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

        // ZOOM KEYBIND, FPS HUD OVERLAY AND THE FAST-MODEL BUILT-IN PACKS ARE ALL WIRED UP AT INIT
        KeyBindingHelper.registerKeyBinding(Zoom.KEY);
        HudRenderCallback.EVENT.register(Overlay::onRenderOverlay);
        FastBlocks.registerPacks();

        // RECONCILE CHLORIDE CONFIG WITH VANILLA OPTIONS.FULLSCREEN ONCE THE WINDOW EXISTS. THE WINDOW IS BUILT
        // FROM OPTIONS.FULLSCREEN AS ITS INITIAL STATE; IF IT DESYNCS FROM CHLORIDECONFIG.FULLSCREEN (MANUAL EDIT,
        // MOD INSTALLED OVER AN EXISTING options.txt) THE WINDOW STARTS IN THE WRONG MODE.
        ClientLifecycleEvents.CLIENT_STARTED.register(mc -> {
            final boolean optsFullscreen = mc.options.fullscreen().get();
            final boolean configFullscreen = ChlorideConfig.fullscreen.mode != Borderless.Mode.WINDOWED;
            if (optsFullscreen != configFullscreen) {
                Borderless.setFullScreenMode(ChlorideConfig.fullscreen.mode);
            }
        });
    }

    // CALLED FROM MinecraftMixin DURING THE CLIENT CONSTRUCTOR, BEFORE OPTIONS-DEPENDENT FEATURES RUN
    public static void earlyLoad() {
        ChlorideConfig.load(FabricLoader.getInstance().getGameDir().resolve("config"));
    }

    public static boolean installed(final String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    /** Builds a stable, unique option id from a config field name (camelCase -&gt; chloride:snake_case). */
    public static ResourceLocation id(final String field) {
        final String path = field.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    /** Builds a unique option id for a per-registry-entry option (e.g. one toggle per particle type). */
    public static ResourceLocation id(final String prefix, final ResourceLocation key) {
        return ResourceLocation.fromNamespaceAndPath(ID, prefix + "/" + key.getNamespace() + "/" + key.getPath());
    }
}
