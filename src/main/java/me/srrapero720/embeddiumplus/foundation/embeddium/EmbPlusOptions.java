package me.srrapero720.embeddiumplus.foundation.embeddium;

import me.jellysquid.mods.sodium.client.gui.options.*;
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


    public static void setFPSOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        var builder = OptionGroup.createBuilder();

        builder.add(OptionImpl.createBuilder(FPSDisplayMode.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.simple"),
                        Component.translatable("embeddium.plus.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayMode.set(value),
                        (opts) -> EmbyConfig.fpsDisplayMode.get())
                .setImpact(OptionImpact.LOW)
                .build()
        );

        builder.add(OptionImpl.createBuilder(FPSDisplaySystemMode.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.system.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplaySystemMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.on"),
                        Component.translatable("embeddium.plus.options.displayfps.system.gpu"),
                        Component.translatable("embeddium.plus.options.displayfps.system.ram")
                }))
                .setBinding((options, value) -> EmbyConfig.fpsDisplaySystemMode.set(value),
                        (options) -> EmbyConfig.fpsDisplaySystemMode.get())
                .build()
        );

        var components = new Component[FPSDisplayGravity.values().length];
        for (int i = 0; i < components.length; i++) {
            components[i] = Component.translatable("embeddium.plus.options.displayfps.gravity." + FPSDisplayGravity.values()[i].name().toLowerCase());
        }

        builder.add(OptionImpl.createBuilder(FPSDisplayGravity.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.gravity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.gravity.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayGravity.class, components))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayGravity.set(value),
                        (opts) -> EmbyConfig.fpsDisplayGravity.get())
                .build()
        );


        builder.add(OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.margin.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.margin.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMargin.set(value);
                            EmbyConfig.fpsDisplayMarginCache = value;
                        },
                        (opts) -> EmbyConfig.fpsDisplayMarginCache)
                .build()
        );

        groups.add(builder.build());
    }


    public static void setPerformanceOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        var builder = OptionGroup.createBuilder();
        var disableFontShadow = OptionImpl.createBuilder(boolean.class, sodiumOpts)
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
        var fastChest = OptionImpl.createBuilder(boolean.class, sodiumOpts)
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

        var hideJEI = OptionImpl.createBuilder(boolean.class, sodiumOpts)
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


}
