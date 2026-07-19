package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class InterfacePage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "interface")));
    public InterfacePage() {
        super(ID, Component.translatable("chloride.interface"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var fps = OptionGroup.createBuilder();
        fps.add(OptionImpl.createBuilder(Overlay.FPS.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPS.class, enumNames("chloride.interface.fps.mode", Overlay.FPS.class)))
                .setBinding((opts, value) -> ChlorideConfig.fpsDisplay.mode = value, opts -> ChlorideConfig.fpsDisplay.mode)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSDetails.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.system.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSDetails.class, enumNames("chloride.interface.fps.system", Overlay.FPSDetails.class)))
                .setBinding((options, value) -> ChlorideConfig.fpsDisplay.systemDetails = value,
                        (options) -> ChlorideConfig.fpsDisplay.systemDetails)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSAlign.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.align_x.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.align_x.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSAlign.class, enumNames("chloride.interface.fps.align_x", Overlay.FPSAlign.class)))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplay.align = value,
                        opts -> ChlorideConfig.fpsDisplay.align)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.margin_x.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.margin_x.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, suffix("px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplay.margin = value,
                        opts -> ChlorideConfig.fpsDisplay.margin)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Overlay.FPSVAlign.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.align_y.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.align_y.desc"))
                .setControl((option) -> new CyclingControl<>(option, Overlay.FPSVAlign.class, enumNames("chloride.interface.fps.align_y", Overlay.FPSVAlign.class)))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplay.verticalAlign = value,
                        opts -> ChlorideConfig.fpsDisplay.verticalAlign)
                .build()
        );
        fps.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.margin_y.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.margin_y.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, suffix("px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplay.verticalMargin = value,
                        opts -> ChlorideConfig.fpsDisplay.verticalMargin)
                .build()
        );
        fps.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.fps.shadow.title"))
                .setTooltip(Component.translatable("chloride.interface.fps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.fpsDisplay.shadow = value,
                        opt -> ChlorideConfig.fpsDisplay.shadow)
                .build()
        );

        final var screens = OptionGroup.createBuilder();

        screens.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "font_shadow"))
                .setName(Component.translatable("chloride.interface.font.shadow.title"))
                .setTooltip(Component.translatable("chloride.interface.font.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.ui.fontShadows = value,
                        (opts) -> ChlorideConfig.ui.fontShadows)
                .setImpact(OptionImpact.VARIES)
                .build()
        );

        Option<?> hideJREMI = null;
        screens.add(hideJREMI = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "hide_jremi"))
                .setName(Component.translatable("chloride.interface.jei.title"))
                .setTooltip(Component.translatable("chloride.interface.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.ui.hideJREMI = value,
                        (opts) -> ChlorideConfig.ui.hideJREMI)
                .setImpact(OptionImpact.MEDIUM)
                .setEnabled(Chloride.installed("jei") || Chloride.installed("roughlyenoughitems") || Chloride.installed("emi"))
                .build()
        );

        screens.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "hide_jremi_hint"))
                .setName(Component.translatable("chloride.interface.jei.hint.title"))
                .setTooltip(Component.translatable("chloride.interface.jei.hint.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.ui.hideJREMIHint = value,
                        (opts) -> ChlorideConfig.ui.hideJREMIHint)
                .setImpact(OptionImpact.LOW)
                .setEnabled(hideJREMI.isAvailable() && (Boolean) hideJREMI.getValue())
                .build()
        );

        final var misc = OptionGroup.createBuilder();
        misc.add(OptionImpl.createBuilder(Borderless.AttachMode.class, STORAGE)
                .setName(Component.translatable("chloride.interface.borderless.f11.title"))
                .setTooltip(Component.translatable("chloride.interface.borderless.f11.desc"))
                .setControl(option -> new CyclingControl<>(option, Borderless.AttachMode.class, enumNames("chloride.interface.borderless.f11", Borderless.AttachMode.class)))
                .setBinding((options, value) -> ChlorideConfig.fullscreen.attachModeF11 = value,
                        (options) -> ChlorideConfig.fullscreen.attachModeF11)
                .build());

        misc.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.interface.language.fast_reload.title"))
                .setTooltip(Component.translatable("chloride.interface.language.fast_reload.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.ui.fastLanguageReload = value, (options) -> ChlorideConfig.ui.fastLanguageReload)
                .build());

        groups.add(fps.build());
        groups.add(screens.build());
        groups.add(misc.build());

        return ImmutableList.copyOf(groups);
    }
}
