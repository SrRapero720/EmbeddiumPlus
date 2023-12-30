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

import me.srrapero720.embeddiumplus.internal.EmbyConfig.*;

import java.util.ArrayList;
import java.util.List;

public class EmbPlusPages {
    private static final SodiumOptionsStorage qualityOptionsStorage = new SodiumOptionsStorage();
    private static final SodiumOptionsStorage dynLightsOptionsStorage = new SodiumOptionsStorage();

    public static OptionPage getQualityPlusPage(){
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.fog.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fog.set(value);
                            EmbyConfig.fogCache = value;
                        },
                        (options) -> EmbyConfig.fogCache)
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ChunkFadeSpeed> fadeInQuality = OptionImpl.createBuilder(ChunkFadeSpeed.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.chunkfadeinquality.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.chunkfadeinquality.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChunkFadeSpeed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.chunkFadeSpeed.set(value),
                        (opts) -> EmbyConfig.chunkFadeSpeed.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .add(fadeInQuality)
                .build()
        );


        Option<DarknessMode> darknessMode = OptionImpl.createBuilder(DarknessMode.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.darkness.mode.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.mode.desc"))
                .setControl((option) -> new CyclingControl<>(option, DarknessMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.darkness.mode.pitchblack"),
                        Component.translatable("embeddium.plus.options.darkness.mode.reallydark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dim"),
                        Component.translatable("options.off")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.darknessMode.set(value),
                        (opts) -> EmbyConfig.darknessMode.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(darknessMode)
                .build()
        );



        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(int.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.clouds.height.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.clouds.height.desc"))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.cloudsHeight.set(value);
                            EmbyConfig.cloudsHeightCache = value;
                        },
                        (options) -> EmbyConfig.cloudsHeightCache)
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

        OptionImpl<SodiumGameOptions, DynLightsSpeed> qualityMode = OptionImpl.createBuilder(DynLightsSpeed.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.speed.desc"))
                .setControl((option) -> new CyclingControl<>(option, DynLightsSpeed.class, new Component[] {
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.slow"),
                        Component.translatable("embeddium.plus.options.common.normal"),
                        Component.translatable("embeddium.plus.options.common.fast"),
                        Component.translatable("embeddium.plus.options.common.superfast"),
                        Component.translatable("embeddium.plus.options.common.fastest"),
                        Component.translatable("embeddium.plus.options.common.realtime")
                }))
                .setBinding((options, value) -> {
                            EmbyConfig.dynLightSpeed.set(value);
                            DynLightsPlus.get().clearLightSources();
                        },
                        (options) -> EmbyConfig.dynLightSpeed.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.entities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.entities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.dynLightsOnEntities.set(value);
                            EmbyConfig.dynLightsOnEntitiesCache = value;
                        },
                        (options) -> EmbyConfig.dynLightsOnEntitiesCache)
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.blockentities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.blockentities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.dynLightsOnTileEntities.set(value);
                            EmbyConfig.dynLightsOnEntitiesCache = value;
                        },
                        (options) -> EmbyConfig.dynLightsOnEntitiesCache)
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
