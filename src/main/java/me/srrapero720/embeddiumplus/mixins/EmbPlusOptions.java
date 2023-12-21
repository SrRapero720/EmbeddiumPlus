package me.srrapero720.embeddiumplus.mixins;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.Flywheel;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbPlusConfig;
import me.srrapero720.embeddiumplus.EmbPlusTools;
import me.srrapero720.embeddiumplus.api.EmbPlusAPI;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class EmbPlusOptions {


    public static Option<EmbPlusConfig.FullScreenMode> getFullscreenOption(MinecraftOptionsStorage options) {
        return OptionImpl.createBuilder(EmbPlusConfig.FullScreenMode.class, options)
                .setName(Component.translatable("embeddium.plus.options.screen.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, EmbPlusConfig.FullScreenMode.class, new Component[] {
                        Component.translatable("embeddium.plus.options.screen.windowed"),
                        Component.translatable("embeddium.plus.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(EmbPlusAPI::setFullScreenMode, (opts) -> EmbPlusConfig.fullScreenMode.get()).build();
    }

    public static ImmutableList<OptionGroup> getPlusOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        Option<EmbPlusConfig.Complexity> displayFps = OptionImpl.createBuilder(EmbPlusConfig.Complexity.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbPlusConfig.Complexity.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.simple"),
                        Component.translatable("embeddium.plus.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> EmbPlusConfig.fpsCounterMode.set(value),
                        (opts) -> EmbPlusConfig.fpsCounterMode.get())
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.position.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.position.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.translateVariable("embeddium.plus.options.common.pixels")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> EmbPlusConfig.fpsCounterPosition.set(value),
                        (opts) -> EmbPlusConfig.fpsCounterPosition.get())
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsPos)
                .build());


        OptionImpl<SodiumGameOptions, Boolean> totalDarkness = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.darkness.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.trueDarknessEnabled.set(value),
                        (options) -> EmbPlusConfig.trueDarknessEnabled.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> fastChest = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fastchest.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fastchest.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.fastChestsEnabled.set(value),
                        (options) -> EmbPlusConfig.fastChestsEnabled.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(EmbPlusTools.flwIsOff())
                .build();

        Option<EmbPlusConfig.DarknessMode> totalDarknessSetting = OptionImpl.createBuilder(EmbPlusConfig.DarknessMode.class, sodiumOpts)
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
                .add(fastChest)
                .add(totalDarknessSetting)
                .build());


        Option<EmbPlusConfig.FadeInQuality> fadeInQuality = OptionImpl.createBuilder(EmbPlusConfig.FadeInQuality.class, sodiumOpts)
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

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fog.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.fog.set(value),
                        (options) -> EmbPlusConfig.fog.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> hideJEI = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.jei.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.hideJEI.set(value),
                        (options) -> EmbPlusConfig.hideJEI.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(EmbPlusTools.isPresent("jei"))
                .build();

        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
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
                .add(hideJEI)
                .add(fadeInQuality)
                .add(fog)
                .add(cloudHeight)
                .build());


        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.enableDistanceChecks.set(value),
                        (options) -> EmbPlusConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> EmbPlusConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbPlusConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> EmbPlusConfig.maxEntityRenderDistanceY.set(value),
                        (options) -> EmbPlusConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );


        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> EmbPlusConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbPlusConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> EmbPlusConfig.maxTileEntityRenderDistanceY.set(value),
                        (options) -> EmbPlusConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );
        return ImmutableList.copyOf(groups);
    }

    // DYN LIGHTS
    private static final SodiumOptionsStorage dynLightsOptionsStorage = new SodiumOptionsStorage();
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
