package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import dev.nolij.zume.common.easing.EasingMethod;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.embeddium.storage.ZumeOptionsStorage;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZoomPage extends OptionPage {

    public ZoomPage() {
        super(Component.translatable("embeddium.plus.options.zoom.page"), create(new ZumeOptionsStorage()));
    }

    private static ImmutableList<OptionGroup> create(ZumeOptionsStorage zoomOptionsStorage) {
        final List<OptionGroup> groups = new ArrayList<>();

        final var enableZume = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.disable = !value,
                        options -> !options.disable
                )
                .build();

        final var cinematicZoom = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.cinematic.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.cinematic.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.enableCinematicZoom = value,
                        options -> options.enableCinematicZoom
                )
                .build();

        final var mouseSensitive = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.sensitive.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.sensitive.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 5, ControlValueFormatter.percentage()))
                .setBinding(
                        (options, value) -> options.mouseSensitivityFloor = Math.min(Math.floor(value / 100d), 1.0d),
                        (options) -> (int) Math.floor(options.mouseSensitivityFloor * 100d)
                )
                .build();

        final var zoomSpeed = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.speed.desc"))
                .setControl((option) -> new SliderControl(option, 5, 150, 5, i -> Component.literal(i + "").append(Component.translatable("embeddium.plus.options.common.nojils"))))
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

        Component[] easeComponents = new Component[EasingMethod.values().length];
        for (int i = 0; i < easeComponents.length; i++) {
            easeComponents[i] = Component.literal(EasingMethod.values()[i].name());
        }
        easeComponents[0] = Component.translatable("embeddium.plus.options.zoom.quadratics.lineal");
        easeComponents[1] = Component.translatable("embeddium.plus.options.zoom.quadratics.quadratics");
        easeComponents[2] = Component.translatable("embeddium.plus.options.zoom.quadratics.quartic");
        easeComponents[3] = Component.translatable("embeddium.plus.options.zoom.quadratics.quintic");
        final var useQuadratics = OptionImpl.createBuilder(EasingMethod.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.quadratics.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.quadratics.desc"))
                .setControl(opt -> new CyclingControl<>(opt, EasingMethod.class, easeComponents))
                .setBinding(
                        (options, value) -> options.zoomEasingMethod = value,
                        options -> options.zoomEasingMethod
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

        final var minFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.min.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.min.desc"))
                .setControl((option) -> new SliderControl(option, 2, 200, 2, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> options.minFOV = (double) value,
                        (options) -> (int) options.minFOV
                )
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(enableZume)
                .add(useQuadratics)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(cinematicZoom)
                .add(zoomSpeed)
                .add(zoomScrolling)
                .add(zoomSmoothnessMS)
                .add(defaultZoom)
                .add(minFov)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(mouseSensitive)
                .add(toggleMode)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
