package me.srrapero720.embeddium_extras.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class EmbeddiumExtrasConfig {
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
    public static ConfigValue<Boolean> enableExtendedServerViewDistance;

    public static ConfigValue<Boolean> hideJEI;

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


    static {
        var builder = new ConfigBuilder("Dynamic Lights Settings");

        builder.block("Misc", b -> {
            cloudHeight = b.define("Cloud Height [Raw, Default 256]", 256);
            fadeInQuality =  b.define("Chunk Fade In Quality (OFF, FAST, FANCY)", "FANCY");
            fog = b.define("Render Fog", true);
            enableExtendedServerViewDistance = b.define("Enable Extended Server View Distance", true);
            hideJEI = b.define("Hide JEI Until Searching", true);
            fullScreenMode = b.defineEnum("Use Borderless Fullscreen", FullscreenMode.FULLSCREEN);
        });

        builder.block("FPS Counter", b -> {
            fpsCounterMode = b.define("Display FPS Counter (OFF, SIMPLE, ADVANCED)", "ADVANCED");
            fpsCounterPosition = b.define("FPS Counter Distance", 12);
        });


        builder.block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.block("True Darkness", b -> {
            trueDarknessEnabled = b.define("Use True Darkness", true);
            darknessOption = b.defineEnum("Darkness Setting (PITCH_BLACK, REALLY_DARK, DARK, DIM)", DarknessOption.DARK);

            builder.block("Advanced", b2 -> {
                blockLightOnly = b2.define("Only Effect Block Lighting", false);
                ignoreMoonPhase = b2.define("Ignore Moon Light", false);
                minimumMoonLevel = b2.defineInRange("Minimum Moon Brightness (0->1)", 0, 0, 1d);
                maximumMoonLevel = b2.defineInRange("Maximum Moon Brightness (0->1)", 0.25d, 0, 1d);
            });

            builder.block("Dimension Settings", b2 -> {
                darkOverworld = b2.define("Dark Overworld?", true);
                darkDefault = b2.define("Dark By Default?", false);
                darkNether = b2.define("Dark Nether?", false);
                darkNetherFogConfigured = b2.defineInRange("Dark Nether Fog Brightness (0->1)", .5, 0, 1d);
                darkEnd = b2.define("Dark End?", false);
                darkEndFogConfigured = b.defineInRange("Dark End Fog Brightness (0->1)", 0, 0, 1d);
                darkSkyless = b2.define("Dark If No Skylight?", false);
            });
        });

        ConfigSpec = builder.save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }


    public enum Complexity {
        OFF("Off"),
        SIMPLE("Simple"),
        ADVANCED("Advanced");

        private final String name;

        private Complexity(String name) {
            this.name = name;
        }
    }

    public enum Quality {
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
}