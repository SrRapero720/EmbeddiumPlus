package me.srrapero720.chloride.mixins.impl.sodium;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.Tools;
import me.srrapero720.chloride.features.sodium.ChlorideOptions;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

@Mixin(SodiumGameOptionPages.class)
public class    SodiumOptionsMixin {

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 4))
    private static OptionGroup.Builder redirect$fullscreenPage(OptionGroup.Builder instance, Option<?> option) {
        instance.add(ChlorideOptions.getFullscreenOption());
        return instance;
    }

    @Inject(method = "performance", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void inject$performance(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        var builder = OptionGroup.createBuilder();
        var fontShadow = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.fontshadow.title"))
                .setTooltip(Component.translatable("chloride.options.fontshadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fontShadows = value,
                        (opts) -> ChlorideConfig.fontShadows)
                .setImpact(OptionImpact.VARIES)
                .build();

        var leavesCulling = OptionImpl.createBuilder(ChlorideConfig.LeavesCullingMode.class, STORAGE)
                .setName(Component.translatable("chloride.options.leaves_culling.title"))
                .setTooltip(Component.translatable("chloride.options.leaves_culling.desc"))
                .setControl(opt -> new CyclingControl<>(opt, ChlorideConfig.LeavesCullingMode.class, new Component[] {
                        Component.translatable("chloride.options.leaves_culling.all"),
                        Component.translatable("chloride.options.leaves_culling.off")
                }))
                .setBinding((opt, v) -> ChlorideConfig.leavesCulling = v,
                        (opts) -> ChlorideConfig.leavesCulling)
                .setImpact(OptionImpact.HIGH)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var fastChest = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.fastchest.title"))
                .setTooltip(Component.translatable("chloride.options.fastchest.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fastChests = value,
                        (opts) -> ChlorideConfig.fastChests)
                .setImpact(OptionImpact.HIGH)
//                .setEnabled(FastModels.canUseOnChests())
                .setEnabled(() -> false)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var fastBeds = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.fastbeds.title"))
                .setTooltip(Component.translatable("chloride.options.fastbeds.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.fastBeds = value,
                        (opts) -> ChlorideConfig.fastBeds)
                .setImpact(OptionImpact.LOW)
//                .setEnabled(EmbyTools.isFlywheelOff())
                .setEnabled(() -> false)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var hideJEI = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.jei.title"))
                .setTooltip(Component.translatable("chloride.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opts, value) -> ChlorideConfig.hideJREMI = value,
                        (opts) -> ChlorideConfig.hideJREMI)
                .setImpact(OptionImpact.LOW)
                .setEnabled(() -> Tools.isModInstalled("jei") || Tools.isModInstalled("roughlyenoughitems") || Tools.isModInstalled("emi"))
                .build();

        builder.add(leavesCulling);
        builder.add(fontShadow);
        builder.add(fastChest);
        builder.add(fastBeds);
        builder.add(hideJEI);

        groups.add(builder.build());
    }
}
