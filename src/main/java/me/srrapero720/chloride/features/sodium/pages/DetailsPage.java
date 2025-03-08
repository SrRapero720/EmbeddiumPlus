package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
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

public class DetailsPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "quality")));
    public DetailsPage() {
        super(ID, Component.translatable("chloride.pages.details"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var fog = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.fog.title"))
                .setTooltip(Component.translatable("chloride.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> {
                            ChlorideConfig.fog = value;
                        },
                        (opt) -> ChlorideConfig.fog)
                .setImpact(OptionImpact.LOW)
                .build();

        final var blueBand = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.blueband.title"))
                .setTooltip(Component.translatable("chloride.options.blueband.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, v) -> ChlorideConfig.blueBand = v, opt -> ChlorideConfig.blueBand)
                .build();

        final var fadeInQuality = OptionImpl.createBuilder(ChlorideConfig.ChunkFadeSpeed.class, STORAGE)
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
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .add(blueBand)
                .add(fadeInQuality)
                .build()
        );

        final var cloudHeight = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.clouds.height.title"))
                .setTooltip(Component.translatable("chloride.options.clouds.height.desc"))
                .setControl((opt) -> new SliderControl(opt, 64, 364, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.cloudsHeight = value,
                        opt -> ChlorideConfig.cloudsHeight)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(cloudHeight)
                .build()
        );

        final var disableNameTagRendering = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.nametag.disable_rendering.title"))
                .setTooltip(Component.translatable("chloride.options.nametag.disable_rendering.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, v) -> ChlorideConfig.disableNameTagRender = v, opt -> ChlorideConfig.disableNameTagRender)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(disableNameTagRendering)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
