package me.srrapero720.chloride.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.foundation.zoom.ZoomFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.foundation.embeddium.ChlorideOptions.STORAGE;

public class ZoomPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "zoom")));
    public ZoomPage() {
        super(ID, Component.translatable("chloride.options.zoom.page"), create());
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
                .setEnabled(ZoomFeature.canUseZoom())
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
