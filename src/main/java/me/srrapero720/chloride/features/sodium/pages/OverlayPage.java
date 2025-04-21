package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class OverlayPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "metrics")));
    public OverlayPage() {
        super(ID, Component.translatable("chloride.options.overlay.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var fpsBuilder = OptionGroup.createBuilder();

        fpsBuilder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplayMode.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.FPSDisplayMode.class, new Component[]{
                        Component.translatable("chloride.options.common.off"),
                        Component.translatable("chloride.options.common.simple"),
                        Component.translatable("chloride.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayMode = value,
                        opts -> ChlorideConfig.fpsDisplayMode)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        fpsBuilder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplaySystemMode.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.system.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.FPSDisplaySystemMode.class, new Component[]{
                        Component.translatable("chloride.options.common.off"),
                        Component.translatable("chloride.options.common.on"),
                        Component.translatable("chloride.options.displayfps.system.gpu"),
                        Component.translatable("chloride.options.displayfps.system.ram")
                }))
                .setBinding((options, value) -> ChlorideConfig.fpsDisplaySystemMode = value,
                        (options) -> ChlorideConfig.fpsDisplaySystemMode)
                .build()
        );

        fpsBuilder.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.shadow.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.fpsDisplayShadow = value,
                        opt -> ChlorideConfig.fpsDisplayShadow)
                .build()
        );

        var alignBuilder = OptionGroup.createBuilder();

        var alignXComponent = new Component[ChlorideConfig.FPSDisplayAlign.values().length];
        for (int i = 0; i < alignXComponent.length; i++) {
            alignXComponent[i] = Component.translatable("chloride.options.displayfps.align_x." + ChlorideConfig.FPSDisplayAlign.values()[i].name().toLowerCase());
        }

        alignBuilder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplayAlign.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.align_x.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.align_x.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.FPSDisplayAlign.class, alignXComponent))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayAlign = value,
                        opts -> ChlorideConfig.fpsDisplayAlign)
                .build()
        );
        alignBuilder.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.margin_x.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.margin_x.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayMargin = value,
                        opts -> ChlorideConfig.fpsDisplayMargin)
                .build()
        );

        var alignYComponent = new Component[ChlorideConfig.FPSDisplayAlign.values().length];
        for (int i = 0; i < alignYComponent.length; i++) {
            alignYComponent[i] = Component.translatable("chloride.options.displayfps.align_y." + ChlorideConfig.FPSDisplayVAlign.values()[i].name().toLowerCase());
        }
        alignBuilder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplayVAlign.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.align_y.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.align_y.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.FPSDisplayVAlign.class, alignYComponent))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayVAlign = value,
                        opts -> ChlorideConfig.fpsDisplayVAlign)
                .build()
        );

        alignBuilder.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.margin_y.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.margin_y.desc"))
                .setControl((option) -> new SliderControl(option, 0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayVMargin = value,
                        opts -> ChlorideConfig.fpsDisplayVMargin)
                .build()
        );

        groups.add(fpsBuilder.build());
        groups.add(alignBuilder.build());

        return ImmutableList.copyOf(groups);
    }
}
