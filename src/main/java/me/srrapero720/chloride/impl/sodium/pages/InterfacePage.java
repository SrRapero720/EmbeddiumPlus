package me.srrapero720.chloride.impl.sodium.pages;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.Overlay;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class InterfacePage {
    private static final ResourceLocation HIDE_JREMI = Chloride.id("hideJREMI");

    private InterfacePage() {}

    public static OptionPageBuilder build(final ConfigBuilder b) {
        final boolean jremi = Chloride.installed("jei") || Chloride.installed("roughlyenoughitems") || Chloride.installed("emi");
        final int maxMargin = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        final OptionPageBuilder page = b.createOptionPage().setName(Component.translatable("chloride.interface"));

        // FPS overlay
        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createEnumOption(Chloride.id("fpsDisplayMode"), Overlay.FPS.class)
                        .setName(Component.translatable("chloride.interface.fps.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.desc"))
                        .setElementNameProvider(enumNames("chloride.interface.fps.mode"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.LOW)
                        .setDefaultValue(Overlay.FPS.ADVANCED)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.mode = v, () -> ChlorideConfig.fpsDisplay.mode))
                .addOption(b.createEnumOption(Chloride.id("fpsDisplaySystemMode"), Overlay.FPSDetails.class)
                        .setName(Component.translatable("chloride.interface.fps.system.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.system.desc"))
                        .setElementNameProvider(enumNames("chloride.interface.fps.system"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(Overlay.FPSDetails.OFF)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.systemDetails = v, () -> ChlorideConfig.fpsDisplay.systemDetails))
                .addOption(b.createEnumOption(Chloride.id("fpsDisplayAlign"), Overlay.FPSAlign.class)
                        .setName(Component.translatable("chloride.interface.fps.align_x.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.align_x.desc"))
                        .setElementNameProvider(enumNames("chloride.interface.fps.align_x"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(Overlay.FPSAlign.LEFT)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.align = v, () -> ChlorideConfig.fpsDisplay.align))
                .addOption(b.createIntegerOption(Chloride.id("fpsDisplayMargin"))
                        .setName(Component.translatable("chloride.interface.fps.margin_x.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.margin_x.desc"))
                        .setValueFormatter(suffix("px"))
                        .setRange(0, maxMargin, 1)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.LOW)
                        .setDefaultValue(12)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.margin = v, () -> ChlorideConfig.fpsDisplay.margin))
                .addOption(b.createEnumOption(Chloride.id("fpsDisplayVAlign"), Overlay.FPSVAlign.class)
                        .setName(Component.translatable("chloride.interface.fps.align_y.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.align_y.desc"))
                        .setElementNameProvider(enumNames("chloride.interface.fps.align_y"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(Overlay.FPSVAlign.TOP)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.verticalAlign = v, () -> ChlorideConfig.fpsDisplay.verticalAlign))
                .addOption(b.createIntegerOption(Chloride.id("fpsDisplayVMargin"))
                        .setName(Component.translatable("chloride.interface.fps.margin_y.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.margin_y.desc"))
                        .setValueFormatter(suffix("px"))
                        .setRange(0, maxMargin, 1)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.LOW)
                        .setDefaultValue(12)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.verticalMargin = v, () -> ChlorideConfig.fpsDisplay.verticalMargin))
                .addOption(b.createBooleanOption(Chloride.id("fpsDisplayShadow"))
                        .setName(Component.translatable("chloride.interface.fps.shadow.title"))
                        .setTooltip(Component.translatable("chloride.interface.fps.shadow.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.fpsDisplay.shadow = v, () -> ChlorideConfig.fpsDisplay.shadow))
        );

        // Screens
        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("fontShadows"))
                        .setName(Component.translatable("chloride.interface.font.shadow.title"))
                        .setTooltip(Component.translatable("chloride.interface.font.shadow.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.VARIES)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.ui.fontShadows = v, () -> ChlorideConfig.ui.fontShadows))
                .addOption(b.createBooleanOption(HIDE_JREMI)
                        .setName(Component.translatable("chloride.interface.jei.title"))
                        .setTooltip(Component.translatable("chloride.interface.jei.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.MEDIUM)
                        .setEnabled(jremi)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.ui.hideJREMI = v, () -> ChlorideConfig.ui.hideJREMI))
                .addOption(b.createBooleanOption(Chloride.id("hideJREMIHint"))
                        .setName(Component.translatable("chloride.interface.jei.hint.title"))
                        .setTooltip(Component.translatable("chloride.interface.jei.hint.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.LOW)
                        .setEnabledProvider(state -> jremi && state.readBooleanOption(HIDE_JREMI), HIDE_JREMI)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.ui.hideJREMIHint = v, () -> ChlorideConfig.ui.hideJREMIHint))
        );

        // Misc
        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createEnumOption(Chloride.id("borderlessAttachModeF11"), Borderless.AttachMode.class)
                        .setName(Component.translatable("chloride.interface.borderless.f11.title"))
                        .setTooltip(Component.translatable("chloride.interface.borderless.f11.desc"))
                        .setElementNameProvider(enumNames("chloride.interface.borderless.f11"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(Borderless.AttachMode.ATTACH)
                        .setBinding(v -> ChlorideConfig.fullscreen.attachModeF11 = v, () -> ChlorideConfig.fullscreen.attachModeF11))
                .addOption(b.createBooleanOption(Chloride.id("fastLanguageReload"))
                        .setName(Component.translatable("chloride.interface.language.fast_reload.title"))
                        .setTooltip(Component.translatable("chloride.interface.language.fast_reload.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.ui.fastLanguageReload = v, () -> ChlorideConfig.ui.fastLanguageReload))
        );

        return page;
    }
}
