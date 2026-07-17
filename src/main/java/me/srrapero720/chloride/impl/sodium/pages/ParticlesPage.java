package me.srrapero720.chloride.impl.sodium.pages;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class ParticlesPage {
    private ParticlesPage() {}

    public static OptionPageBuilder build(final ConfigBuilder b) {
        final OptionPageBuilder page = b.createOptionPage().setName(Component.translatable("chloride.particles"));

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("rainParticles"))
                        .setName(Component.translatable("chloride.particles.rain.title"))
                        .setTooltip(Component.translatable("chloride.particles.rain.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.particles.rain = v, () -> ChlorideConfig.particles.rain))
                .addOption(b.createBooleanOption(Chloride.id("rainDropParticles"))
                        .setName(Component.translatable("chloride.particles.rain.drop.title"))
                        .setTooltip(Component.translatable("chloride.particles.rain.drop.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.particles.rainDrops = v, () -> ChlorideConfig.particles.rainDrops))
                .addOption(b.createBooleanOption(Chloride.id("crackingBlockParticles"))
                        .setName(Component.translatable("chloride.particles.block.cracking.title"))
                        .setTooltip(Component.translatable("chloride.particles.block.cracking.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.particles.blockCracking = v, () -> ChlorideConfig.particles.blockCracking))
                .addOption(b.createBooleanOption(Chloride.id("destroyedBlockParticles"))
                        .setName(Component.translatable("chloride.particles.block.destroy.title"))
                        .setTooltip(Component.translatable("chloride.particles.block.destroy.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.particles.blockDestroyed = v, () -> ChlorideConfig.particles.blockDestroyed))
        );

        // THE PER-PARTICLE-TYPE TOGGLES LIVE IN ParticleListScreen (EXTERNAL PAGE): ONE SODIUM OPTION PER
        // REGISTERED PARTICLE TANKED THE WHOLE SETTINGS SCREEN ON BIG MODPACKS (ISSUE #174)
        return page;
    }
}
