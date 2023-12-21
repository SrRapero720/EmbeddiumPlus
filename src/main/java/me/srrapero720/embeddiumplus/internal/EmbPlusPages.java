package me.srrapero720.embeddiumplus.internal;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class EmbPlusPages {
    private static final SodiumOptionsStorage qualityOptionsStorage = new SodiumOptionsStorage();
    // DYN LIGHTS
    private static final SodiumOptionsStorage dynLightsOptionsStorage = new SodiumOptionsStorage();

    public static OptionPage getQualityPlusPage(){
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.fog.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.fog.set(value),
                        (options) -> EmbPlusConfig.fog.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<EmbPlusConfig.FadeInQuality> fadeInQuality = OptionImpl.createBuilder(EmbPlusConfig.FadeInQuality.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.chunkfadeinquality.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.chunkfadeinquality.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbPlusConfig.FadeInQuality.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding(
                        (opts, value) -> EmbPlusConfig.fadeInQuality.set(value),
                        (opts) -> EmbPlusConfig.fadeInQuality.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .add(fadeInQuality)
                .build()
        );

        OptionImpl<SodiumGameOptions, Boolean> totalDarkness = OptionImpl.createBuilder(Boolean.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.darkness.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.trueDarknessEnabled.set(value),
                        (options) -> EmbPlusConfig.trueDarknessEnabled.get())
                .setImpact(OptionImpact.LOW)
                .build();



        Option<EmbPlusConfig.DarknessMode> totalDarknessSetting = OptionImpl.createBuilder(EmbPlusConfig.DarknessMode.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.darkness.mode.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.mode.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbPlusConfig.DarknessMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.darkness.mode.pitchblack"),
                        Component.translatable("embeddium.plus.options.darkness.mode.reallydark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dim")
                }))
                .setBinding(
                        (opts, value) -> EmbPlusConfig.darknessOption.set(value),
                        (opts) -> EmbPlusConfig.darknessOption.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(totalDarkness)
                .add(totalDarknessSetting)
                .build()
        );



        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(Integer.TYPE, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.clouds.height.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.clouds.height.desc"))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            EmbPlusConfig.cloudHeight.set(value);
                        },
                        (options) -> EmbPlusConfig.cloudHeight.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(cloudHeight)
                .build()
        );


        return new OptionPage(Component.translatable("sodium.options.pages.quality").append("++"), ImmutableList.copyOf(groups));
    }

    public static OptionPage getDynLightsPage() {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, EmbPlusConfig.DynamicLightsQuality> qualityMode = OptionImpl.createBuilder(EmbPlusConfig.DynamicLightsQuality.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.speed.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbPlusConfig.DynamicLightsQuality.class, new Component[] {
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.slow"),
                        Component.translatable("embeddium.plus.options.common.fast"),
                        Component.translatable("embeddium.plus.options.common.faster"),
                        Component.translatable("embeddium.plus.options.common.realtime")
                }))
                .setBinding((options, value) -> {
                            EmbPlusConfig.dynQuality.set(value);
                            DynLightsPlus.get().clearLightSources();
                        },
                        (options) -> EmbPlusConfig.dynQuality.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.entities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.entities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.entityLighting.set(value),
                        (options) -> EmbPlusConfig.entityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.blockentities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.blockentities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.tileEntityLighting.set(value),
                        (options) -> EmbPlusConfig.tileEntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
                .build()
        );

        return new OptionPage(Component.translatable("embeddium.plus.options.dynlights.group"), ImmutableList.copyOf(groups));
    }
}
