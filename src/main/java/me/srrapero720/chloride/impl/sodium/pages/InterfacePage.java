package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.Overlay;
import me.srrapero720.chloride.impl.sodium.controls.BetterCyclingControl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component; 

import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

public class InterfacePage extends OptionPage {
    public InterfacePage() {
        super(Component.translatable("chloride.interface"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var fps = OptionGroup.createBuilder();
        fps.add(OptionImpl.createBuilder(Overlay.FPS.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPS.class, BetterCyclingControl.tEnumComponent("chloride.interface.fps.mode", Overlay.FPS.class)))
                .setBinding((opts, value) -> ChlorideConfig.fpsDisplayMode = value, opts -> ChlorideConfig.fpsDisplayMode)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSDetails.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.system.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSDetails.class, BetterCyclingControl.tEnumComponent("chloride.interface.fps.system", Overlay.FPSDetails.class)))
                .setBinding((options, value) -> ChlorideConfig.fpsDisplaySystemMode = value,
                        (options) -> ChlorideConfig.fpsDisplaySystemMode)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSAlign.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.align_x.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.align_x.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSAlign.class, BetterCyclingControl.tEnumComponent("chloride.interface.fps.align_x", Overlay.FPSAlign.class)))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayAlign = value,
                        opts -> ChlorideConfig.fpsDisplayAlign)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.margin_x.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.margin_x.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayMargin = value,
                        opts -> ChlorideConfig.fpsDisplayMargin)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSVAlign.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.align_y.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.align_y.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSVAlign.class, BetterCyclingControl.tEnumComponent("chloride.interface.fps.align_y", Overlay.FPSVAlign.class)))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayVAlign = value,
                        opts -> ChlorideConfig.fpsDisplayVAlign)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.margin_y.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.margin_y.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayVMargin = value,
                        opts -> ChlorideConfig.fpsDisplayVMargin)
                .build()
        );
        fps.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.shadow.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.fpsDisplayShadow = value,
                        opt -> ChlorideConfig.fpsDisplayShadow)
                .build()
        );

        final var screens = OptionGroup.createBuilder();

        screens.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.font.shadow.title"))
                .setTooltip(Component.translatable("chloride.interface.font.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fontShadows = value,
                        (opts) -> ChlorideConfig.fontShadows)
                .setImpact(OptionImpact.VARIES)
                .build()
        );

        screens.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.jei.title"))
                .setTooltip(Component.translatable("chloride.interface.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.hideJREMI = value,
                        (opts) -> ChlorideConfig.hideJREMI)
                .setImpact(OptionImpact.LOW)
                .setEnabled(() -> Chloride.installed("jei") || Chloride.installed("roughlyenoughitems") || Chloride.installed("emi"))
                .build()
        );

        final var misc = OptionGroup.createBuilder();
        misc.add(OptionImpl.createBuilder(Borderless.AttachMode.class, STORAGE)
                .setName(Component.translatable("chloride.interface.borderless.f11.title"))
                .setTooltip(Component.translatable("chloride.interface.borderless.f11.desc"))
                .setControl(option -> new CyclingControl<>(option, Borderless.AttachMode.class, BetterCyclingControl.tEnumComponent("chloride.interface.borderless.f11", Borderless.AttachMode.class)))
                .setBinding((options, value) -> ChlorideConfig.borderlessAttachModeF11 = value,
                        (options) -> ChlorideConfig.borderlessAttachModeF11)
                .build());

        misc.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.language.fast_reload.title"))
                .setTooltip(Component.translatable("chloride.interface.language.fast_reload.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.fastLanguageReload = value, (options) -> ChlorideConfig.fastLanguageReload)
                .build());

        groups.add(fps.build());
        groups.add(screens.build());
        groups.add(misc.build());

        return ImmutableList.copyOf(groups);
    }
}
