package me.srrapero720.chloride;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Collections;
import java.util.List;

import static me.srrapero720.chloride.Chloride.LOGGER;
import static me.srrapero720.chloride.ChlorideConfig.*;

public class ChlorideConfig_Old {
    public static final Marker IT = MarkerManager.getMarker("Config");
    public static ForgeConfigSpec SPECS;

    public static void tryRestore() {
        final var path = FMLPaths.CONFIGDIR.get().resolve("embeddium++.toml").toFile();
        if (!path.exists()) return;
        
        LOGGER.warn(IT, "Restoring config from old Embeddium++Config");

        // FORCE LOAD
        try {
            final var configData = CommentedFileConfig.builder(path).sync().writingMode(WritingMode.REPLACE).build();
            configData.load();

            // SPEC DEFINE
            final var BUILDER = new ForgeConfigSpec.Builder();

            // embeddiumplus ->
            BUILDER.push("embeddiumplus");

            // embeddiumplus -> general ->
            BUILDER.push("general");
            // GENERAL
            final ForgeConfigSpec.EnumValue<FullScreenMode> fullScreen = BUILDER
                    .comment("Set Fullscreen mode", "Borderless let you change between screens more faster and move your mouse across monitors")
                    .defineEnum("fullscreen", FullScreenMode.WINDOWED);

            final ForgeConfigSpec.EnumValue<FPSDisplayMode> fpsDisplayMode = BUILDER
                    .comment("Configure FPS Display mode", "Complete mode gives you min FPS count and average count")
                    .defineEnum("fpsDisplay", FPSDisplayMode.ADVANCED);

            final ForgeConfigSpec.EnumValue<FPSDisplayAlign> fpsDisplayGravity = BUILDER
                    .comment("Configure FPS Display gravity", "Places counter on specified corner of your screen")
                    .defineEnum("fpsDisplayGravity", FPSDisplayAlign.LEFT);

            final ForgeConfigSpec.EnumValue<FPSDisplaySystemMode> fpsDisplaySystemMode = BUILDER
                    .comment("Shows GPU and memory usage onto FPS display")
                    .defineEnum("fpsDisplaySystem", FPSDisplaySystemMode.OFF);

            final ForgeConfigSpec.IntValue fpsDisplayMargin = BUILDER
                    .comment("Configure FPS Display margin", "Give some space between corner and text")
                    .defineInRange("fpsDisplayMargin", 12, 0, 48);

            final ForgeConfigSpec.BooleanValue fpsDisplayShadow = BUILDER
                    .comment("Toggle FPS Display shadow", "In case sometimes you can't see the text")
                    .define("fpsDisplayShadow", false);

            // embeddiumplus ->
            BUILDER.pop();

            // embeddiumplus -> quality
            BUILDER.push("quality");
            // QUALITY
            final ForgeConfigSpec.BooleanValue fog = BUILDER
                    .comment("Toggle fog feature", "Fog was a vanilla feature")
                    .define("fog", true);
            final ForgeConfigSpec.BooleanValue blueBand = BUILDER
                    .comment("Clean my skies", "Blue band was a vanilla feature, toggle off will show sky color directly")
                    .define("blueBand", true);
            final ForgeConfigSpec.IntValue cloudsHeight = BUILDER
                    .comment("Raise clouds", "Modify clouds height perfect for a adaptative world experience")
                    .defineInRange("cloudsHeight", 192, 0, 512);

            final ForgeConfigSpec.BooleanValue disableNameTagRender = BUILDER
                    .comment("Do not show me your name", "disables nametag rendering for players and entities")
                    .define("disableNameTagRendering", false);

            final ForgeConfigSpec.EnumValue<ChunkFadeSpeed> chunkFadeSpeed = BUILDER
                    .comment("Chunks fade in speed", "This option doesn't affect performance, just changes speed")
                    .defineEnum("chunkFadeSpeed", ChunkFadeSpeed.SLOW);

            // embeddiumplus -> quality -> darkness
            BUILDER.push("darkness");
            // QUALITY: TRUE DARKNESS
            final ForgeConfigSpec.EnumValue<DarknessMode> darknessMode = BUILDER
                    .comment("Configure Darkness Mode", "Each config changes what is considered 'true darkness'")
                    .defineEnum("mode", DarknessMode.VANILLA);

            final ForgeConfigSpec.BooleanValue darknessOnOverworld = BUILDER
                    .comment("Toggle Darkness on Overworld dimension")
                    .define("enableOnOverworld", true);

            final ForgeConfigSpec.BooleanValue darknessOnNether = BUILDER
                    .comment("Toggle Darkness on Nether dimension")
                    .define("enableOnNether", false);

            final ForgeConfigSpec.DoubleValue darknessNetherFogBright = BUILDER
                    .comment("Configure fog brightness on nether when darkness is enabled")
                    .defineInRange("netherFogBright", 0.5f, 0d, 1d);

            final ForgeConfigSpec.BooleanValue darknessOnEnd = BUILDER
                    .comment("Toggle Darkness on End dimension")
                    .define("enableOnEnd", false);

            final ForgeConfigSpec.DoubleValue darknessEndFogBright = BUILDER
                    .comment("Configure fog brightness on nether when darkness is enabled")
                    .defineInRange("endFogBright", 0.5f, 0d, 1d);

            final ForgeConfigSpec.BooleanValue darknessByDefault = BUILDER
                    .comment("Toggle Darkness default mode for modded dimensions")
                    .define("valueByDefault", false);

            final ForgeConfigSpec.ConfigValue<List<? extends String>> darknessDimensionWhiteList = BUILDER
                    .comment("List of all dimensions to use True Darkness", "This option overrides 'valueByDefault' state")
                    .defineListAllowEmpty(Collections.singletonList("dimensionWhitelist"), Collections::emptyList, s -> s.toString().contains(":"));

            final ForgeConfigSpec.BooleanValue darknessOnNoSkyLight = BUILDER
                    .comment("Toggle darkness when dimension has no SkyLight")
                    .define("enableOnNoSkyLight", false);

            final ForgeConfigSpec.BooleanValue darknessBlockLightOnly = BUILDER
                    .comment("Disables all bright sources of darkness like moon or fog", "Only affects darkness effect")
                    .define("enableBlockLightOnly", false);

            final ForgeConfigSpec.BooleanValue darknessAffectedByMoonPhase = BUILDER
                    .comment("Toggles if moon phases affects darkness in the overworld")
                    .define("affectedByMoonPhase", true);

            final ForgeConfigSpec.DoubleValue darknessFullMoonBright = BUILDER
                    .comment("Configure max moon brightness level with darkness")
                    .defineInRange("fullMoonBright", 0.25d, 0, 1d);

            final ForgeConfigSpec.DoubleValue darknessNewMoonBright = BUILDER
                    .comment("Configure min moon brightness level with darkness")
                    .defineInRange("newMoonBright", 0, 0, 1d);


            // embeddiumplus ->
            BUILDER.pop(2);

            // embeddiumplus -> performance
            BUILDER.push("performance");

            final ForgeConfigSpec.EnumValue<LeavesCullingMode> leavesCulling = BUILDER
                    .comment("Sets culling mode", "Reduces number of visible faces when the neighbor blocks are leaves")
                    .defineEnum("leavesCulling", LeavesCullingMode.OFF);

            // PERFORMANCE;
            final ForgeConfigSpec.BooleanValue hideJREMI = BUILDER
                    .comment("Toggles JREI item rendering until searching", "Increases performance a little bit and cleans your screen when you don't want to use it")
                    .define("hideJREI", false);

            final ForgeConfigSpec.BooleanValue fontShadows = BUILDER
                    .comment("Toggles Minecraft Fonts shadows", "Depending of the case may increase performance", "Gives a flat style text")
                    .define("fontShadows", true);

            // embeddiumplus -> performance -> fastModels
            BUILDER.push("fastModels");
            final ForgeConfigSpec.BooleanValue fastChests = BUILDER
                    .comment("Toggles FastChest feature", "Without flywheel installed or using any backend, it increases FPS significatly on chest rooms")
                    .define("enableChests", false);

            final ForgeConfigSpec.BooleanValue fastBeds = BUILDER
                    .comment("Toggles FastBeds feature")
                    .define("enableBeds", false);

            // embeddiumplus -> performance
            BUILDER.pop();

            // embeddiumplus -> performance -> distanceCulling
            BUILDER.push("distanceCulling");

            // embeddiumplus -> performance -> distanceCulling -> tileEntities
            BUILDER.push("tileEntities");
            final ForgeConfigSpec.BooleanValue tileEntityDistanceCulling = BUILDER
                    .comment("Toggles distance culling for Block Entities", "Maybe you use another mod for that :(")
                    .define("enable", true);

            final ForgeConfigSpec.IntValue tileEntityCullingDistanceX = BUILDER
                    .comment("Configure horizontal max distance before cull Block entities", "Value is squared, default was 64^2 (or 64x64)")
                    .defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);

            final ForgeConfigSpec.IntValue tileEntityCullingDistanceY = BUILDER
                    .comment("Configure vertical max distance before cull Block entities", "Value is raw")
                    .defineInRange("cullingMaxDistanceY", 32, 0, 512);

            // QUICK CHECK
            final ForgeConfigSpec.ConfigValue<List<? extends String>> tileEntityWhitelist = BUILDER
                    .comment("List of all Block Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:chest\" - Ignores chests only", "Example 2: \"ae2:all\" - ignores all Block entities from Applied Energetics 2")
                    .defineListAllowEmpty(Collections.singletonList("whitelist"), Collections.emptyList(), s -> s.toString().contains(":"));

            // embeddiumplus -> performance -> distanceCulling ->
            BUILDER.pop();

            // embeddiumplus -> performance -> distanceCulling -> entities
            BUILDER.push("entities");
            final ForgeConfigSpec.BooleanValue entityDistanceCulling = BUILDER
                    .comment("Toggles distance culling for entities, doesn't affect monsters culling", "Check the options below")
                    .define("enable", true);
            final ForgeConfigSpec.IntValue entityCullingDistanceX = BUILDER
                    .comment("Configure horizontal max distance before cull entities", "Value is squared, default was 64^2 (or 64x64)")
                    .defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);

