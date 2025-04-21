package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.shaders.FogShape;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
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

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class SkiesPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "skies")));
    public SkiesPage() {
        super(ID, Component.translatable("chloride.pages.skies"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var fogBuilder = OptionGroup.createBuilder();

        fogBuilder.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.fog.title"))
                .setTooltip(Component.translatable("chloride.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog = value,
                        (opt) -> ChlorideConfig.fog)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        fogBuilder.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.blueband.title"))
                .setTooltip(Component.translatable("chloride.options.blueband.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, v) -> ChlorideConfig.blueBand = v, opt -> ChlorideConfig.blueBand)
                .build()
        );

        final var customFog = OptionGroup.createBuilder();

        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.custom_fog.title"))
                .setTooltip(Component.translatable("chloride.options.custom_fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.customFog = value,
                        (opt) -> ChlorideConfig.customFog)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.custom_fog.start.title"))
                .setTooltip(Component.translatable("chloride.options.custom_fog.start.desc"))
                .setControl((option) -> new SliderControl(option, -1000, 1000, 10, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fogStart = current,
                        (options) -> ChlorideConfig.fogStart)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.custom_fog.end.title"))
                .setTooltip(Component.translatable("chloride.options.custom_fog.end.desc"))
                .setControl((option) -> new SliderControl(option, 100, 10000, 50, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fogEnd = current,
                        (options) -> ChlorideConfig.fogEnd)
                .build()
        );

        var alignYComponent = new Component[FogShape.values().length];
        for (int i = 0; i < alignYComponent.length; i++) {
            alignYComponent[i] = Component.translatable("chloride.options.custom_fog.shape." + FogShape.values()[i].name().toLowerCase());
        }

        customFog.add(OptionImpl.createBuilder(FogShape.class, STORAGE)
                .setName(Component.translatable("chloride.options.custom_fog.shape.title"))
                .setTooltip(Component.translatable("chloride.options.custom_fog.shape.desc"))
                .setControl((option) -> new CyclingControl<>(option, FogShape.class, alignYComponent))
                .setBinding(
                        (opts, value) -> ChlorideConfig.fogShape = value,
                        opts -> ChlorideConfig.fogShape)
                .build()
        );

        final var chunkBuilder = OptionGroup.createBuilder();
        chunkBuilder.add(OptionImpl.createBuilder(ChlorideConfig.ChunkFadeSpeed.class, STORAGE)
                .setName(Component.translatable("chloride.options.fadein.title"))
                .setTooltip(Component.translatable("chloride.options.fadein.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.ChunkFadeSpeed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding((opts, value) -> ChlorideConfig.chunkFadeSpeed = value,
                        (opts) -> ChlorideConfig.chunkFadeSpeed)
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build()
        );

        groups.add(fogBuilder.build());
        groups.add(customFog.build());
        groups.add(chunkBuilder.build());

        return ImmutableList.copyOf(groups);
    }
}
