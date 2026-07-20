package me.srrapero720.chloride.mixins.impl.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.FastBlocks;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;
import static me.srrapero720.chloride.Chloride.id;

/**
 * Injects Chloride's additive toggles directly into Sodium's own option pages. The public config API can replace or
 * overlay existing options (see {@code ChlorideConfigIntegration#registerOptionReplacement} for the fullscreen swap)
 * but cannot add brand-new options to another mod's page, so these are appended at the end of the relevant page.
 */
@Mixin(SodiumConfigBuilder.class)
public class SodiumConfigBuilderMixin {

    @WrapOperation(
            method = "buildGeneralPage",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=general.graphics_api")),
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;addOption(Lnet/caffeinemc/mods/sodium/api/config/structure/OptionBuilder;)Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;", ordinal = 0)
    )
    private OptionGroupBuilder chloride$hotswapToggle(final OptionGroupBuilder group, final OptionBuilder option, final Operation<OptionGroupBuilder> original, @Local(argsOnly = true) final ConfigBuilder b) {
        original.call(group, option);
        return group.addOption(b.createBooleanOption(id("hotswap"))
                .setName(Component.translatable("chloride.general.hotswap"))
                .setTooltip(Component.translatable("chloride.general.hotswap.desc"))
                .setStorageHandler(STORAGE)
                .setDefaultValue(false)
                .setBinding(v -> ChlorideConfig.hotswap = v, () -> ChlorideConfig.hotswap));
    }

    @ModifyReturnValue(method = "buildGeneralPage", at = @At("RETURN"))
    private OptionPageBuilder chloride$general(final OptionPageBuilder page, @Local(argsOnly = true) final ConfigBuilder b) {
        return page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(id("disableBorderlessOptimizations"))
                        .setName(Component.translatable("chloride.general.screen.borderless.optimization"))
                        .setTooltip(Component.translatable("chloride.general.screen.borderless.optimization.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(false)
                        .setBinding(v -> {
                            ChlorideConfig.fullscreen.disableBorderlessOptimizations = v;
                            Borderless.reloadFullscreenMode();
                        }, () -> ChlorideConfig.fullscreen.disableBorderlessOptimizations)));
    }

    @ModifyReturnValue(method = "buildPerformancePage", at = @At("RETURN"))
    private OptionPageBuilder chloride$performance(final OptionPageBuilder page, @Local(argsOnly = true) final ConfigBuilder b) {
        return page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(id("fastChests"))
                        .setName(Component.translatable("chloride.performance.fastchest.title"))
                        .setTooltip(Component.translatable("chloride.performance.fastchest.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD, OptionFlag.REQUIRES_GAME_RESTART)
                        .setEnabled(FastBlocks.canUseOnChests())
                        .setDefaultValue(false)
                        .setBinding(v -> {
                            ChlorideConfig.fastBlocks.chests = v;
                            NeoForge.EVENT_BUS.post(new FastModelSettingsUpdate.ChestEvent());
                        }, () -> ChlorideConfig.fastBlocks.chests))
                .addOption(b.createBooleanOption(id("fastBeds"))
                        .setName(Component.translatable("chloride.performance.fastbeds.title"))
                        .setTooltip(Component.translatable("chloride.performance.fastbeds.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD, OptionFlag.REQUIRES_GAME_RESTART)
                        .setDefaultValue(false)
                        .setBinding(v -> {
                            ChlorideConfig.fastBlocks.beds = v;
                            NeoForge.EVENT_BUS.post(new FastModelSettingsUpdate.BedEvent());
                        }, () -> ChlorideConfig.fastBlocks.beds)));
    }
}
