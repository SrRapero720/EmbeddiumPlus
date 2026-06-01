package me.srrapero720.chloride.impl.sodium;

import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.Borderless.Mode;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import me.srrapero720.chloride.impl.FastBlocks;
import me.srrapero720.chloride.impl.sodium.pages.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

import static me.srrapero720.chloride.Chloride.*;

@Mod.EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SodiumFeatures {
    public static final OptionStorage<?> STORAGE = new OptionStorage<>() {
        @Override public Object getData() { return new Object(); }
        @Override public void save() { ChlorideConfig.write(); }
    };

    @SubscribeEvent
    public static void onSodiumPagesRegister(final OptionGUIConstructionEvent e) {
        final var pages = e.getPages();

        pages.add(new InterfacePage());
        pages.add(new WorldPage());
        if (!ChlorideConfig.modpackMode) pages.add(new DarknessPage());
        pages.add(new ParticlesPage());
        pages.add(new EntitiesPage());
        if (!ChlorideConfig.modpackMode) pages.add(new ZoomPage());
        if (ChlorideConfig.modpackMode) {
            LOGGER.info("Modpack Mode is enabled, skipping chloride True Darkness and Zoom page registration");
        }
    }

    private static Option<?> particles;

    @SubscribeEvent
    public static void onSodiumPagesRegister(final OptionGroupConstructionEvent e) {
        if (e.getId() != null && e.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
            final var options = e.getOptions();
            for (int i = 0; i < options.size(); i++) {
                final var id = options.get(i).getId();
                if (id != null && id.matches(StandardOptions.Option.FULLSCREEN)) {
                    options.set(i, getFullscreenOption());
                    options.add(i + 1, getBorderlessOptimizationOption());
                    break;
                }
            }
        }
        if (e.getId() != null && e.getId().toString().equals(StandardOptions.Group.DETAILS.toString())) {
            final var options = e.getOptions();
            for (final Option<?> option: options) {
                final var id = option.getId();
                if (id != null && id.matches(StandardOptions.Option.PARTICLES)) {
                    particles = option;
                    options.remove(option);
                    break;
                }
            }
        }

        if (e.getId() != null && e.getId().equals(ParticlesPage.PARTICLE_BASE_PAGE)) {
            final var options = e.getOptions();
            options.add(0, particles);
        }
    }

    @SubscribeEvent
    public static void onSodiumGroupRegister(final OptionPageConstructionEvent e) {
        if (e.getId() != null && e.getId().equals(StandardOptions.Pages.PERFORMANCE)) {
            final var builder = OptionGroup.createBuilder();

            final var fastChest = OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "fast_chests"))
                    .setName(Component.translatable("chloride.performance.fastchest.title"))
                    .setTooltip(Component.translatable("chloride.performance.fastchest.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> {
                                ChlorideConfig.fastChests = value;
                                MinecraftForge.EVENT_BUS.post(new FastModelSettingsUpdate.ChestEvent());
                            },
                            (opts) -> ChlorideConfig.fastChests)
                    .setImpact(OptionImpact.MEDIUM)
                    .setEnabledPredicate(FastBlocks::canUseOnChests)
                    .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD, OptionFlag.REQUIRES_GAME_RESTART)
                    .build();

            final var fastBeds = OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setId(ResourceLocation.tryBuild(ID, "fast_beds"))
                    .setName(Component.translatable("chloride.performance.fastbeds.title"))
                    .setTooltip(Component.translatable("chloride.performance.fastbeds.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> {
                                ChlorideConfig.fastBeds = value;
                                MinecraftForge.EVENT_BUS.post(new FastModelSettingsUpdate.BedEvent());
                            },
                            (opts) -> ChlorideConfig.fastBeds)
                    .setImpact(OptionImpact.MEDIUM)
                    .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD, OptionFlag.REQUIRES_GAME_RESTART)
                    .build();

            builder.add(fastChest);
            builder.add(fastBeds);

            e.addGroup(builder.build());
        }
    }

    private static Option<Mode> getFullscreenOption() {
        return OptionImpl.createBuilder(Mode.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "fullscreen"))
                .setName(Component.translatable("options.fullscreen"))
                .setTooltip(Component.translatable("chloride.general.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, Mode.class, new Component[] {
                        Component.translatable("chloride.general.screen.windowed"),
                        Component.translatable("chloride.general.screen.borderless"),
                        Component.translatable("chloride.general.screen.fullscreen")
                }))
                .setBinding(
                        (s, v) -> Borderless.setFullScreenMode(v),
                        (opts) -> ChlorideConfig.fullScreen
                ).build();
    }

    private static Option<Boolean> getBorderlessOptimizationOption() {
        return OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "borderless_optimizations"))
                .setName(Component.translatable("chloride.general.screen.borderless.optimization"))
                .setTooltip(Component.translatable("chloride.general.screen.borderless.optimization.desc"))
                .setControl(TickBoxControl::new)
                .setImpact(OptionImpact.HIGH)
                .setBinding(
                        (s, v) -> {
                            ChlorideConfig.disableBorderlessOptimizations = v;
                            Borderless.reloadFullscreenMode();
                        },
                        (opts) -> ChlorideConfig.disableBorderlessOptimizations
                ).build();
    }
}
