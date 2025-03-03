package me.srrapero720.chloride;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.gson.*;
import com.mojang.blaze3d.platform.Window;
import me.srrapero720.chloride.mixins.impl.borderless.accessors.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.srrapero720.chloride.Chloride.LOGGER;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChlorideConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ChlorideConfig.class, new ChlorideConfigSerial())
            .create();
    private static final ChlorideConfig DUMMY = new ChlorideConfig();
    private static File configFile;

    @ConfigField public static FullScreenMode fullScreen = FullScreenMode.WINDOWED;
    @ConfigField public static FPSDisplayMode fpsDisplayMode = FPSDisplayMode.ADVANCED;
    @ConfigField public static FPSDisplayGravity fpsDisplayGravity = FPSDisplayGravity.LEFT;
    @ConfigField public static FPSDisplaySystemMode fpsDisplaySystemMode = FPSDisplaySystemMode.OFF;
    @ConfigField public static int fpsDisplayMargin = 12;
    @ConfigField public static boolean fpsDisplayShadow = false;

    @ConfigField public static boolean fog = true;
    @ConfigField public static boolean blueBand = true;
    @ConfigField public static int cloudsHeight = 192;
    @ConfigField public static boolean disableNameTagRender = false;
    @ConfigField public static ChunkFadeSpeed chunkFadeSpeed = ChunkFadeSpeed.SLOW;

    @ConfigField public static DarknessMode darknessMode = DarknessMode.OFF;
    @ConfigField public static boolean darknessOnOverworld = true;
    @ConfigField public static boolean darknessOnNether = false;
    @ConfigField public static double darknessNetherFogBright = 0.5;
    @ConfigField public static boolean darknessOnEnd = false;
    @ConfigField public static double darknessEndFogBright = 0.5;
    @ConfigField public static boolean darknessByDefault = false;
    @ConfigField public static List<String> darknessDimensionWhiteList = Collections.emptyList();
    @ConfigField public static boolean darknessOnNoSkyLight = false;
    @ConfigField public static boolean darknessBlockLightOnly = false;
    @ConfigField public static boolean darknessAffectedByMoonPhase = true;
    @ConfigField public static double darknessNewMoonBright = 0.0;
    @ConfigField public static double darknessFullMoonBright = 0.25;

    @ConfigField public static boolean hideJREMI = false;
    @ConfigField public static boolean fontShadows = true;
    @ConfigField public static LeavesCullingMode leavesCulling = LeavesCullingMode.OFF;
    @ConfigField public static boolean fastChests = false;
    @ConfigField public static boolean fastBeds = false;

    @ConfigField public static boolean tileEntityDistanceCulling = true;
    @ConfigField public static int tileEntityCullingDistanceX = 4096;
    @ConfigField public static int tileEntityCullingDistanceY = 32;
    @ConfigField public static boolean entityDistanceCulling = true;
    @ConfigField public static int entityCullingDistanceX = 4096;
    @ConfigField public static int entityCullingDistanceY = 32;
    @ConfigField public static boolean monsterDistanceCulling = false;
    @ConfigField public static int monsterCullingDistanceX = 16384;
    @ConfigField public static int monsterCullingDistanceY = 64;
    @ConfigField public static List<String> entityWhitelist = List.of("minecraft:ghast", "minecraft:ender_dragon", "iceandfire:*", "create:*");
    @ConfigField public static List<String> monsterWhitelist = Collections.emptyList();
    @ConfigField public static List<String> tileEntityWhitelist = List.of("waterframes:*");

    @ConfigField public static AttachMode borderlessAttachModeF11 = AttachMode.ATTACH;
    @ConfigField public static boolean fastLanguageReload = true;

    @ConfigField public static DynLightsSpeed dynLightSpeed = DynLightsSpeed.REALTIME;
    @ConfigField public static boolean dynLightsOnEntities = true;
    @ConfigField public static boolean dynLightsOnTileEntities = true;
    @ConfigField public static boolean dynLightsUpdateOnPositionChange = true;


    public static void setFullScreenMode(Options opts, FullScreenMode value) {
        fullScreen = value;
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

    public enum AttachMode {
        ATTACH, REPLACE, OFF;
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

        public static FullScreenMode nextBorderless(FullScreenMode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> BORDERLESS;
            };
        }

        public static FullScreenMode nextFullscreen(FullScreenMode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> FULLSCREEN;
            };
        }

        public static FullScreenMode getVanillaConfig() {
            return Minecraft.getInstance().options.fullscreen().get() ? BORDERLESS : WINDOWED;
        }

        public boolean isBorderless() {
            return this == BORDERLESS;
        }
    }

    public enum LeavesCullingMode {
        ALL, OFF; // MORE, HALF, LESS
    }

    static void load(Path configPath) {
        configFile = configPath.resolve("chloride-client.json").toFile();
        if (!configFile.exists()) {
            write();
        } else {
            read();
            write();
        }
    }

    public static void write() {
        try (final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(configFile))) {
            out.write(GSON.toJson(DUMMY).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("Cannot write file", e);
        }
    }

    private static void read() {
        try (final BufferedInputStream in = new BufferedInputStream(new FileInputStream(configFile))) {
            GSON.fromJson(new String(in.readAllBytes(), StandardCharsets.UTF_8), ChlorideConfig.class);
        } catch (final Exception e) {
            LOGGER.error("Cannot read file, writting to defaults", e);
            write();
        }
    }

    private static final class ChlorideConfigSerial implements JsonSerializer<ChlorideConfig>, JsonDeserializer<ChlorideConfig> {
        @Override
        public JsonElement serialize(ChlorideConfig src, Type typeOfSrc, JsonSerializationContext context) {
            final JsonObject jsonObject = new JsonObject();

            for (final Field field: ChlorideConfig.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(ConfigField.class))
                    continue;

                try {
                    field.setAccessible(true);
                    jsonObject.add(field.getName(), context.serialize(field.get(null)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error al acceder al campo: " + field.getName(), e);
                }
            }

            return jsonObject;
        }

        @Override
        public ChlorideConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            for (final Field field: ChlorideConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigField.class) || !Modifier.isStatic(field.getModifiers()))
                    continue;

                try {
                    final JsonElement element = jsonObject.get(field.getName());
                    if (element == null) continue;
                    if (element.isJsonNull()) continue;

                    field.setAccessible(true);
                    field.set(null, context.deserialize(element, field.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error al asignar el valor al campo: " + field.getName(), e);
                }
            }

            return new ChlorideConfig(); // Devuelve una instancia vacía; solo usa campos estáticos
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @interface ConfigField {

    }
}
