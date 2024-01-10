package me.srrapero720.embeddiumplus;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.blaze3d.platform.Window;
import me.srrapero720.embeddiumplus.mixins.impl.borderless.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmbyConfig {
    public static final ForgeConfigSpec SPECS;

    // GENERAL
    public static final ForgeConfigSpec.EnumValue<FullScreenMode> fullScreen;
    public static final ForgeConfigSpec.EnumValue<FPSDisplayMode> fpsDisplayMode;
    public static final ForgeConfigSpec.EnumValue<FPSDisplayGravity> fpsDisplayGravity;
    public static final ForgeConfigSpec.EnumValue<FPSDisplaySystemMode> fpsDisplaySystemMode;
    public static final ForgeConfigSpec.IntValue fpsDisplayMargin;
    public static final ForgeConfigSpec.BooleanValue fpsDisplayShadow;
    public static volatile int fpsDisplayMarginCache;
    public static volatile boolean fpsDisplayShadowCache;

    // QUALITY
    public static final ForgeConfigSpec.BooleanValue fog;
    public static final ForgeConfigSpec.IntValue cloudsHeight;
    public static final ForgeConfigSpec.EnumValue<ChunkFadeSpeed> chunkFadeSpeed;
    public static volatile boolean fogCache;
    public static volatile int cloudsHeightCache;

    // QUALITY: TRUE DARKNESS
    public static final ForgeConfigSpec.EnumValue<DarknessMode> darknessMode;
    public static final ForgeConfigSpec.BooleanValue darknessOnOverworld;
    public static final ForgeConfigSpec.BooleanValue darknessOnNether;
    public static final ForgeConfigSpec.DoubleValue darknessNetherFogBright;
    public static final ForgeConfigSpec.BooleanValue darknessOnEnd;
    public static final ForgeConfigSpec.DoubleValue darknessEndFogBright;
    public static final ForgeConfigSpec.BooleanValue darknessByDefault;
    public static final ForgeConfigSpec.BooleanValue darknessOnNoSkyLight;
    public static final ForgeConfigSpec.BooleanValue darknessBlockLightOnly;
    public static final ForgeConfigSpec.BooleanValue darknessAffectedByMoonPhase;
    public static final ForgeConfigSpec.DoubleValue darknessNewMoonBright;
    public static final ForgeConfigSpec.DoubleValue darknessFullMoonBright;
    public static volatile boolean darknessOnOverworldCache;
    public static volatile boolean darknessOnNetherCache;
    public static volatile double darknessNetherFogBrightCache;
    public static volatile boolean darknessOnEndCache;
    public static volatile double darknessEndFogBrightCache;
    public static volatile boolean darknessByDefaultCache;
    public static volatile boolean darknessOnNoSkyLightCache;
    public static volatile boolean darknessBlockLightOnlyCache;
    public static volatile boolean darknessAffectedByMoonPhaseCache;
    public static volatile double darknessNewMoonBrightCache;
    public static volatile double darknessFullMoonBrightCache;

    // PERFORMANCE;
    public static final ForgeConfigSpec.BooleanValue hideJREI;
    public static final ForgeConfigSpec.BooleanValue fontShadows;
    public static final ForgeConfigSpec.BooleanValue fastChests;
    public static final ForgeConfigSpec.BooleanValue fastBeds;
    public static volatile boolean hideJREICache;
    public static volatile boolean fontShadowsCache;
    public static volatile boolean fastChestsCache;
    public static volatile boolean fastBedsCache;

    public static final ForgeConfigSpec.BooleanValue tileEntityDistanceCulling;
    public static final ForgeConfigSpec.IntValue tileEntityCullingDistanceX;
    public static final ForgeConfigSpec.IntValue tileEntityCullingDistanceY;
    public static final ForgeConfigSpec.BooleanValue entityDistanceCulling;
    public static final ForgeConfigSpec.IntValue entityCullingDistanceX;
    public static final ForgeConfigSpec.IntValue entityCullingDistanceY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entityWhitelist; // QUICK CHECK
    public static volatile boolean tileEntityDistanceCullingCache;
    public static volatile int tileEntityCullingDistanceXCache;
    public static volatile int tileEntityCullingDistanceYCache;
    public static volatile boolean entityDistanceCullingCache;
    public static volatile int entityCullingDistanceXCache;
    public static volatile int entityCullingDistanceYCache;

    // DYN LIGHTS
    public static final ForgeConfigSpec.EnumValue<DynLightsSpeed> dynLightSpeed;
    public static final ForgeConfigSpec.BooleanValue dynLightsOnEntities;
    public static final ForgeConfigSpec.BooleanValue dynLightsOnTileEntities;
    public static final ForgeConfigSpec.BooleanValue dynLightsUpdateOnPositionChange;
    public static volatile boolean dynLightsOnEntitiesCache;
    public static volatile boolean dynLightsOnTileEntitiesCache;
    public static volatile boolean dynLightsUpdateOnPositionChangeCache;

    static {
        var BUILDER = new ForgeConfigSpec.Builder();

        // embeddiumplus ->
        BUILDER.push("embeddiumplus");

        // embeddiumplus -> general ->
        BUILDER.push("general");
        fullScreen = BUILDER
                .comment("Set Fullscreen mode", "Borderless let you change between screens more faster and move your mouse across monitors")
                .defineEnum("fullscreen", FullScreenMode.WINDOWED);

        fpsDisplayMode = BUILDER
                .comment("Configure FPS Display mode", "Complete mode gives you min FPS count and average count")
                .defineEnum("fpsDisplay", FPSDisplayMode.ADVANCED);

        fpsDisplayGravity = BUILDER
                .comment("Configure FPS Display gravity", "Places counter on specified corner of your screen")
                .defineEnum("fpsDisplayGravity", FPSDisplayGravity.LEFT);

        fpsDisplaySystemMode = BUILDER
                .comment("Shows GPU and memory usage onto FPS display")
                .defineEnum("fpsDisplaySystem", FPSDisplaySystemMode.OFF);

        fpsDisplayMargin = BUILDER
                .comment("Configure FPS Display margin", "Give some space between corner and text")
                .defineInRange("fpsDisplayMargin", 12, 0, 48);

        fpsDisplayShadow = BUILDER
                .comment("Toggle FPS Display shadow", "In case sometimes you can't see the text")
                .define("fpsDisplayShadow", false);

        // embeddiumplus ->
        BUILDER.pop();

        // embeddiumplus -> quality
        BUILDER.push("quality");
        fog = BUILDER
                .comment("Toggle fog feature", "Fog was a vanilla feature, toggling off may increases performance")
                .define("fog", true);
        cloudsHeight = BUILDER
                .comment("Raise clouds", "Modify clouds height perfect for a adaptative world experience")
                .defineInRange("cloudsHeight", 128, 0, 320);

        chunkFadeSpeed = BUILDER
                .comment("Chunks fade in speed", "This option doesn't affect performance, just changes speed")
                .defineEnum("chunkFadeSpeed", ChunkFadeSpeed.SLOW);

        // embeddiumplus -> quality -> darkness
        BUILDER.push("darkness");
        darknessMode = BUILDER
                .comment("Configure Darkness Mode", "Each config changes what is considered 'true darkness'")
                .defineEnum("mode", DarknessMode.OFF);

        darknessOnOverworld = BUILDER
                .comment("Toggle Darkness on Overworld dimension")
                .define("enableOnOverworld", true);

        darknessOnNether = BUILDER
                .comment("Toggle Darkness on Nether dimension")
                .define("enableOnNether", false);

        darknessNetherFogBright = BUILDER
                .comment("Configure fog brightness on nether when darkness is enabled")
                .defineInRange("netherFogBright", 0.5f, 0d, 1d);

        darknessOnEnd = BUILDER
                .comment("Toggle Darkness on End dimension")
                .define("enableOnEnd", false);

        darknessEndFogBright = BUILDER
                .comment("Configure fog brightness on nether when darkness is enabled")
                .defineInRange("endFogBright", 0.5f, 0d, 1d);

        darknessByDefault = BUILDER
                .comment("Toggle Darkness default mode for modded dimensions", "This option will be replaced with a whitelist in a near future")
                .define("valueByDefault", false);

        darknessOnNoSkyLight = BUILDER
                .comment("Toggle darkness when dimension has no SkyLight")
                .define("enableOnNoSkyLight", false);

        darknessBlockLightOnly = BUILDER
                .comment("Disables all bright sources of darkness like moon or fog", "Only affects darkness effect")
                .define("enableBlockLightOnly", false);

        darknessAffectedByMoonPhase = BUILDER
                .comment("Toggles if moon phases affects darkness in the overworld")
                .define("affectedByMoonPhase", true);

        darknessFullMoonBright = BUILDER
                .comment("Configure max moon brightness level with darkness")
                .defineInRange("fullMoonBright",0.25d, 0, 1d);

        darknessNewMoonBright = BUILDER
                .comment("Configure min moon brightness level with darkness")
                .defineInRange("newMoonBright",0, 0, 1d);


        // embeddiumplus ->
        BUILDER.pop(2);

        // embeddiumplus -> performance
        BUILDER.push("performance");

        hideJREI = BUILDER
                .comment("Toggles JREI item rendering until searching", "Increases performance a little bit and cleans your screen when you don't want to use it")
                .define("hideJREI", false);

        fontShadows = BUILDER
                .comment("Toggles Minecraft Fonts shadows", "Depending of the case may increase performance", "Gives a flat style text")
                .define("fontShadows", false);

        fastChests = BUILDER
                .comment("Toggles FastChest feature", "Without flywheel installed or using any backend, it increases FPS significatly on chest rooms")
                .define("fastChests", false);

        fastBeds = BUILDER
                .comment("Toggles FastBeds feature")
                .define("fastBeds", true);

        // embeddiumplus -> performance -> distanceCulling
        BUILDER.push("distanceCulling");

        // embeddiumplus -> performance -> distanceCulling -> tileEntities
        BUILDER.push("tileEntities");
        tileEntityDistanceCulling = BUILDER
                .comment("Toggles distance culling for Block Entities", "Maybe you use another mod for that :(")
                .define("enable", true);

        tileEntityCullingDistanceX = BUILDER
                .comment("Configure horizontal max distance before cull Block entities", "Value is squared, default was 64^2 (or 64x64)")
                .defineInRange("cullingDistanceX", 4096, 0, Integer.MAX_VALUE);

        tileEntityCullingDistanceY = BUILDER
                .comment("Configure vertical max distance before cull Block entities", "Value is raw")
                .defineInRange("cullingDistanceY", 32, 0, 512);

        // embeddiumplus -> performance -> distanceCulling ->
        BUILDER.pop();

        // embeddiumplus -> performance -> distanceCulling -> entities
        BUILDER.push("entities");
        entityDistanceCulling = BUILDER
                .comment("Toggles distance culling for entities", "Maybe you use another mod for that :(")
                .define("enable", true);
        entityCullingDistanceX = BUILDER
                .comment("Configure horizontal max distance before cull entities", "Value is squared, default was 64^2 (or 64x64)")
                .defineInRange("cullingDistanceX", 4096, 0, Integer.MAX_VALUE);

        entityCullingDistanceY = BUILDER
                .comment("Configure vertical max distance before cull entities", "Value is raw")
                .defineInRange("cullingDistanceY", 32, 0, 512);

        entityWhitelist = BUILDER
                .comment("List of all entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod")
                .defineListAllowEmpty(Collections.singletonList("whitelist"), Collections::emptyList, (s) -> s.toString().contains(":"));

        // embeddiumplus ->
        BUILDER.pop(3);

        // embeddiumplus -> dynlights
        BUILDER.push("dynlights");
        dynLightSpeed = BUILDER
                .comment("Configure how fast light whould be updated")
                .defineEnum("updateSpeed", DynLightsSpeed.REALTIME);

        dynLightsOnEntities = BUILDER
                .comment("Toggle if Entities should have dynamic lights")
                .define("onEntities", true);

        dynLightsOnTileEntities = BUILDER
                .comment("Toggle if Block Entities should have dynamic lights")
                .define("onTileEntities", true);

        dynLightsUpdateOnPositionChange = BUILDER
                .define("updateOnlyOnPositionChange", true);


        // embeddiumplus ->
        BUILDER.pop();

        SPECS = BUILDER.build();
    }

    public static boolean isLoaded() {
        return SPECS.isLoaded();
    }

    public static void load() {
        if (isLoaded()) return;

        EmbeddiumPlus.LOGGER.warn("Force-loading Embeddium++ config");

        // FORCE LOAD
        var path = FMLPaths.CONFIGDIR.get().resolve("embeddium++.toml");
        try {
            final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

            configData.load();
            SPECS.setConfig(configData);
            updateCache(null);
        } catch (Exception e) {
            var file = path.toFile();
            if (!file.exists()) throw new RuntimeException("Failed to read configuration file");
            if (!file.delete()) throw new RuntimeException("Failed to remove corrupted configuration file");
            load();
        }
    }

    @SubscribeEvent
    public static void updateCache(ModConfigEvent ignored) {
        EmbeddiumPlus.LOGGER.info("Updating cache...");

        fpsDisplayMarginCache = fpsDisplayMargin.get();
        fpsDisplayShadowCache = fpsDisplayShadow.get();

        fogCache = fog.get();
        cloudsHeightCache = cloudsHeight.get();

        darknessOnOverworldCache = darknessOnOverworld.get();
        darknessOnNetherCache = darknessOnNether.get();
        darknessNetherFogBrightCache = darknessNetherFogBright.get();
        darknessOnEndCache = darknessOnEnd.get();
        darknessEndFogBrightCache = darknessEndFogBright.get();
        darknessByDefaultCache = darknessByDefault.get();
        darknessOnNoSkyLightCache = darknessOnNoSkyLight.get();
        darknessBlockLightOnlyCache = darknessBlockLightOnly.get();
        darknessAffectedByMoonPhaseCache = darknessAffectedByMoonPhase.get();
        darknessNewMoonBrightCache = darknessNewMoonBright.get();
        darknessFullMoonBrightCache = darknessFullMoonBright.get();

        hideJREICache = hideJREI.get();
        fontShadowsCache = fontShadows.get();
        fastChestsCache = fastChests.get();
        fastBedsCache = fastBeds.get();

        tileEntityDistanceCullingCache = tileEntityDistanceCulling.get();
        tileEntityCullingDistanceXCache = tileEntityCullingDistanceX.get();
        tileEntityCullingDistanceYCache = tileEntityCullingDistanceY.get();
        entityDistanceCullingCache = entityDistanceCulling.get();
        entityCullingDistanceXCache = entityCullingDistanceX.get();
        entityCullingDistanceYCache = entityCullingDistanceY.get();

        dynLightsOnEntitiesCache = dynLightsOnEntities.get();
        dynLightsOnTileEntitiesCache = dynLightsOnTileEntities.get();
        dynLightsUpdateOnPositionChangeCache = dynLightsUpdateOnPositionChange.get();
    }

    public static void setFullScreenMode(Options opts, FullScreenMode value) {
        fullScreen.set(value);
        opts.fullscreen.set(value != FullScreenMode.WINDOWED);

        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();
        if (window.isFullscreen() != opts.fullscreen.get()) {
            window.toggleFullScreen();
            opts.fullscreen.set(window.isFullscreen());
        }

        if (opts.fullscreen.get()) {
            ((MainWindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }


    /* CONFIG VALUES */
    public enum FPSDisplayMode {
        OFF, SIMPLE, ADVANCED;

        public boolean off() {
            return this == OFF;
        }
    }
    public enum FPSDisplayGravity { LEFT, CENTER, RIGHT; }
    public enum ChunkFadeSpeed { OFF, FAST, SLOW; }
    public enum FPSDisplaySystemMode {
        OFF, ON, GPU, RAM;

        public boolean ram() { return this == RAM || this == ON; }
        public boolean gpu() { return this == GPU || this == ON; }
        public boolean off() { return this == OFF; }
    }
    public enum DynLightsSpeed {
        OFF(-1),
        SLOW(750),
        NORMAL(500),
        FAST(250),
        SUPERFAST(100),
        FASTESTS(50),
        REALTIME(-1);
        private final int delay;

        DynLightsSpeed(int delay) {
            this.delay = delay;
        }
        public int getDelay() { return delay; }

        public boolean off() {
            return this == OFF;
        }
    }
    public enum DarknessMode {
        TOTAL_DARKNESS(0.04f),
        PITCH_BLACK(0f),
        DARK(0.08f),
        DIM(0.12f),
        OFF(-1);

        public final float value;
        DarknessMode(float value) { this.value = value; }
    }
    public enum FullScreenMode {
        WINDOWED, BORDERLESS, FULLSCREEN;

        public static FullScreenMode nextOf(FullScreenMode current) {
            return switch (current) {
                case WINDOWED -> BORDERLESS;
                case BORDERLESS -> FULLSCREEN;
                case FULLSCREEN -> WINDOWED;
            };
        }

        public static FullScreenMode getVanillaConfig() {
            return Minecraft.getInstance().options.fullscreen().get() ? BORDERLESS : WINDOWED;
        }

        public boolean isBorderless() {
            return this == BORDERLESS;
        }
    }
}
