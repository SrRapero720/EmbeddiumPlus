package me.srrapero720.chloride.impl.sodium.pages;

import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.ChunkFade;
import me.srrapero720.chloride.impl.LeavesCulling;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class WorldPage {
    private WorldPage() {}

    public static OptionPageBuilder build(final ConfigBuilder b) {
        final OptionPageBuilder page = b.createOptionPage().setName(Component.translatable("chloride.world"));

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("blueBand"))
                        .setName(Component.translatable("chloride.world.blueband.title"))
                        .setTooltip(Component.translatable("chloride.world.blueband.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.blueBand = v, () -> ChlorideConfig.blueBand))
        );

        if (!ChlorideConfig.modpackMode) {
            page.addOptionGroup(b.createOptionGroup()
                    .addOption(b.createBooleanOption(Chloride.id("fog"))
                            .setName(Component.translatable("chloride.world.fog.title"))
                            .setTooltip(Component.translatable("chloride.world.fog.desc"))
                            .setStorageHandler(STORAGE)
                            .setImpact(OptionImpact.LOW)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.fog = v, () -> ChlorideConfig.fog))
                    .addOption(b.createBooleanOption(Chloride.id("fogOnOverworld"))
                            .setName(Component.translatable("chloride.world.fog.overworld.title"))
                            .setTooltip(Component.translatable("chloride.world.fog.overworld.desc"))
                            .setStorageHandler(STORAGE)
                            .setImpact(OptionImpact.LOW)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.fogOnOverworld = v, () -> ChlorideConfig.fogOnOverworld))
                    .addOption(b.createBooleanOption(Chloride.id("fogOnNether"))
                            .setName(Component.translatable("chloride.world.fog.nether.title"))
                            .setTooltip(Component.translatable("chloride.world.fog.nether.desc"))
                            .setStorageHandler(STORAGE)
                            .setImpact(OptionImpact.LOW)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.fogOnNether = v, () -> ChlorideConfig.fogOnNether))
                    .addOption(b.createBooleanOption(Chloride.id("fogOnEnd"))
                            .setName(Component.translatable("chloride.world.fog.end.title"))
                            .setTooltip(Component.translatable("chloride.world.fog.end.desc"))
                            .setStorageHandler(STORAGE)
                            .setImpact(OptionImpact.LOW)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.fogOnEnd = v, () -> ChlorideConfig.fogOnEnd))
                    .addOption(b.createBooleanOption(Chloride.id("customFog"))
                            .setName(Component.translatable("chloride.world.custom_fog.title"))
                            .setTooltip(Component.translatable("chloride.world.custom_fog.desc"))
                            .setStorageHandler(STORAGE)
                            .setImpact(OptionImpact.LOW)
                            .setDefaultValue(false)
                            .setBinding(v -> ChlorideConfig.customFog = v, () -> ChlorideConfig.customFog))
                    .addOption(b.createIntegerOption(Chloride.id("fogStart"))
                            .setName(Component.translatable("chloride.world.custom_fog.start.title"))
                            .setTooltip(Component.translatable("chloride.world.custom_fog.start.desc"))
                            .setValueFormatter(NUMBER)
                            .setRange(-1000, 1000, 10)
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(0)
                            .setBinding(v -> ChlorideConfig.fogStart = v, () -> ChlorideConfig.fogStart))
                    .addOption(b.createIntegerOption(Chloride.id("fogEnd"))
                            .setName(Component.translatable("chloride.world.custom_fog.end.title"))
                            .setTooltip(Component.translatable("chloride.world.custom_fog.end.desc"))
                            .setValueFormatter(NUMBER)
                            .setRange(100, 10000, 50)
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(192)
                            .setBinding(v -> ChlorideConfig.fogEnd = v, () -> ChlorideConfig.fogEnd))
                    .addOption(b.createEnumOption(Chloride.id("fogShape"), FogShape.class)
                            .setName(Component.translatable("chloride.world.custom_fog.shape.title"))
                            .setTooltip(Component.translatable("chloride.world.custom_fog.shape.desc"))
                            .setElementNameProvider(enumNames("chloride.world.custom_fog.shape"))
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(FogShape.CYLINDER)
                            .setBinding(v -> ChlorideConfig.fogShape = v, () -> ChlorideConfig.fogShape))
            );
        }

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createEnumOption(Chloride.id("leavesCulling"), LeavesCulling.LeavesCullingMode.class)
                        .setName(Component.translatable("chloride.world.leaves_culling.title"))
                        .setTooltip(Component.translatable("chloride.world.leaves_culling.desc"))
                        .setElementNameProvider(enumNames("chloride.world.leaves_culling"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setDefaultValue(LeavesCulling.LeavesCullingMode.OFF)
                        .setBinding(v -> ChlorideConfig.leavesCulling = v, () -> ChlorideConfig.leavesCulling))
                .addOption(b.createIntegerOption(Chloride.id("cloudsHeight"))
                        .setName(Component.translatable("chloride.world.clouds.height.title"))
                        .setTooltip(Component.translatable("chloride.world.clouds.height.desc"))
                        .setValueFormatter(BLOCKS)
                        .setRange(64, 364, 4)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(192)
                        .setBinding(v -> ChlorideConfig.cloudsHeight = v, () -> ChlorideConfig.cloudsHeight))
        );

        // World "amazings" (chunk fade) — kept disabled, mirrors the previous behaviour.
        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createEnumOption(Chloride.id("chunkFadeSpeed"), ChunkFade.Speed.class)
                        .setName(Component.translatable("chloride.world.fade.title"))
                        .setTooltip(Component.translatable("chloride.world.fade.desc"))
                        .setElementNameProvider(EnumOptionBuilder.nameProviderFrom(
                                Component.translatable("options.off"),
                                Component.translatable("options.graphics.fast"),
                                Component.translatable("options.graphics.fancy")))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.LOW)
                        .setEnabled(false)
                        .setDefaultValue(ChunkFade.Speed.SLOW)
                        .setBinding(v -> ChlorideConfig.chunkFadeSpeed = v, () -> ChlorideConfig.chunkFadeSpeed))
        );

        return page;
    }
}