            final ForgeConfigSpec.IntValue entityCullingDistanceY = BUILDER
                    .comment("Configure vertical max distance before cull entities", "Value is raw")
                    .defineInRange("cullingMaxDistanceY", 32, 0, 512);

            // QUICK CHECK
            final ForgeConfigSpec.ConfigValue<List<? extends String>> entityWhitelist = BUILDER
                    .comment("List of all Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod")
                    .defineListAllowEmpty(Collections.singletonList("whitelist"), Collections.emptyList(), (s) -> s.toString().contains(":"));

            // embeddiumplus -> performance -> distanceCulling -> entities -> monsters
            BUILDER.push("monsters");
            final ForgeConfigSpec.BooleanValue monsterDistanceCulling = BUILDER
                    .comment("Toggles distance culling for monsters (or hostile entities, whatever you want to call it), doesn't affect neutral/pacific entities", "Check the options above")
                    .define("enable", false);

            final ForgeConfigSpec.IntValue monsterCullingDistanceX = BUILDER
                    .comment("Configure horizontal max distance before cull monster entities", "Value is squared, default was 64^2 (or 64x64)")
                    .defineInRange("cullingMaxDistanceX", 16384, 0, Integer.MAX_VALUE);

            final ForgeConfigSpec.IntValue monsterCullingDistanceY = BUILDER
                    .comment("Configure vertical max distance before cull monster entities", "Value is raw")
                    .defineInRange("cullingMaxDistanceY", 64, 0, 512);

