package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.internal.EmbPlusOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class OptionPagesMixin {
    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 4))
    private static OptionGroup.Builder redirectFullScreenOption(OptionGroup.Builder instance, Option<?> option) {
        instance.add(EmbPlusOptions.getFullscreenOption(vanillaOpts));
        return instance;
    }

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 8), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injectFPSOption(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        EmbPlusOptions.setFPSOptions(groups, sodiumOpts, vanillaOpts);
    }

    @Inject(method = "performance", at = @At(value = "NEW", target = "(Lnet/minecraft/network/chat/Component;Lcom/google/common/collect/ImmutableList;)Lme/jellysquid/mods/sodium/client/gui/options/OptionPage;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void setPerformanceOptions(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        EmbPlusOptions.setPerformanceOptions(groups, sodiumOpts, vanillaOpts);
    }

    @Unique private static OptionGroup.Builder embPlus$advancedBuilder;
    @Redirect(method = "advanced", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;"))
    private static OptionGroup.Builder regroup() {
        if (embPlus$advancedBuilder == null) embPlus$advancedBuilder = OptionGroup.createBuilder();
        return embPlus$advancedBuilder;
    }

    @Redirect(method = "advanced", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static <E> boolean unlist(List<E> instance, E e) {
        return true; // NO-OP
    }

    @Inject(method = "advanced", at = @At(value = "NEW", target = "(Lnet/minecraft/network/chat/Component;Lcom/google/common/collect/ImmutableList;)Lme/jellysquid/mods/sodium/client/gui/options/OptionPage;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void relist(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        groups.add(embPlus$advancedBuilder.build());
        embPlus$advancedBuilder = null;
    }

    @Inject(method = "advanced", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void changeCategoryName(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        cir.setReturnValue(EmbPlusOptions.setPerformancePlusOptions(groups, sodiumOpts));
    }
}