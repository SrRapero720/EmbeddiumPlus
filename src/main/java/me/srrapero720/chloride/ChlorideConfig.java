package me.srrapero720.chloride;

import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.chloride.impl.*;
import me.srrapero720.waterconfig.ConfigSpec;
import me.srrapero720.waterconfig.WaterConfig;
import me.srrapero720.waterconfig.api.annotations.Comment;
import me.srrapero720.waterconfig.api.annotations.NumberConditions;
import me.srrapero720.waterconfig.api.annotations.Spec;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.chloride.Chloride.LOGGER;

@Spec(value = Chloride.ID, suffix = "client", format = WaterConfig.FORMAT_TOML, old = WaterConfig.FORMAT_JSON)
@Comment("Chloride client settings, organized by feature")
public class ChlorideConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");
    private static volatile ConfigSpec SPEC;

    // GENERAL (ROOT)
    @Spec.Field
    @Comment("Hides non-performance pages and options from the Sodium settings screen (for modpack makers)")
    public static boolean modpackMode = false;

    // FEATURE GROUPS
    @Spec.Field public static final FullscreenConfig fullscreen = new FullscreenConfig();
    @Spec.Field public static final FpsDisplayConfig fpsDisplay = new FpsDisplayConfig();
    @Spec.Field public static final FogConfig fog = new FogConfig();
    @Spec.Field public static final WorldConfig world = new WorldConfig();
    @Spec.Field public static final NametagsConfig nametags = new NametagsConfig();
    @Spec.Field public static final DarknessConfig darkness = new DarknessConfig();
    @Spec.Field public static final InterfaceConfig ui = new InterfaceConfig();
    @Spec.Field public static final FastBlocksConfig fastBlocks = new FastBlocksConfig();
    @Spec.Field public static final ParticlesConfig particles = new ParticlesConfig();
    @Spec.Field public static final CullingConfig culling = new CullingConfig();
    @Spec.Field public static final ZoomConfig zoom = new ZoomConfig();

    @Spec(value = "fullscreen", disableStatic = true)
    @Comment("Fullscreen and borderless window behavior")
    public static class FullscreenConfig {
        @Spec.Field
        @Comment("Window mode applied at boot and by the fullscreen option")
        public Borderless.Mode mode = Borderless.Mode.WINDOWED;

        @Spec.Field
        @Comment("Adds 1px to the borderless window height, avoiding driver-level exclusive fullscreen optimizations")
        public boolean disableBorderlessOptimizations = false;

        @Spec.Field
        @Comment("Which window modes the F11 key cycles through")
        public Borderless.AttachMode attachModeF11 = Borderless.AttachMode.ATTACH;
    }

    @Spec(value = "fpsDisplay", disableStatic = true)
    @Comment("FPS and system details overlay")
    public static class FpsDisplayConfig {
        @Spec.Field public Overlay.FPS mode = Overlay.FPS.ADVANCED;
        @Spec.Field public Overlay.FPSAlign align = Overlay.FPSAlign.LEFT;
        @Spec.Field public Overlay.FPSVAlign verticalAlign = Overlay.FPSVAlign.TOP;
        @Spec.Field public Overlay.FPSDetails systemDetails = Overlay.FPSDetails.OFF;
        @Spec.Field @NumberConditions(minInt = 0) public int margin = 12;
        @Spec.Field @NumberConditions(minInt = 0) public int verticalMargin = 12;
        @Spec.Field public boolean shadow = false;
    }

    @Spec(value = "fog", disableStatic = true)
    @Comment("Fog rendering and the sky blue band")
    public static class FogConfig {
        @Spec.Field public boolean enabled = true;
        @Spec.Field public boolean onOverworld = true;
        @Spec.Field public boolean onNether = true;
        @Spec.Field public boolean onEnd = true;
        @Spec.Field public boolean blueBand = true;

        @Spec.Field
        @Comment("Overrides vanilla fog with the start/end/shape values below")
        public boolean custom = false;

        @Spec.Field @NumberConditions(minInt = -1000, maxInt = 1000) public int start = 0;
        @Spec.Field @NumberConditions(minInt = 100, maxInt = 10000) public int end = 192;
        @Spec.Field public FogShape shape = FogShape.CYLINDER;
    }

    @Spec(value = "world", disableStatic = true)
    @Comment("World rendering tweaks")
    public static class WorldConfig {
        @Spec.Field @NumberConditions(minInt = 64, maxInt = 364) public int cloudsHeight = 192;
        @Spec.Field public ChunkFade.Speed chunkFadeSpeed = ChunkFade.Speed.SLOW;
        @Spec.Field public LeavesCulling.LeavesCullingMode leavesCulling = LeavesCulling.LeavesCullingMode.OFF;

        @Spec.Field
        @Comment("Y level where the dark below-horizon void plane starts rendering; 63 keeps vanilla behavior, lower it to remove the dark sky band at low heights")
        @NumberConditions(minInt = -64, maxInt = 256)
        public int lowerVoidHorizon = 63;

        @Spec.Field
        @Comment("Keeps the skybox from clipping on very low render distances by enforcing a minimum sky far-plane")
        public boolean farSkybox = true;
    }

    @Spec(value = "nametags", disableStatic = true)
    @Comment("Nametag rendering toggles")
    public static class NametagsConfig {
        @Spec.Field public boolean entities = true;
        @Spec.Field public boolean players = true;
        @Spec.Field public boolean items = true;
    }

    @Spec(value = "darkness", disableStatic = true)
    @Comment("True darkness feature")
    public static class DarknessConfig {
        @Spec.Field public Darkness.DarkMode mode = Darkness.DarkMode.VANILLA;
        @Spec.Field public boolean onOverworld = true;
        @Spec.Field public boolean onNether = false;
        @Spec.Field @NumberConditions(minDouble = 0, maxDouble = 1) public double netherFogBright = 0.5;
        @Spec.Field public boolean onEnd = false;
        @Spec.Field @NumberConditions(minDouble = 0, maxDouble = 1) public double endFogBright = 0.5;

        @Spec.Field
        @Comment("Apply darkness on modded dimensions not present in the whitelist below")
        public boolean byDefault = false;

        @Spec.Field public List<ResourceLocation> dimensionWhitelist = toId();
        @Spec.Field public boolean onNoSkyLight = false;
        @Spec.Field public boolean blockLightOnly = false;
        @Spec.Field public boolean affectedByMoonPhase = true;
        @Spec.Field @NumberConditions(minDouble = 0, maxDouble = 1) public double newMoonBright = 0.0;
        @Spec.Field @NumberConditions(minDouble = 0, maxDouble = 1) public double fullMoonBright = 0.25;
    }

    @Spec(value = "interface", disableStatic = true)
    @Comment("Interface and HUD tweaks")
    public static class InterfaceConfig {
        @Spec.Field
        @Comment("Hides the JEI/REI/EMI ingredient overlay until the search field is focused")
        public boolean hideJREMI = false;

        @Spec.Field public boolean hideJREMIHint = false;
        @Spec.Field public boolean fontShadows = true;
        @Spec.Field public boolean fastLanguageReload = true;

        @Spec.Field
        @Comment("Settings screen style; non-sodium styles are WIP and only open on dev environments")
        public SettingsScreens.Style settingsScreen = SettingsScreens.Style.SODIUM;
    }

    @Spec(value = "fastBlocks", disableStatic = true)
    @Comment("Renders chests and beds as simple static block models")
    public static class FastBlocksConfig {
        @Spec.Field public boolean chests = false;
        @Spec.Field public boolean beds = false;
    }

    @Spec(value = "particles", disableStatic = true)
    @Comment("Particle toggles")
    public static class ParticlesConfig {
        @Spec.Field public boolean rain = true;
        @Spec.Field public boolean rainDrops = true;
        @Spec.Field public boolean blockCracking = true;
        @Spec.Field public boolean blockDestroyed = true;

        @Spec.Field
        @Comment("Particle ids fully disabled; 'modid:all' disables a whole namespace")
        public List<ResourceLocation> disabled = toId();
    }

    @Spec(value = "culling", disableStatic = true)
    @Comment("Entity and tile-entity distance culling")
    public static class CullingConfig {
        @Spec.Field public boolean tileEntities = true;

        @Spec.Field
        @Comment("Squared horizontal distance in blocks")
        @NumberConditions(minInt = 0, maxInt = 16384)
        public int tileEntityDistanceX = 4096;

        @Spec.Field @NumberConditions(minInt = 0, maxInt = 256) public int tileEntityDistanceY = 32;
        @Spec.Field public boolean entities = true;

        @Spec.Field
        @Comment("Maximum rendered entities; 512 disables the limit")
        @NumberConditions(minInt = 0, maxInt = 512)
        public int entityLimit = 512;

        @Spec.Field
        @Comment("Squared horizontal distance in blocks")
        @NumberConditions(minInt = 0, maxInt = 16384)
        public int entityDistanceX = 4096;

        @Spec.Field @NumberConditions(minInt = 0, maxInt = 256) public int entityDistanceY = 32;
        @Spec.Field public boolean monsters = false;

        @Spec.Field
        @Comment("Squared horizontal distance in blocks")
        @NumberConditions(minInt = 0, maxInt = 16384)
        public int monsterDistanceX = 16384;

        @Spec.Field @NumberConditions(minInt = 0, maxInt = 256) public int monsterDistanceY = 64;

        @Spec.Field
        @Comment("Entities never culled; 'modid:all' matches a whole namespace")
        public List<ResourceLocation> entityWhitelist = toId("minecraft:ghast", "minecraft:ender_dragon", "iceandfire:all", "create:all");

        @Spec.Field public List<ResourceLocation> monsterWhitelist = toId();
        @Spec.Field public List<ResourceLocation> tileEntityWhitelist = toId("waterframes:all");
    }

    @Spec(value = "zoom", disableStatic = true)
    @Comment("Zoom keybind")
    public static class ZoomConfig {
        @Spec.Field public boolean enabled = true;
        @Spec.Field @NumberConditions(minDouble = 1, maxDouble = 100) public double max = 50;
    }

    public static List<ResourceLocation> toId(final String... ids) {
        final List<ResourceLocation> result = new ArrayList<>();
        for (String id: ids) {
            if (id.endsWith(":*")) id = id.replace(":*", ":all");
            result.add(ResourceLocation.tryParse(id));
        }

        return result;
    }

    static void load(final Path configPath) {
        try {
            WaterConfig.setPath(configPath);
            WaterConfig.init();
            SPEC = WaterConfig.registerBlocking(ChlorideConfig.class);
        } catch (final Exception e) {
            LOGGER.error(IT, "Cannot load config, running with default values", e);
        }
    }

    public static void write() {
        final ConfigSpec spec = SPEC;
        if (spec == null) return;
        spec.refresh();      // RE-VALIDATES DIRECT (REFLECT-MODE) FIELD MUTATIONS THE SPEC CANNOT SEE
        spec.setDirty(true); // THE WATERCONFIG WORKER PERSISTS ASYNC; ITS SHUTDOWN HOOK COVERS GAME EXIT
    }
}
