package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.dynlights.DynLightsPlus;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class QualityPlusPage extends OptionPage {
    private static final SodiumOptionsStorage qualityOptionsStorage = new SodiumOptionsStorage();

    public QualityPlusPage() {
        super(Component.translatable("sodium.options.pages.quality").append("++"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();


        final var fog = OptionImpl.createBuilder(Boolean.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.fog.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> {
                            EmbyConfig.fog.set(value);
                            EmbyConfig.fogCache = value;
                        },
                        (options) -> EmbyConfig.fogCache)
                .setImpact(OptionImpact.LOW)
                .build();

        final var fadeInQuality = OptionImpl.createBuilder(EmbyConfig.ChunkFadeSpeed.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.chunkfadeinquality.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.chunkfadeinquality.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.ChunkFadeSpeed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding((opts, value) -> EmbyConfig.chunkFadeSpeed.set(value),
                        (opts) -> EmbyConfig.chunkFadeSpeed.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .add(fadeInQuality)
                .build()
        );


        final var darknessMode = OptionImpl.createBuilder(EmbyConfig.DarknessMode.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.darkness.mode.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.mode.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.DarknessMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.darkness.mode.pitchblack"),
                        Component.translatable("embeddium.plus.options.darkness.mode.reallydark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dim"),
                        Component.translatable("options.off")
                }))
                .setBinding((opts, value) -> EmbyConfig.darknessMode.set(value),
                        (opts) -> EmbyConfig.darknessMode.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(darknessMode)
                .build()
        );

        final var cloudHeight = OptionImpl.createBuilder(int.class, qualityOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.clouds.height.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.clouds.height.desc"))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding((options, value) -> {
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

        return ImmutableList.copyOf(groups);
    }
}