            // QUICK CHECK
            final ForgeConfigSpec.ConfigValue<List<? extends String>> monsterWhitelist = BUILDER
                    .comment("List of all monster entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod")
                    .defineListAllowEmpty(Collections.singletonList("whitelist"), Collections.emptyList(), (s) -> s.toString().contains(":"));

            // embeddiumplus ->
            BUILDER.pop(4);

            // embeddiumplus -> others
            BUILDER.push("others");
            // OTHERS
            final ForgeConfigSpec.EnumValue<AttachMode> borderlessAttachModeF11 = BUILDER
                    .comment("Configure if borderless fullscreen option should be attached to F11 or replace vanilla fullscreen")
                    .defineEnum("borderlessAttachModeOnF11", AttachMode.ATTACH);
            final ForgeConfigSpec.BooleanValue fastLanguageReload = BUILDER
                    .comment("Toggles fast language reload", "Embeddedt points it maybe cause troubles to JEI, so ¿why not add it as a toggleable option?")
                    .define("fastLanguageReload", true);

            BUILDER.pop();

            // embeddiumplus -> dynlights
            BUILDER.push("dynlights");
            // DYN LIGHTS
            final ForgeConfigSpec.EnumValue<DynLightsSpeed> dynLightSpeed = BUILDER
                    .comment("Configure how fast light whould be updated")
                    .defineEnum("updateSpeed", DynLightsSpeed.REALTIME);

            final ForgeConfigSpec.BooleanValue dynLightsOnEntities = BUILDER
                    .comment("Toggle if Entities should have dynamic lights")
                    .define("onEntities", true);

            final ForgeConfigSpec.BooleanValue dynLightsOnTileEntities = BUILDER
                    .comment("Toggle if Block Entities should have dynamic lights")
                    .define("onTileEntities", true);

            final ForgeConfigSpec.BooleanValue dynLightsUpdateOnPositionChange = BUILDER
                    .define("updateOnlyOnPositionChange", true);

            // embeddiumplus ->
            BUILDER.pop();

