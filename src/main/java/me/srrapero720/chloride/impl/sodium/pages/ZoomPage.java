package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Zoom;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

public class ZoomPage extends OptionPage {
    public ZoomPage() {
        super(Component.translatable("chloride.zoom"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var base = OptionGroup.createBuilder();

        final var specialZoomEnableTooltip = Component.translatable("chloride.zoom.desc").append("[").append(Component.keybind("chloride.zoom").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(Component.literal("]").append("\n"));
        if (!Zoom.canUseZoom()) specialZoomEnableTooltip.append(Component.translatable("chloride.zoom.forbidden"));
        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.zoom.title"))
                .setTooltip(specialZoomEnableTooltip)
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.enableZoom = value, opt -> ChlorideConfig.enableZoom)
                .setEnabled(Zoom::canUseZoom)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        base.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.zoom.max.title"))
                .setTooltip(Component.translatable("chloride.zoom.max.desc"))
                .setControl((option) -> new SliderControl(option, 10, 100, 1, v -> Component.literal(v + "°")))
                .setBinding((opts, v) -> ChlorideConfig.maxZoom = (double) v, opts -> Math.toIntExact(Math.round(ChlorideConfig.maxZoom)))
                .build()
        );

        groups.add(base.build());


        return ImmutableList.copyOf(groups);
    }
}
