package me.srrapero720.embeddiumplus.foundation.embeddium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.network.chat.Component;

import me.srrapero720.embeddiumplus.EmbyConfig.*;

import java.util.List;

public class EmbPlusOptions {
    public static Option<FullScreenMode> getFullscreenOption(MinecraftOptionsStorage options) {
        return OptionImpl.createBuilder(FullScreenMode.class, options)
                .setName(Component.translatable("embeddium.plus.options.screen.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("embeddium.plus.options.screen.windowed"),
                        Component.translatable("embeddium.plus.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(EmbyConfig::setFullScreenMode, (opts) -> EmbyConfig.fullScreen.get()).build();
    }


    public static void setFPSOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts, MinecraftOptionsStorage vanillaOpts) {
        var builder = OptionGroup.createBuilder();

        var displayFps = OptionImpl.createBuilder(FPSDisplayMode.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.simple"),
                        Component.translatable("embeddium.plus.options.common.complete")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayMode.set(value),
                        (opts) -> EmbyConfig.fpsDisplayMode.get())
                .setImpact(OptionImpact.LOW)
                .build();


        var displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.position.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.position.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.translateVariable("embeddium.plus.options.common.pixels")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMargin.set(value);
                            EmbyConfig.fpsDisplayMarginCache = value;
                        },
                        (opts) -> EmbyConfig.fpsDisplayMarginCache)
                .build();

        builder.add(displayFps);
        builder.add(displayFpsPos);

        groups.add(builder.build());
    }


    public static void setPerformanceOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts, MinecraftOptionsStorage vanillaOpts) {
        var builder = OptionGroup.createBuilder();
        var disableFontShadow = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fontshadow.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fontshadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fontShadows.set(value);
                            EmbyConfig.fontShadowsCache = value;
                        },
                        (options) -> EmbyConfig.fontShadowsCache)
                .setImpact(OptionImpact.VARIES)
                .build();
        var fastChest = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fastchest.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fastchest.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fastChests.set(value);
                            EmbyConfig.fastChestsCache = value;
                        },
                        (options) -> EmbyConfig.fastChestsCache)
                .setImpact(OptionImpact.HIGH)
                .setEnabled(EmbyTools.isFlywheelOff())
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var hideJEI = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.jei.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.hideJREI.set(value);
                            EmbyConfig.hideJREICache = value;
                        },
                        (options) -> EmbyTools.isFlywheelOff() && EmbyConfig.hideJREICache)
                .setImpact(OptionImpact.LOW)
                .setEnabled(EmbyTools.isModInstalled("jei"))
                .build();

        builder.add(disableFontShadow);
        builder.add(fastChest);
        builder.add(hideJEI);

        groups.add(builder.build());
    }


    public static OptionPage setPerformancePlusOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {

        var enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityDistanceCulling.set(value);
                            EmbyConfig.entityDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.entityDistanceCullingCache)
                .setImpact(OptionImpact.LOW)
                .build();

        var maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            int result = value * value;
                            EmbyConfig.entityCullingDistanceX.set(result);
                            EmbyConfig.entityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.entityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityCullingDistanceY.set(value);
                            EmbyConfig.entityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.entityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );


        var maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            int result = value * value;
                            EmbyConfig.tileEntityCullingDistanceX.set(result);
                            EmbyConfig.tileEntityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.tileEntityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("embeddium.plus.options.common.blocks")))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.tileEntityCullingDistanceY.set(value);
                            EmbyConfig.tileEntityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.tileEntityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );
        return new OptionPage(Component.translatable("sodium.options.pages.performance").append("++"), ImmutableList.copyOf(groups));
    }

}
