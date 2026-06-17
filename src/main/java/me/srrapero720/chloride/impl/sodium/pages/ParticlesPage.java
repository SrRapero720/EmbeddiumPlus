package me.srrapero720.chloride.impl.sodium.pages;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.HashMap;

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
                        .setBinding(v -> ChlorideConfig.rainParticles = v, () -> ChlorideConfig.rainParticles))
                .addOption(b.createBooleanOption(Chloride.id("rainDropParticles"))
                        .setName(Component.translatable("chloride.particles.rain.drop.title"))
                        .setTooltip(Component.translatable("chloride.particles.rain.drop.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.rainDropParticles = v, () -> ChlorideConfig.rainDropParticles))
                .addOption(b.createBooleanOption(Chloride.id("crackingBlockParticles"))
                        .setName(Component.translatable("chloride.particles.block.cracking.title"))
                        .setTooltip(Component.translatable("chloride.particles.block.cracking.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.crackingBlockParticles = v, () -> ChlorideConfig.crackingBlockParticles))
                .addOption(b.createBooleanOption(Chloride.id("destroyedBlockParticles"))
                        .setName(Component.translatable("chloride.particles.block.destroy.title"))
                        .setTooltip(Component.translatable("chloride.particles.block.destroy.desc"))
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.destroyedBlockParticles = v, () -> ChlorideConfig.destroyedBlockParticles))
        );

        final OptionGroupBuilder enabled = b.createOptionGroup();
        final OptionGroupBuilder disabled = b.createOptionGroup();
        boolean hasEnabled = false;
        boolean hasDisabled = false;
        final HashMap<String, String> cachedDisplayNames = new HashMap<>();

        for (final ResourceLocation key : BuiltInRegistries.PARTICLE_TYPE.keySet()) {
            final String displayName = cachedDisplayNames.computeIfAbsent(key.getNamespace(), k -> {
                final var mod = ModList.get().getModFileById(k);
                if (mod != null) {
                    return mod.getMods().get(0).getDisplayName();
                }
                return I18n.get("chloride.particles.provider.unknown");
            });

            final boolean isDisabled = ChlorideConfig.disabledParticles.contains(key);
            if (isDisabled) {
                hasDisabled = true;
            } else {
                hasEnabled = true;
            }

            (isDisabled ? disabled : enabled).addOption(b.createBooleanOption(Chloride.id("particle", key))
                    .setName(Component.literal(key.toString()))
                    .setTooltip(Component.translatable("chloride.particles.provider", Component.literal(displayName).withStyle(ChatFormatting.GOLD)))
                    .setStorageHandler(STORAGE)
                    .setDefaultValue(true)
                    .setBinding(v -> {
                        if (v) {
                            ChlorideConfig.disabledParticles.remove(key);
                        } else {
                            ChlorideConfig.disabledParticles.add(key);
                        }
                    }, () -> !ChlorideConfig.disabledParticles.contains(key)));
        }

        if (hasDisabled) page.addOptionGroup(disabled);
        if (hasEnabled) page.addOptionGroup(enabled);

        return page;
    }
}
