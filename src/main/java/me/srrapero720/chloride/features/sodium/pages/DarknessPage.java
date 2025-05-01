package me.srrapero720.chloride.features.sodium.pages;

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
import me.srrapero720.chloride.Tools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class DarknessPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation(Chloride.ID, "true_darkness"));
    public DarknessPage() {
        super(ID, Component.translatable("chloride.darkness"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var darknessBasics = OptionGroup.createBuilder();
        darknessBasics.add(OptionImpl.createBuilder(ChlorideConfig.DarknessMode.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.level.title"))
                .setTooltip(Component.translatable("chloride.darkness.level.desc"))
                .setControl(option ->
                        new CyclingControl<>(option, ChlorideConfig.DarknessMode.class, Tools.tEnumComponent("chloride.darkness.level", ChlorideConfig.DarknessMode.class)))
                .setBinding((opts, value) -> ChlorideConfig.darknessMode = value,
                        opts -> ChlorideConfig.darknessMode)
                .build()
        );

        darknessBasics.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.noskylight.title"))
                .setTooltip(Component.translatable("chloride.darkness.noskylight.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnNoSkyLight = value,
                        options -> ChlorideConfig.darknessOnNoSkyLight)
                .build()
        );

        final var darknessOverworld = OptionGroup.createBuilder();
        darknessOverworld.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.others.title"))
                .setTooltip(Component.translatable("chloride.darkness.others.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessByDefault = value,
                        options -> ChlorideConfig.darknessByDefault)
                .build()
        );


        darknessOverworld.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.overworld.title"))
                .setTooltip(Component.translatable("chloride.darkness.overworld.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnOverworld = value,
                        options -> ChlorideConfig.darknessOnOverworld)
                .build()
        );

        final var darknessNether = OptionGroup.createBuilder();
        darknessNether.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.nether.title"))
                .setTooltip(Component.translatable("chloride.darkness.nether.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnNether = value,
                        options -> ChlorideConfig.darknessOnNether)
                .build()
        );

        darknessNether.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.nether.brightness.title"))
                .setTooltip(Component.translatable("chloride.darkness.nether.brightness.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessNetherFogBright = current / 100d,
                        options -> Math.toIntExact(Math.round(ChlorideConfig.darknessNetherFogBright * 100)))
                .build()
        );

        final var darknessEnd = OptionGroup.createBuilder();
        darknessEnd.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.end.title"))
                .setTooltip(Component.translatable("chloride.darkness.end.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessOnEnd = value,
                        options -> ChlorideConfig.darknessOnEnd)
                .build()
        );

        darknessEnd.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.end.brightness.title"))
                .setTooltip(Component.translatable("chloride.darkness.end.brightness.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessEndFogBright = current / 100d,
                        options -> Math.toIntExact(Math.round(ChlorideConfig.darknessEndFogBright * 100)))
                .build()
        );

        final var darknessOthers = OptionGroup.createBuilder();
        darknessOthers.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.blocklightonly.title"))
                .setTooltip(Component.translatable("chloride.darkness.blocklightonly.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ChlorideConfig.darknessBlockLightOnly = value,
                        options -> ChlorideConfig.darknessBlockLightOnly)
                .build()
        );


        darknessOthers.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.desc"))
                .setControl(TickBoxControl::new)
                .setEnabledPredicate(() -> !ChlorideConfig.darknessBlockLightOnly)
                .setBinding((options, value) -> ChlorideConfig.darknessAffectedByMoonPhase = value,
                        options -> ChlorideConfig.darknessAffectedByMoonPhase)
                .build()
        );

        darknessOthers.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.fresh.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.fresh.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessNewMoonBright = current / 100d,
                        options -> Math.toIntExact(Math.round(ChlorideConfig.darknessNewMoonBright * 100d)))
                .build()
        );

        darknessOthers.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.darkness.moonphase.full.title"))
                .setTooltip(Component.translatable("chloride.darkness.moonphase.full.desc"))
                .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((options, current) -> ChlorideConfig.darknessFullMoonBright = current / 100d,
                        options -> Math.toIntExact(Math.round(ChlorideConfig.darknessFullMoonBright * 100)))
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
