package me.srrapero720.chloride;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.chloride.mixins.impl.accessors.WindowAccessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.srrapero720.chloride.Chloride.LOGGER;

public class ChlorideConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ResourceLocation.class, new ChlorideResourceLocationSerial())
            .registerTypeAdapter(new TypeToken<List<ResourceLocation>>(){}.getType(), new ListResourceLocationAdapter())
            .registerTypeAdapter(ChlorideConfig.class, new ChlorideConfigSerial())
            .create();
    private static final ChlorideConfig DUMMY = new ChlorideConfig();
    private static File configFile;

    @ConfigField public static boolean modpackMode = false;
    @ConfigField public static FullScreenMode fullScreen = FullScreenMode.WINDOWED;
    @ConfigField public static FPSDisplayMode fpsDisplayMode = FPSDisplayMode.ADVANCED;
    @ConfigField public static FPSDisplayAlign fpsDisplayAlign = FPSDisplayAlign.LEFT;
    @ConfigField public static FPSDisplayVAlign fpsDisplayVAlign = FPSDisplayVAlign.TOP;
    @ConfigField public static FPSDisplaySystemMode fpsDisplaySystemMode = FPSDisplaySystemMode.OFF;
    @ConfigField public static int fpsDisplayMargin = 12;
    @ConfigField public static int fpsDisplayVMargin = 12;
    @ConfigField public static boolean fpsDisplayShadow = false;

    @ConfigField public static boolean fog = true;
    @ConfigField public static boolean blueBand = true;
    @ConfigField public static boolean customFog = true;
    @ConfigField public static int fogStart = 0;
    @ConfigField public static int fogEnd = 192;
    @ConfigField public static FogShape fogShape = FogShape.CYLINDER;
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
    @ConfigField public static List<ResourceLocation> darknessDimensionWhiteList = Collections.emptyList();
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
    @ConfigField public static List<ResourceLocation> entityWhitelist = Tools.toId("minecraft:ghast", "minecraft:ender_dragon", "iceandfire:all", "create:all");
    @ConfigField public static List<ResourceLocation> monsterWhitelist = Tools.toId();
    @ConfigField public static List<ResourceLocation> tileEntityWhitelist = Tools.toId("waterframes:all");

    @ConfigField public static AttachMode borderlessAttachModeF11 = AttachMode.ATTACH;
    @ConfigField public static boolean fastLanguageReload = true;

    @ConfigField public static boolean enableZoom = true;
    @ConfigField public static double maxZoom = 50;

    @ConfigField public static DynLightsSpeed dynLightSpeed = DynLightsSpeed.REALTIME;
    @ConfigField public static boolean dynLightsOnEntities = true;
    @ConfigField public static boolean dynLightsOnTileEntities = true;
    @ConfigField public static boolean dynLightsUpdateOnPositionChange = true;


    public static void setFullScreenMode(FullScreenMode value) {
        Minecraft client = Minecraft.getInstance();
        Options opts = client.options;

        fullScreen = value;
        opts.fullscreen().set(value != FullScreenMode.WINDOWED);

        Window window = client.getWindow();

        if (window.isFullscreen() != opts.fullscreen().get()) {
            window.toggleFullScreen();
            opts.fullscreen().set(window.isFullscreen());
        }

        if (opts.fullscreen().get()) {
            ((WindowAccessors) (Object) window).setDirty(true);
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
    public enum FPSDisplayAlign { LEFT, CENTER, RIGHT; }
    public enum FPSDisplayVAlign { TOP, CENTER, BOTTOM; }
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
        OFF(-1),
        DIM(0.18f),
        DARK(0.12f),
        DARKNESS(0.08f),
        BLACK(0.04f),
        BLACKNESS(0f);

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

    public static class ListResourceLocationAdapter implements JsonSerializer<List<ResourceLocation>>, JsonDeserializer<List<ResourceLocation>> {
        @Override
        public JsonElement serialize(List<ResourceLocation> src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.stream().map(ResourceLocation::toString).collect(Collectors.toList()));
        }

        @Override
        public List<ResourceLocation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray()) {
                throw new JsonParseException("Expected a JSON array for List<ResourceLocation>");
            }

            JsonArray jsonArray = json.getAsJsonArray();
            return jsonArray.asList().stream()
                    .map(e -> (ResourceLocation) context.deserialize(e, ResourceLocation.class))
                    .collect(Collectors.toList());
        }
    }

    private static final class ChlorideResourceLocationSerial implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {

        @Override
        public ResourceLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ResourceLocation.tryParse(jsonElement.getAsString().replace(":*", ":all"));
        }

        @Override
        public JsonElement serialize(ResourceLocation resourceLocation, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(resourceLocation.toString());
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
                    field.set(null, context.deserialize(element, field.getGenericType()));
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
