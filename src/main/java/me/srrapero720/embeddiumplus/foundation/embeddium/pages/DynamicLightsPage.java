package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.dynlights.DynLightsPlus;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DynamicLightsPage extends OptionPage {
    private static final SodiumOptionsStorage dynLightsOptionsStorage = new SodiumOptionsStorage();

    public DynamicLightsPage() {
        super(Component.translatable("embeddium.plus.options.dynlights.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var qualityMode = OptionImpl.createBuilder(EmbyConfig.DynLightsSpeed.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.speed.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.DynLightsSpeed.class, new Component[] {
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


        final var entityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.entities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.entities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> {
                            EmbyConfig.dynLightsOnEntities.set(value);
                            EmbyConfig.dynLightsOnEntitiesCache = value;
                        },
                        (options) -> EmbyConfig.dynLightsOnEntitiesCache)
                .setImpact(OptionImpact.MEDIUM)
                .build();

        final var tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynLightsOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.dynlights.tiles.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> {
                            EmbyConfig.dynLightsOnTileEntities.set(value);
                            EmbyConfig.dynLightsOnEntitiesCache = value;
                        },
                        (options) -> EmbyConfig.dynLightsOnEntitiesCache)
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
