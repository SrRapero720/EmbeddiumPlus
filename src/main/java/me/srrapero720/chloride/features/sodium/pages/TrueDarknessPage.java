package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class TrueDarknessPage extends OptionPage {
    public TrueDarknessPage() {
        super(Component.translatable("chloride.options.darkness.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        // GENERAL ENABLE
        final var darknessMode = OptionImpl.createBuilder(ChlorideConfig.DarknessMode.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.mode.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.mode.desc"))
                .setControl((option) -> new CyclingControl<>(option, ChlorideConfig.DarknessMode.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("chloride.options.darkness.mode.dim"),
                        Component.translatable("chloride.options.darkness.mode.dark"),
                        Component.translatable("chloride.options.darkness.mode.darkness"),
                        Component.translatable("chloride.options.darkness.mode.black"),
                        Component.translatable("chloride.options.darkness.mode.blackness"),
                }))
                .setBinding((opts, value) -> ChlorideConfig.darknessMode = value,
                        (opts) -> ChlorideConfig.darknessMode)
                .build();

        var noSkylight = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.noskylight.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.noskylight.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnNoSkyLight = value,
                        (options) -> ChlorideConfig.darknessOnNoSkyLight)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(darknessMode)
                .add(noSkylight)
                .build()
        );

        // OVERWORLD
        var darknessOtherDim = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.others.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.others.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessByDefault = value,
                        (options) -> ChlorideConfig.darknessByDefault)
                .build();
        var darknessOnOverworld = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.overworld.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.overworld.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnOverworld = value,
                        (options) -> ChlorideConfig.darknessOnOverworld)
                .build();

        var darknessOnNether = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.nether.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.nether.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnNether = value,
                        (options) -> ChlorideConfig.darknessOnNether)
                .build();

        final var netherFogBright = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.nether.brightness.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.nether.brightness.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessNetherFogBright = current / 100d,
                        (options) -> Math.toIntExact(Math.round(ChlorideConfig.darknessNetherFogBright * 100)))
                .build();

        var darknessOnEnd = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.end.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.end.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnEnd = value,
                        (options) -> ChlorideConfig.darknessOnEnd)
                .build();

        final var endFogBright = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.end.brightness.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.end.brightness.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessEndFogBright = current / 100d,
                        (options) -> Math.toIntExact(Math.round(ChlorideConfig.darknessEndFogBright * 100)))
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(darknessOtherDim)
                .add(darknessOnOverworld)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(darknessOnNether)
                .add(netherFogBright)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(darknessOnEnd)
                .add(endFogBright)
                .build()
        );

        var blockLightOnly = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.blocklightonly.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.blocklightonly.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessBlockLightOnly = value,
                        (options) -> ChlorideConfig.darknessBlockLightOnly)
                .build();


        var affectedByMoonPhase = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.moonphase.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.moonphase.desc"))
                .setControl(TickBoxControl::new)
                .setEnabled(() -> !ChlorideConfig.darknessBlockLightOnly)
                .setBinding((options, value) -> ChlorideConfig.darknessAffectedByMoonPhase = value,
                        (options) -> ChlorideConfig.darknessAffectedByMoonPhase)
                .build();

        final var newMoonBright = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.moonphase.fresh.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.moonphase.fresh.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessNewMoonBright = current / 100d,
                        (options) -> Math.toIntExact(Math.round(ChlorideConfig.darknessNewMoonBright * 100d)))
                .build();

        final var fullMoonBright = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.darkness.moonphase.full.title"))
                .setTooltip(Component.translatable("chloride.options.darkness.moonphase.full.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessFullMoonBright = current / 100d,
                        (options) -> Math.toIntExact(Math.round(ChlorideConfig.darknessFullMoonBright * 100)))
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(blockLightOnly)
                .add(affectedByMoonPhase)
                .add(newMoonBright)
                .add(fullMoonBright)
                .build()
        );


        return ImmutableList.copyOf(groups);
    }
}
