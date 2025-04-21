package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.features.ZoomFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class ZoomPage extends OptionPage {
    public ZoomPage() {
        super(Component.translatable("chloride.options.zoom.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var base = OptionGroup.createBuilder();

        var specialZoomEnableTooltip = Component.translatable("chloride.options.zoom.desc").append("[").append(Component.keybind("chloride.zoom").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(Component.literal("]").append("\n"));
        if (!ZoomFeature.canUseZoom()) specialZoomEnableTooltip.append(Component.translatable("chloride.options.zoom.forbidden"));
        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.zoom.title"))
                .setTooltip(specialZoomEnableTooltip)
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.enableZoom = value, opt -> ChlorideConfig.enableZoom)
                .setEnabled(ZoomFeature::canUseZoom)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        base.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.zoom.max.title"))
                .setTooltip(Component.translatable("chloride.options.zoom.max.desc"))
                .setControl((option) -> new SliderControl(option, 10, 100, 1, v -> Component.literal(v + "°")))
                .setBinding((opts, v) -> ChlorideConfig.maxZoom = (double) v, opts -> Math.toIntExact(Math.round(ChlorideConfig.maxZoom)))
                .build()
        );

        groups.add(base.build());


        return ImmutableList.copyOf(groups);
    }
}