            SPECS = BUILDER.build();
            SPECS.setConfig(configData);

            LOGGER.info(IT,"Porting old config");



            ChlorideConfig.fullScreen = fullScreen.get();
            ChlorideConfig.fpsDisplayMode = fpsDisplayMode.get();
            ChlorideConfig.fpsDisplayAlign = fpsDisplayGravity.get();
            ChlorideConfig.fpsDisplaySystemMode = fpsDisplaySystemMode.get();
            ChlorideConfig.fpsDisplayMargin = fpsDisplayMargin.get();
            ChlorideConfig.fpsDisplayShadow = fpsDisplayShadow.get();

            ChlorideConfig.fog = fog.get();
            ChlorideConfig.blueBand = blueBand.get();
            ChlorideConfig.cloudsHeight = cloudsHeight.get();
            ChlorideConfig.entityNametagRendering = disableNameTagRender.get();
            ChlorideConfig.chunkFadeSpeed = chunkFadeSpeed.get();

            ChlorideConfig.darknessMode = darknessMode.get();
            ChlorideConfig.darknessOnOverworld = darknessOnOverworld.get();
            ChlorideConfig.darknessOnNether = darknessOnNether.get();
            ChlorideConfig.darknessNetherFogBright = darknessNetherFogBright.get();
            ChlorideConfig.darknessOnEnd = darknessOnEnd.get();
            ChlorideConfig.darknessEndFogBright = darknessEndFogBright.get();
            ChlorideConfig.darknessByDefault = darknessByDefault.get();
            ChlorideConfig.darknessDimensionWhiteList = Tools.toId(darknessDimensionWhiteList.get().toArray(new String[0]));
            ChlorideConfig.darknessOnNoSkyLight = darknessOnNoSkyLight.get();
            ChlorideConfig.darknessBlockLightOnly = darknessBlockLightOnly.get();
            ChlorideConfig.darknessAffectedByMoonPhase = darknessAffectedByMoonPhase.get();
            ChlorideConfig.darknessNewMoonBright = darknessNewMoonBright.get();
            ChlorideConfig.darknessFullMoonBright = darknessFullMoonBright.get();

            ChlorideConfig.hideJREMI = hideJREMI.get();
            ChlorideConfig.fontShadows = fontShadows.get();
            ChlorideConfig.leavesCulling = leavesCulling.get();
            ChlorideConfig.fastChests = fastChests.get();
            ChlorideConfig.fastBeds = fastBeds.get();

            ChlorideConfig.tileEntityDistanceCulling = tileEntityDistanceCulling.get();
            ChlorideConfig.tileEntityCullingDistanceX = tileEntityCullingDistanceX.get();
            ChlorideConfig.tileEntityCullingDistanceY = tileEntityCullingDistanceY.get();
            ChlorideConfig.entityDistanceCulling = entityDistanceCulling.get();
            ChlorideConfig.entityCullingDistanceX = entityCullingDistanceX.get();
            ChlorideConfig.entityCullingDistanceY = entityCullingDistanceY.get();
            ChlorideConfig.monsterDistanceCulling = monsterDistanceCulling.get();
            ChlorideConfig.monsterCullingDistanceX = monsterCullingDistanceX.get();
            ChlorideConfig.monsterCullingDistanceY = monsterCullingDistanceY.get();
            ChlorideConfig.entityWhitelist = Tools.toId(entityWhitelist.get().toArray(new String[0]));
            ChlorideConfig.monsterWhitelist = Tools.toId(monsterWhitelist.get().toArray(new String[0]));
            ChlorideConfig.tileEntityWhitelist = Tools.toId(tileEntityWhitelist.get().toArray(new String[0]));

            ChlorideConfig.borderlessAttachModeF11 = borderlessAttachModeF11.get();
            ChlorideConfig.fastLanguageReload = fastLanguageReload.get();

            ChlorideConfig.dynLightSpeed = dynLightSpeed.get();
            ChlorideConfig.dynLightsOnEntities = dynLightsOnEntities.get();
            ChlorideConfig.dynLightsOnTileEntities = dynLightsOnTileEntities.get();
            ChlorideConfig.dynLightsUpdateOnPositionChange = dynLightsUpdateOnPositionChange.get();

            LOGGER.info(IT,"Chloride config successfully adjusted by old Embeddium++Config");

            configData.close();
            if (!path.delete()) LOGGER.error(IT, "Cannot delete old config file '{}', it requires manual deletion", path);
        } catch (final Exception e) {
            if (!path.delete()) throw new RuntimeException("Failed to remove corrupted configuration file");
        }
    }
}
