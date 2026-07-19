package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Zoom;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class ZoomPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "zoom")));
    public ZoomPage() {
        super(ID, Component.translatable("chloride.zoom"), create());
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
                .setBinding((opt, value) -> ChlorideConfig.zoom.enabled = value, opt -> ChlorideConfig.zoom.enabled)
                .setEnabled(Zoom.canUseZoom())
                .setImpact(OptionImpact.LOW)
                .build()
        );

        base.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.zoom.max.title"))
                .setTooltip(Component.translatable("chloride.zoom.max.desc"))
                .setControl((option) -> new SliderControl(option, 10, 100, 1, suffix("°")))
                .setBinding((opts, v) -> ChlorideConfig.zoom.max = (double) v, opts -> Math.toIntExact(Math.round(ChlorideConfig.zoom.max)))
                .build()
        );

        groups.add(base.build());


        return ImmutableList.copyOf(groups);
    }
}
