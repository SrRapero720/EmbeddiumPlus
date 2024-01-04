package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.srrapero720.embeddiumplus.foundation.embeddium.pages.DynamicLightsPage;
import me.srrapero720.embeddiumplus.foundation.embeddium.pages.QualityPlusPage;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false, priority = 100/* Prevents other forks of sodium extra stay above emb++*/)
public class OptionsGUIMixin {
    @Shadow @Final private List<OptionPage> pages;

    // TODO: we can't inject on constructors :P
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private <E> boolean redirect$qualityPage(List<E> instance, E e) {
        instance.add(e);
        pages.add(new QualityPlusPage());
        return true;
    }

    // TODO: we can't inject on constructors :P
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    private <E> boolean redirect$performancePage(List<E> instance, E e) {
        instance.add(e);
        pages.add(new QualityPlusPage());
        return true;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$dynLightsPage(Screen prevScreen, CallbackInfo ci) {
        pages.add(new DynamicLightsPage());
    }
}