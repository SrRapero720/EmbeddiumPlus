package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class DetailsPage extends OptionPage {
    public DetailsPage() {
        super(Component.translatable("chloride.pages.details"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

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
