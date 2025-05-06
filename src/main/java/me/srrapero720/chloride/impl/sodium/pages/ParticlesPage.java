package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

public class ParticlesPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "particles")));
    public static final OptionIdentifier<Void> PARTICLE_BASE_PAGE = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "particles_page_base")));
    public ParticlesPage() {
        super(ID, Component.translatable("chloride.particles"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var particles = OptionGroup.createBuilder();
        particles.setId(PARTICLE_BASE_PAGE);

        final var base = OptionGroup.createBuilder();
        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.rain.title"))
                .setTooltip(Component.translatable("chloride.particles.rain.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.rainParticles = value, opts -> ChlorideConfig.rainParticles)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.rain.drop.title"))
                .setTooltip(Component.translatable("chloride.particles.rain.drop.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.rainDropParticles = value, opts -> ChlorideConfig.rainDropParticles)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.block.cracking.title"))
                .setTooltip(Component.translatable("chloride.particles.block.cracking.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.crackingBlockParticles = value, opts -> ChlorideConfig.crackingBlockParticles)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.block.destroy.title"))
                .setTooltip(Component.translatable("chloride.particles.block.destroy.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.destroyedBlockParticles = value, opts -> ChlorideConfig.destroyedBlockParticles)
                .build()
        );

        final var enabled = OptionGroup.createBuilder();
        final var disabled = OptionGroup.createBuilder();
        final var cachedDisplayNames = new HashMap<String, String>();

        for (final var key: BuiltInRegistries.PARTICLE_TYPE.keySet()) {
            // THIS CAN BE OPTIMIZED WITH A UPSIDE-DOWN SEARCHER, SEARCH FROM THE LAST ADDED VALUE, NO IDEA ABOUT JAVA IMPL
            final String displayName = cachedDisplayNames.computeIfAbsent(key.getNamespace(), k -> {
                final var mod = ModList.get().getModFileById(k);
                if (mod != null) {
                    return mod.getMods().get(0).getDisplayName();
                }
                return I18n.get("chloride.particles.provider.unknown");
            });

            (ChlorideConfig.disabledParticles.contains(key) ? disabled : enabled).add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "particle_" + key.getNamespace() + "_" + key.getPath()))
                    .setName(Component.literal(key.toString()))
                    .setTooltip(Component.translatable("chloride.particles.provider", Component.literal(displayName).withStyle(ChatFormatting.GOLD)))
                    .setControl(TickBoxControl::new)
                    .setBinding((opts, value) -> {
                        if (value) {
                            ChlorideConfig.disabledParticles.remove(key);
                        } else {
                            ChlorideConfig.disabledParticles.add(key);
                        }
                    }, opts -> !ChlorideConfig.disabledParticles.contains(key))
                    .build()
            );
        }

        groups.add(particles.build());
        groups.add(base.build());
        groups.add(disabled.build());
        groups.add(enabled.build());

        return ImmutableList.copyOf(groups);
    }
}
