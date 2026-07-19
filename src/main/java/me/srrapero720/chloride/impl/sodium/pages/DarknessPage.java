package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class DarknessPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "true_darkness")));
    public DarknessPage() {
        super(ID, Component.translatable("chloride.darkness"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var darknessBasics = OptionGroup.createBuilder();
        darknessBasics.add(OptionImpl.createBuilder(Darkness.DarkMode.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.level.title"))
                .setTooltip(Component.translatable("chloride.darkness.level.desc"))
                .setControl(option -> new CyclingControl<>(option, Darkness.DarkMode.class, enumNames("chloride.darkness.level", Darkness.DarkMode.class)))
                .setBinding((opts, value) -> ChlorideConfig.darkness.mode = value, opts -> ChlorideConfig.darkness.mode)
                .build()
        );

        darknessBasics.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.noskylight.title"))
                .setTooltip(Component.translatable("chloride.darkness.noskylight.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.onNoSkyLight = value, opts -> ChlorideConfig.darkness.onNoSkyLight)
                .build()
        );

        final var darknessOverworld = OptionGroup.createBuilder();
        darknessOverworld.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.others.title"))
                .setTooltip(Component.translatable("chloride.darkness.others.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.byDefault = value, opts -> ChlorideConfig.darkness.byDefault)
                .build()
        );


        darknessOverworld.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.overworld.title"))
                .setTooltip(Component.translatable("chloride.darkness.overworld.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.onOverworld = value, opts -> ChlorideConfig.darkness.onOverworld)
                .build()
        );

        final var darknessNether = OptionGroup.createBuilder();
        darknessNether.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.nether.title"))
                .setTooltip(Component.translatable("chloride.darkness.nether.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.onNether = value, opts -> ChlorideConfig.darkness.onNether)
                .build()
        );

        darknessNether.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.nether.brightness.title"))
                .setTooltip(Component.translatable("chloride.darkness.nether.brightness.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((opts, current) -> ChlorideConfig.darkness.netherFogBright = current / 100d,
                        opts -> Math.toIntExact(Math.round(ChlorideConfig.darkness.netherFogBright * 100)))
                .build()
        );

        final var darknessEnd = OptionGroup.createBuilder();
        darknessEnd.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.end.title"))
                .setTooltip(Component.translatable("chloride.darkness.end.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.onEnd = value, opts -> ChlorideConfig.darkness.onEnd)
                .build()
        );

        darknessEnd.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.end.brightness.title"))
                .setTooltip(Component.translatable("chloride.darkness.end.brightness.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((opts, current) -> ChlorideConfig.darkness.endFogBright = current / 100d,
                        opts -> Math.toIntExact(Math.round(ChlorideConfig.darkness.endFogBright * 100)))
                .build()
        );

        final var darknessOthers = OptionGroup.createBuilder();
        darknessOthers.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.blocklightonly.title"))
                .setTooltip(Component.translatable("chloride.darkness.blocklightonly.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.darkness.blockLightOnly = value, opts -> ChlorideConfig.darkness.blockLightOnly)
                .build()
        );


        darknessOthers.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.desc"))
                .setControl(TickBoxControl::new)
                .setEnabledPredicate(() -> !ChlorideConfig.darkness.blockLightOnly)
                .setBinding((opts, value) -> ChlorideConfig.darkness.affectedByMoonPhase = value, opts -> ChlorideConfig.darkness.affectedByMoonPhase)
                .build()
        );

        darknessOthers.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.fresh.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.fresh.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((opts, value) -> ChlorideConfig.darkness.newMoonBright = value / 100d,
                        opts -> Math.toIntExact(Math.round(ChlorideConfig.darkness.newMoonBright * 100d)))
                .build()
        );

        darknessOthers.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.full.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.full.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((opts, value) -> ChlorideConfig.darkness.fullMoonBright = value / 100d,
                        opts -> Math.toIntExact(Math.round(ChlorideConfig.darkness.fullMoonBright * 100)))
                .build()
        );

        groups.add(darknessBasics.build());
        groups.add(darknessOverworld.build());
        groups.add(darknessNether.build());
        groups.add(darknessEnd.build());
        groups.add(darknessOthers.build());

        return ImmutableList.copyOf(groups);
    }
}
