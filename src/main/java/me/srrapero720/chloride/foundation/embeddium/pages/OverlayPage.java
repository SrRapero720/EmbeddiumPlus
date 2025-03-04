package me.srrapero720.chloride.foundation.embeddium.pages;

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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.foundation.embeddium.EmbPlusOptions.STORAGE;

public class OverlayPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "metrics")));
    public OverlayPage() {
        super(ID, Component.translatable("chloride.options.overlay.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var builder = OptionGroup.createBuilder();

        builder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplayMode.class, STORAGE)
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

        builder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplaySystemMode.class, STORAGE)
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

        var components = new Component[ChlorideConfig.FPSDisplayGravity.values().length];
        for (int i = 0; i < components.length; i++) {
            components[i] = Component.translatable("chloride.options.displayfps.gravity." + ChlorideConfig.FPSDisplayGravity.values()[i].name().toLowerCase());
        }

        builder.add(OptionImpl.createBuilder(ChlorideConfig.FPSDisplayGravity.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.gravity.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.gravity.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.FPSDisplayGravity.class, components))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayGravity = value,
                        opts -> ChlorideConfig.fpsDisplayGravity)
                .build()
        );


        builder.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.margin.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.margin.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fpsDisplayMargin = value,
                        opts -> ChlorideConfig.fpsDisplayMargin)
                .build()
        );

        builder.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.displayfps.shadow.title"))
                .setTooltip(Component.translatable("chloride.options.displayfps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.fpsDisplayShadow = value,
                        opt -> ChlorideConfig.fpsDisplayShadow)
                .build()
        );

        groups.add(builder.build());

        return ImmutableList.copyOf(groups);
    }
}
