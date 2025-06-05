package me.srrapero720.chloride.mixins.impl.sodium;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.FastBlocks;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

@Mixin(SodiumGameOptionPages.class)
public class SodiumOptionsMixin {

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 4))
    private static OptionGroup.Builder redirect$fullscreenPage(OptionGroup.Builder instance, Option<?> option) {

        instance.add(OptionImpl.createBuilder(Borderless.Mode.class, STORAGE)
                .setName(Component.translatable("options.fullscreen"))
                .setTooltip(Component.translatable("chloride.general.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, Borderless.Mode.class, new Component[] {
                        Component.translatable("chloride.general.screen.windowed"),
                        Component.translatable("chloride.general.screen.borderless"),
                        Component.translatable("chloride.general.screen.fullscreen")
                }))
                .setBinding(
                        (s, g) -> Borderless.setFullScreenMode(g),
                        (opts) -> ChlorideConfig.fullScreen
                ).build()
        );

        instance.add(OptionImpl.createBuilder(boolean.class, STORAGE)
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
                ).build()
        );

        return instance;
    }

    @WrapOperation(method = "performance", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static ImmutableList<OptionGroup> inject$performance(Collection<OptionGroup> list, Operation<ImmutableList<OptionGroup>> original) {
        final var builder = OptionGroup.createBuilder();

        final var fastChest = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.performance.fastchest.title"))
                .setTooltip(Component.translatable("chloride.performance.fastchest.desc"))
                .setControl(TickBoxControl::new)
                .setEnabled(() -> FastBlocks.SOLID_CHESTS_PACK != null)
                .setBinding(
                        (opts, value) -> {
                            ChlorideConfig.fastChests = value;
                            NeoForge.EVENT_BUS.post(new FastModelSettingsUpdate.ChestEvent());
                        },
                        (opts) -> ChlorideConfig.fastChests)
                .setImpact(OptionImpact.MEDIUM)
                .setEnabled(FastBlocks::canUseOnChests)
                .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                .build();

        final var fastBeds = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.performance.fastbeds.title"))
                .setTooltip(Component.translatable("chloride.performance.fastbeds.desc"))
                .setControl(TickBoxControl::new)
                .setEnabled(() -> FastBlocks.SOLID_BEDS_PACK != null)
                .setBinding(
                        (opts, value) -> {
                            ChlorideConfig.fastBeds = value;
                            NeoForge.EVENT_BUS.post(new FastModelSettingsUpdate.BedEvent());
                        },
                        (opts) -> ChlorideConfig.fastBeds)
                .setImpact(OptionImpact.MEDIUM)
                .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                .build();

        builder.add(fastChest);
        builder.add(fastBeds);

        list.add(builder.build());
        return original.call(list);
    }
}
