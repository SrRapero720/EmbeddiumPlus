package me.srrapero720.chloride;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.chloride.impl.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static me.srrapero720.chloride.Chloride.LOGGER;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChlorideConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");
    private static final HashMap<String, Object> DEFAULTS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ChlorideResourceLocationSerial())
            .registerTypeAdapter(new TypeToken<List<ResourceLocation>>(){}.getType(), new ListResourceLocationAdapter())
            .registerTypeAdapter(ChlorideConfig.class, new ChlorideConfigSerial())
            .create();
    private static final ChlorideConfig DUMMY = new ChlorideConfig();
    private static File configFile;

    @ConfigField public static boolean modpackMode = false;
    @ConfigField public static Borderless.Mode fullScreen = Borderless.Mode.WINDOWED;
    @ConfigField public static boolean disableBorderlessOptimizations = false;
    @ConfigField public static Overlay.FPS fpsDisplayMode = Overlay.FPS.ADVANCED;
    @ConfigField public static Overlay.FPSAlign fpsDisplayAlign = Overlay.FPSAlign.LEFT;
    @ConfigField public static Overlay.FPSVAlign fpsDisplayVAlign = Overlay.FPSVAlign.TOP;
    @ConfigField public static Overlay.FPSDetails fpsDisplaySystemMode = Overlay.FPSDetails.OFF;
    @ConfigField public static int fpsDisplayMargin = 12;
    @ConfigField public static int fpsDisplayVMargin = 12;
    @ConfigField public static boolean fpsDisplayShadow = false;

    @ConfigField public static boolean fog = true;
    @ConfigField public static boolean fogOnOverworld = true;
    @ConfigField public static boolean fogOnNether = true;
    @ConfigField public static boolean fogOnEnd = true;

    @ConfigField public static boolean blueBand = true;
    @ConfigField public static boolean customFog = false;
    @ConfigField public static int fogStart = 0;
    @ConfigField public static int fogEnd = 192;
    @ConfigField public static FogShape fogShape = FogShape.CYLINDER;
    @ConfigField public static int cloudsHeight = 192;
    @ConfigField public static boolean entityNametagRendering = true;
    @ConfigField public static boolean playerNametagRendering = true;
    @ConfigField public static boolean itemNametagRendering = true;
    @ConfigField public static ChunkFade.Speed chunkFadeSpeed = ChunkFade.Speed.SLOW;

    @ConfigField public static Darkness.DarkMode darknessMode = Darkness.DarkMode.VANILLA;
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
    @ConfigField public static boolean hideJREMIHint = false;
    @ConfigField public static boolean fontShadows = true;
    @ConfigField public static LeavesCulling.LeavesCullingMode leavesCulling = LeavesCulling.LeavesCullingMode.OFF;
    @ConfigField public static boolean fastChests = false;
    @ConfigField public static boolean fastBeds = false;

    @ConfigField public static boolean rainParticles = true;
    @ConfigField public static boolean rainDropParticles = true;
    @ConfigField public static boolean crackingBlockParticles = true;
    @ConfigField public static boolean destroyedBlockParticles = true;
    @ConfigField public static List<ResourceLocation> disabledParticles = toId();

    @ConfigField public static boolean tileEntityDistanceCulling = true;
    @ConfigField public static int tileEntityCullingDistanceX = 4096;
    @ConfigField public static int tileEntityCullingDistanceY = 32;
    @ConfigField public static boolean entityDistanceCulling = true;
    @ConfigField public static int entityLimit = 512;
    @ConfigField public static int entityCullingDistanceX = 4096;
    @ConfigField public static int entityCullingDistanceY = 32;
    @ConfigField public static boolean monsterDistanceCulling = false;
    @ConfigField public static int monsterCullingDistanceX = 16384;
    @ConfigField public static int monsterCullingDistanceY = 64;
    @ConfigField public static List<ResourceLocation> entityWhitelist = toId("minecraft:ghast", "minecraft:ender_dragon", "iceandfire:all", "create:all");
    @ConfigField public static List<ResourceLocation> monsterWhitelist = toId();
    @ConfigField public static List<ResourceLocation> tileEntityWhitelist = toId("waterframes:all");

    @ConfigField public static Borderless.AttachMode borderlessAttachModeF11 = Borderless.AttachMode.ATTACH;
    @ConfigField public static boolean fastLanguageReload = true;

    @ConfigField public static boolean enableZoom = true;
    @ConfigField public static double maxZoom = 50;


    public static List<ResourceLocation> toId(final String... ids) {
        final List<ResourceLocation> result = new ArrayList<>();
        for (String id: ids) {
            if (id.endsWith(":*")) id = id.replace(":*", ":all");
            result.add(ResourceLocation.tryParse(id));
        }

        return result;
    }

    static void load(final Path configPath) {
        configFile = configPath.resolve("chloride-client.json").toFile();
        for (Field field: ChlorideConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(ConfigField.class))
                continue;

            try {
                field.setAccessible(true);
                DEFAULTS.put(field.getName(), field.get(null));
            } catch (final IllegalAccessException e) {
                LOGGER.error(IT,"Cannot access field: {}", field.getName(), e);
            }
        }
        if (!configFile.exists()) {
            write();
        } else {
            read();
            write();
        }

        // Ensure no field is null
        for (final Field field: ChlorideConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(ConfigField.class))
                continue;

            try {
                field.setAccessible(true);
                if (field.get(null) == null) {
                    LOGGER.warn(IT, "Field {} is null, setting to default value", field.getName());
                    field.set(null, DEFAULTS.get(field.getName()));
                }
            } catch (final IllegalAccessException e) {
                LOGGER.error(IT,"Cannot access field: {}", field.getName(), e);
            }
        }
    }

    public static void write() {
        try (final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(configFile))) {
            out.write(GSON.toJson(DUMMY).getBytes(StandardCharsets.UTF_8));
        } catch (final Exception e) {
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

    @Retention(RetentionPolicy.RUNTIME)
    @interface ConfigField {

    }

    public static class ListResourceLocationAdapter implements JsonSerializer<List<ResourceLocation>>, JsonDeserializer<List<ResourceLocation>> {
        @Override
        public JsonElement serialize(final List<ResourceLocation> src, final Type typeOfSrc, final JsonSerializationContext context) {
            return context.serialize(src.stream().map(ResourceLocation::toString).collect(Collectors.toList()));
        }

        @Override
        public List<ResourceLocation> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray()) {
                throw new JsonParseException("Expected a JSON array for List<ResourceLocation>");
            }

            final JsonArray jsonArray = json.getAsJsonArray();
            return jsonArray.asList().stream()
                    .map(e -> (ResourceLocation) context.deserialize(e, ResourceLocation.class))
                    .collect(Collectors.toList());
        }
    }

    private static final class ChlorideResourceLocationSerial implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {

        @Override
        public ResourceLocation deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ResourceLocation.tryParse(jsonElement.getAsString().replace(":*", ":all"));
        }

        @Override
        public JsonElement serialize(final ResourceLocation resourceLocation, final Type type, final JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(resourceLocation.toString());
        }
    }

    private static final class ChlorideConfigSerial implements JsonSerializer<ChlorideConfig>, JsonDeserializer<ChlorideConfig> {
        @Override
        public JsonElement serialize(final ChlorideConfig src, final Type typeOfSrc, final JsonSerializationContext context) {
            final JsonObject jsonObject = new JsonObject();

            for (final Field field: ChlorideConfig.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(ConfigField.class))
                    continue;

                try {
                    field.setAccessible(true);
                    jsonObject.add(field.getName(), context.serialize(field.get(null)));
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException("Error al acceder al campo: " + field.getName(), e);
                }
            }

            return jsonObject;
        }

        @Override
        public ChlorideConfig deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            for (final Field field: ChlorideConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigField.class) || !Modifier.isStatic(field.getModifiers()))
                    continue;

                try {
                    final JsonElement element = jsonObject.get(field.getName());
                    if (element == null || element.isJsonNull()) {
                        if (DEFAULTS.containsKey(field.getName())) {
                            field.set(null, DEFAULTS.get(field.getName()));
                            continue;
                        } else {
                            throw new JsonParseException("Missing default field: " + field.getName());
                        }
                    }

                    field.setAccessible(true);
                    field.set(null, context.deserialize(element, field.getGenericType()));
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException("Error al asignar el valor al campo: " + field.getName(), e);
                }
            }

            return new ChlorideConfig(); // Devuelve una instancia vacía; solo usa campos estáticos
        }
    }
}
