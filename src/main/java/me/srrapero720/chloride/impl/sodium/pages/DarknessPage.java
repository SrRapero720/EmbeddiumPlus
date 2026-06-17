package me.srrapero720.chloride.impl.sodium.pages;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Darkness;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class DarknessPage {
    private static final ResourceLocation BLOCK_LIGHT_ONLY = Chloride.id("darknessBlockLightOnly");

    private DarknessPage() {}

    public static OptionPageBuilder build(final ConfigBuilder b) {
        final OptionPageBuilder page = b.createOptionPage().setName(Component.translatable("chloride.darkness"));

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createEnumOption(Chloride.id("darknessMode"), Darkness.DarkMode.class)
                        .setName(Component.translatable("chloride.darkness.level.title"))
                        .setTooltip(Component.translatable("chloride.darkness.level.desc"))
                        .setElementNameProvider(enumNames("chloride.darkness.level"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(Darkness.DarkMode.VANILLA)
                        .setBinding(v -> ChlorideConfig.darknessMode = v, () -> ChlorideConfig.darknessMode))
                .addOption(b.createBooleanOption(Chloride.id("darknessOnNoSkyLight"))
                        .setName(Component.translatable("chloride.darkness.noskylight.title"))
                        .setTooltip(Component.translatable("chloride.darkness.noskylight.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.darknessOnNoSkyLight = v, () -> ChlorideConfig.darknessOnNoSkyLight))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("darknessByDefault"))
                        .setName(Component.translatable("chloride.darkness.others.title"))
                        .setTooltip(Component.translatable("chloride.darkness.others.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.darknessByDefault = v, () -> ChlorideConfig.darknessByDefault))
                .addOption(b.createBooleanOption(Chloride.id("darknessOnOverworld"))
                        .setName(Component.translatable("chloride.darkness.overworld.title"))
                        .setTooltip(Component.translatable("chloride.darkness.overworld.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.darknessOnOverworld = v, () -> ChlorideConfig.darknessOnOverworld))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("darknessOnNether"))
                        .setName(Component.translatable("chloride.darkness.nether.title"))
                        .setTooltip(Component.translatable("chloride.darkness.nether.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.darknessOnNether = v, () -> ChlorideConfig.darknessOnNether))
                .addOption(b.createIntegerOption(Chloride.id("darknessNetherFogBright"))
                        .setName(Component.translatable("chloride.darkness.nether.brightness.title"))
                        .setTooltip(Component.translatable("chloride.darkness.nether.brightness.desc"))
                        .setValueFormatter(PERCENT)
                        .setRange(0, 100, 1)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(50)
                        .setBinding(v -> ChlorideConfig.darknessNetherFogBright = v / 100d,
                                () -> Math.toIntExact(Math.round(ChlorideConfig.darknessNetherFogBright * 100))))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("darknessOnEnd"))
                        .setName(Component.translatable("chloride.darkness.end.title"))
                        .setTooltip(Component.translatable("chloride.darkness.end.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.darknessOnEnd = v, () -> ChlorideConfig.darknessOnEnd))
                .addOption(b.createIntegerOption(Chloride.id("darknessEndFogBright"))
                        .setName(Component.translatable("chloride.darkness.end.brightness.title"))
                        .setTooltip(Component.translatable("chloride.darkness.end.brightness.desc"))
                        .setValueFormatter(PERCENT)
                        .setRange(0, 100, 1)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(50)
                        .setBinding(v -> ChlorideConfig.darknessEndFogBright = v / 100d,
                                () -> Math.toIntExact(Math.round(ChlorideConfig.darknessEndFogBright * 100))))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(BLOCK_LIGHT_ONLY)
                        .setName(Component.translatable("chloride.darkness.blocklightonly.title"))
                        .setTooltip(Component.translatable("chloride.darkness.blocklightonly.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.darknessBlockLightOnly = v, () -> ChlorideConfig.darknessBlockLightOnly))
                .addOption(b.createBooleanOption(Chloride.id("darknessAffectedByMoonPhase"))
                        .setName(Component.translatable("chloride.darkness.moonphase.title"))
                        .setTooltip(Component.translatable("chloride.darkness.moonphase.desc"))
                        .setStorageHandler(STORAGE)
                        .setEnabledProvider(state -> !state.readBooleanOption(BLOCK_LIGHT_ONLY), BLOCK_LIGHT_ONLY)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.darknessAffectedByMoonPhase = v, () -> ChlorideConfig.darknessAffectedByMoonPhase))
                .addOption(b.createIntegerOption(Chloride.id("darknessNewMoonBright"))
                        .setName(Component.translatable("chloride.darkness.moonphase.fresh.title"))
                        .setTooltip(Component.translatable("chloride.darkness.moonphase.fresh.desc"))
                        .setValueFormatter(PERCENT)
                        .setRange(0, 100, 1)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(0)
                        .setBinding(v -> ChlorideConfig.darknessNewMoonBright = v / 100d,
                                () -> Math.toIntExact(Math.round(ChlorideConfig.darknessNewMoonBright * 100d))))
                .addOption(b.createIntegerOption(Chloride.id("darknessFullMoonBright"))
                        .setName(Component.translatable("chloride.darkness.moonphase.full.title"))
                        .setTooltip(Component.translatable("chloride.darkness.moonphase.full.desc"))
                        .setValueFormatter(PERCENT)
                        .setRange(0, 100, 1)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(25)
                        .setBinding(v -> ChlorideConfig.darknessFullMoonBright = v / 100d,
                                () -> Math.toIntExact(Math.round(ChlorideConfig.darknessFullMoonBright * 100))))
        );

        return page;
    }
}
