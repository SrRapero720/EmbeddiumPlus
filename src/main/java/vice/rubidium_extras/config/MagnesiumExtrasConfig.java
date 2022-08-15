package vice.rubidium_extras.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class MagnesiumExtrasConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ConfigValue<String> fadeInQuality;

    public static ConfigValue<String> fpsCounterMode;
    public static ConfigValue<Integer> fpsCounterPosition;
    public static ForgeConfigSpec.ConfigValue<Integer> cloudHeight;


    public static ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ConfigValue<Boolean> fog;
    public static ConfigValue<Boolean> enableDistanceChecks;

    public static ConfigValue<Boolean> hideJEI;

    // Ok Zoomer
    public static ZoomValues zoomValues = new ZoomValues();
    public static ConfigValue<Boolean> lowerZoomSensitivity;
    public static ConfigValue<String> zoomTransition;
    public static ConfigValue<String> zoomMode;
    public static ConfigValue<String> cinematicCameraMode;
    public static ConfigValue<Boolean> zoomScrolling;
    public static ConfigValue<Boolean> zoomOverlay;


    public static ForgeConfigSpec.EnumValue<FullscreenMode> fullScreenMode;


    // Total Darkness
    public static double darkNetherFogEffective;
    public static double darkEndFogEffective;
    public static ForgeConfigSpec.BooleanValue trueDarknessEnabled;
    public static ForgeConfigSpec.EnumValue<DarknessOption> darknessOption;
    //advanced
    public static ForgeConfigSpec.DoubleValue darkNetherFogConfigured;
    public static ForgeConfigSpec.BooleanValue darkEnd;
    public static ForgeConfigSpec.DoubleValue darkEndFogConfigured;
    public static ForgeConfigSpec.BooleanValue darkSkyless;
    public static ForgeConfigSpec.BooleanValue blockLightOnly;
    public static ForgeConfigSpec.BooleanValue ignoreMoonPhase;
    public static ForgeConfigSpec.DoubleValue minimumMoonLevel;
    public static ForgeConfigSpec.DoubleValue maximumMoonLevel;
    public static ForgeConfigSpec.BooleanValue darkOverworld;
    public static ForgeConfigSpec.BooleanValue darkDefault;
    public static ForgeConfigSpec.BooleanValue darkNether;



    static
    {
        var builder = new ConfigBuilder("Dynamic Lights Settings");

        builder.Block("Misc", b -> {
            cloudHeight = b.define("Cloud Height [Raw, Default 256]", 256);
            fadeInQuality =  b.define("Chunk Fade In Quality (OFF, FAST, FANCY)", "FANCY");
            fog = b.define("Render Fog", true);
            hideJEI = b.define("Hide JEI Until Searching", true);
            fullScreenMode = b.defineEnum("Use Borderless Fullscreen", FullscreenMode.FULLSCREEN);
        });

        builder.Block("FPS Counter", b -> {
            fpsCounterMode = b.define("Display FPS Counter (OFF, SIMPLE, ADVANCED)", "ADVANCED");
            fpsCounterPosition = b.define("FPS Counter Distance", 12);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower Zoom Sensitivity", true);
            zoomScrolling = b.define("Zoom Scrolling Enabled", true);
            zoomTransition = b.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("Zoom Overlay?", true);
            //zoomValues = b.define("Zoom Advanced Values", new ZoomValues());
        });

        builder.Block("True Darkness", b -> {
            trueDarknessEnabled = b.define("Use True Darkness", true);
            darknessOption = b.defineEnum("Darkness Setting (PITCH_BLACK, REALLY_DARK, DARK, DIM)", DarknessOption.DARK);

            builder.Block("Advanced", b2 -> {
                blockLightOnly = b2.define("Only Effect Block Lighting", false);
                ignoreMoonPhase = b2.define("Ignore Moon Light", false);
                minimumMoonLevel = b2.defineInRange("Minimum Moon Brightness (0->1)", 0, 0, 1d);
                maximumMoonLevel = b2.defineInRange("Maximum Moon Brightness (0->1)", 0.25d, 0, 1d);
            });

            builder.Block("Dimension Settings", b2 -> {
                darkOverworld = b2.define("Dark Overworld?", true);
                darkDefault = b2.define("Dark By Default?", false);
                darkNether = b2.define("Dark Nether?", false);
                darkNetherFogConfigured = b2.defineInRange("Dark Nether Fog Brightness (0->1)", .5, 0, 1d);
                darkEnd = b2.define("Dark End?", false);
                darkEndFogConfigured = b.defineInRange("Dark End Fog Brightness (0->1)", 0, 0, 1d);
                darkSkyless = b2.define("Dark If No Skylight?", false);
            });
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }


    public enum Complexity
    {
        OFF("Off"),
        SIMPLE("Simple"),
        ADVANCED("Advanced");

        private final String name;

        private Complexity(String name) {
            this.name = name;
        }
    }

    public enum Quality
    {
        OFF("Off"),
        FAST("Fast"),
        FANCY("Fancy");

        private final String name;

        private Quality(String name) {
            this.name = name;
        }
    }

    public enum DarknessOption {
        PITCH_BLACK(0f),
        REALLY_DARK (0.04f),
        DARK(0.08f),
        DIM(0.12f);

        public final float value;

        private DarknessOption(float value) {
            this.value = value;
        }
    }

    public enum FullscreenMode {
        WINDOWED,
        BORDERLESS,
        FULLSCREEN
    }

    public enum ZoomTransitionOptions {
        OFF,
        SMOOTH
    }

    public enum ZoomModes {
        HOLD,
        TOGGLE,
        PERSISTENT
    }

    public enum CinematicCameraOptions {
        OFF,
        VANILLA,
        MULTIPLIED
    }

    public static class ZoomValues {
        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The divisor applied to the FOV when zooming.")
        public double zoomDivisor = 4.0;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The minimum value that you can scroll down.")
        public double minimumZoomDivisor = 1.0;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The maximum value that you can scroll up.")
        public double maximumZoomDivisor = 50.0;

        //@Setting.Constrain.Range(min = 0.0)
        //@Setting(comment = "The number which is de/incremented by zoom scrolling. Used when the zoom divisor is above the starting point.")
        public double scrollStep = 1.0;

       //"The number which is de/incremented by zoom scrolling. Used when the zoom divisor is below the starting point.")
        public double lesserScrollStep = 0.5;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The multiplier used for the multiplied cinematic camera.")
        public double cinematicMultiplier = 4.0;

        ////@Setting.Constrain.Range(min = Double.MIN_NORMAL, max = 1.0)
        //@Setting(comment = "The multiplier used for smooth transitions.")
        public double smoothMultiplier = 0.75;

        //@Setting(comment = "The minimum value which the linear transition step can reach.")
        public double minimumLinearStep = 0.125;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
       // @Setting(comment = "The maximum value which the linear transition step can reach.")
        public double maximumLinearStep = 0.25;
    }
}
