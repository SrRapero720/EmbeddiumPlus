package me.srrapero720.embeddiumplus.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class EmbeddiumPlusConfig {
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.EnumValue<Quality> fadeInQuality;

    public static ForgeConfigSpec.EnumValue<Complexity> fpsCounterMode;
    public static ConfigValue<Integer> fpsCounterPosition;
    public static ForgeConfigSpec.ConfigValue<Integer> cloudHeight;


    public static ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxEntityRenderDistanceY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> entityWhitelist;

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
        ConfigBuilder builder = new ConfigBuilder("EmbeddiumPlus");


        builder.comment("You can configure FPS overlay at the corner")
                .block("fps_overlay", b -> {
            fpsCounterMode = b.defineEnum("DisplayMode", Complexity.ADVANCED);
            fpsCounterPosition = b.define("OverlayMargin", 12);
        });

        builder.comment("Configure max Entity distance")
                .block("entity_distance_limit", b -> {
                    enableDistanceChecks = b
                            .comment("Turn on this feature")
                            .define("Enable", true);

                    maxEntityRenderDistanceSquare = b
                            .comment("Max horizontal render distance")
                            .comment("Value is squared, default was 64^2 (or 64x64)")
                            .define("maxHorizontalDistance", 4096);

                    maxEntityRenderDistanceY = b
                            .comment("Max vertical render distance")
                            .comment("Value is raw")
                            .define("maxVerticalDistance", 32);

                    entityWhitelist = b
                            .comment("List of entities to not be ignored when are out of configured radius.")
                            .comment("Accepts ResourceLocation and Mod IDs")
                            .comment("Example: \"minecraft:bat\" for specific entity or \"alexmobs:*\" for all mod specific entities")
                            .defineListAllowEmpty("entityWhitelist", Collections::emptyList, (s) -> s.toString().contains(":"));
        });

        builder.comment("Configure max BlockEntity distance")
                .block("block_entity_distance", b -> {
                    maxTileEntityRenderDistanceSquare = b
                            .comment("Max horizontal render distance")
                            .comment("Value is squared, default was 64^2 (or 64x64)")
                            .define("maxHorizontalDistance", 4096);
                    maxTileEntityRenderDistanceY = b
                            .comment("Max vertical render distance")
                            .comment("Value is raw")
                            .define("maxVerticalDistance", 32);
                });

        builder.comment("Configure TrueDarkness feature")
                .comment("Section deprecated and removed soon (in favor of rework)")
                .block("true_darkness", b -> {

                    trueDarknessEnabled = b
                            .comment("Turn on this feature")
                            .define("Enable", false);

                    darknessOption = b
                            .comment("Sets darkness mode")
                            .comment("Depending of the option darkness can be less or more aggressive")
                            .defineEnum("DarknessMode", DarknessOption.DARK);

                    builder.block("Advanced", ignored -> {
                        blockLightOnly = b.define("BlockLightingOnly", false);
                        ignoreMoonPhase = b.define("IgnoreMoonPhase", false);
                        minimumMoonLevel = b.defineInRange("MinimumMoonBrightness", 0, 0, 1d);
                        maximumMoonLevel = b.defineInRange("MaximumMoonBrightness", 0.25d, 0, 1d);
                    });

                    builder.comment("Configure what dimension should use TrueDarkness")
                            .block("DimensionSettings", ignored -> {
                                darkDefault = b.define("DefaultSetting", false);
                                darkOverworld = b.define("Overworld", true);
                                darkNether = b.define("Nether", false);
                                darkNetherFogConfigured = b.defineInRange("DarkNetherFogBrightness", .5, 0, 1d);
                                darkEnd = b.define("Dark End?", false);
                                darkEndFogConfigured = b.defineInRange("DarkEndFogBrightness", 0, 0, 1d);
                                darkSkyless = b.define("DarkWhenNoSkylight", false);
                    });
        });

        builder.block("Misc", b -> {
            cloudHeight = b.define("CloudHeight", 128);
            fadeInQuality =  b.defineEnum("ChunkFadeInQuality", Quality.FANCY);
            fog = b.define("RenderFog", true);
            enableExtendedServerViewDistance = b.define("ExtendedServerViewDistance", true);
            hideJEI = b.define("HideJEI", false);
            fullScreenMode = b.defineEnum("BorderlessFullscreen", FullscreenMode.FULLSCREEN);
        });

        ConfigSpec = builder.save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }


    public enum FullscreenMode { WINDOWED, BORDERLESS, FULLSCREEN }

    public enum Complexity { OFF, SIMPLE, ADVANCED; }

    public enum Quality { OFF, FAST, FANCY; }

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
}