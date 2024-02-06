package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import dev.nolij.zume.common.Zume;
import dev.nolij.zume.common.config.ZumeConfig;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.embeddium.storage.ZumeOptionsStorage;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ZoomPage extends OptionPage {
    private static final ZumeOptionsStorage zoomOptionsStorage = new ZumeOptionsStorage();

    public ZoomPage() {
        super(Component.translatable("embeddium.plus.options.zoom.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var cinematicZoom = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.cinematic.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.cinematic.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.enableCinematicZoom = value,
                        options -> options.enableCinematicZoom
                )
                .setImpact(OptionImpact.LOW)
                .build();

        final var mouseSensitive = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.sensitive.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.sensitive.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> options.mouseSensitivityFloor = Math.min(Math.floor(value / 100d), 1.0d),
                        (options) -> (int) Math.floor(options.mouseSensitivityFloor * 100d)
                )
                .build();

        final var zoomSpeed = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.speed.desc"))
                .setControl((option) -> new SliderControl(option, 5, 100, 1, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> options.zoomSpeed = value.shortValue(),
                        (options) -> (int) options.zoomSpeed
                )
                .build();

        final var zoomScrolling = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.scrolling.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.scrolling.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.enableZoomScrolling = value,
                        options -> options.enableZoomScrolling
                )
                .build();

        final var zoomSmoothnessMS = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.smoothness.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.smoothness.desc"))
                .setControl((option) -> new SliderControl(option, 50, 150, 10, i -> Component.literal(i + "").append(Component.translatable("embeddium.plus.options.common.millis"))))
                .setBinding(
                        (options, value) -> options.zoomSmoothnessMs = value.shortValue(),
                        (options) -> (int) options.zoomSmoothnessMs
                )
                .build();

        final var easingExponent = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.easing.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.easing.desc"))
                .setControl((option) -> new SliderControl(option, 1, 8, 1, ControlValueFormatter.multiplier()))
                .setBinding(
                        (options, value) -> options.easingExponent = value.shortValue(),
                        (options) -> (int) options.easingExponent
                )
                .build();

        final var useQuadratics = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.quadratics.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.quadratics.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.useQuadratic = value,
                        options -> options.useQuadratic
                )
                .build();

        final var defaultZoom = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.default.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.default.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding(
                        (options, value) -> options.defaultZoom = Math.min(Math.floor(value / 100d), 1.0d),
                        (options) -> (int) Math.floor(options.defaultZoom * 100d)
                )
                .build();

        final var toggleMode = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.toggle.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.toggle.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.toggleMode = value,
                        options -> options.toggleMode
                )
                .build();

        final var maxFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.max.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.max.desc"))
                .setControl((option) -> new SliderControl(option, 40, 200, 2, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> options.maxFOV = (double) value,
                        (options) -> (int) options.maxFOV
                )
                .build();

        final var minFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.min.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.min.desc"))
                .setControl((option) -> new SliderControl(option, 40, 200, 2, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> options.minFOV = (double) value,
                        (options) -> (int) options.minFOV
                )
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(cinematicZoom)
                .add(zoomSpeed)
                .add(zoomScrolling)
                .add(zoomSmoothnessMS)
                .add(defaultZoom)
                .add(maxFov)
                .add(minFov)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(mouseSensitive)
                .add(easingExponent)
                .add(useQuadratics)
                .add(toggleMode)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
