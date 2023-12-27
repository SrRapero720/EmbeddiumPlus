package me.srrapero720.embeddiumplus.internal;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.api.EmbPlusAPI;
import net.minecraft.network.chat.Component;

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


    public static void setFPSOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts, MinecraftOptionsStorage vanillaOpts) {
        var builder = OptionGroup.createBuilder();

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

        builder.add(displayFps);
        builder.add(displayFpsPos);

        groups.add(builder.build());
    }


    public static void setPerformanceOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts, MinecraftOptionsStorage vanillaOpts) {
        var builder = OptionGroup.createBuilder();
        OptionImpl<SodiumGameOptions, Boolean> disableFontShadow = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fontshadow.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fontshadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.disableFontShadow.set(value),
                        (options) -> EmbPlusConfig.disableFontShadow.get())
                .setImpact(OptionImpact.VARIES)
                .build();
        OptionImpl<SodiumGameOptions, Boolean> fastChest = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fastchest.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fastchest.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.fastChestsEnabled.set(value),
                        (options) -> EmbPlusConfig.fastChestsEnabled.get())
                .setImpact(OptionImpact.HIGH)
                .setEnabled(EmbPlusTools.flwIsOff())
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> hideJEI = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.jei.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbPlusConfig.hideJEI.set(value),
                        (options) -> EmbPlusTools.flwIsOff() ? EmbPlusConfig.hideJEI.get() : false)
                .setImpact(OptionImpact.LOW)
                .setEnabled(EmbPlusTools.isPresent("jei"))
                .build();

        builder.add(disableFontShadow);
        builder.add(fastChest);
        builder.add(hideJEI);

        groups.add(builder.build());
    }


    public static OptionPage setPerformancePlusOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {

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
        return new OptionPage(Component.translatable("sodium.options.pages.performance").append("++"), ImmutableList.copyOf(groups));
    }

}
