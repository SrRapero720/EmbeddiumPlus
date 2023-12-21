package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.srrapero720.embeddiumplus.internal.EmbPlusPages;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class OptionsGUIMixin {
    @Shadow @Final private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void dynamicLightsPage(Screen prevScreen, CallbackInfo ci) {
        pages.add(EmbPlusPages.getDynLightsPage());
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private <E> boolean qualityPlusPage(List<E> instance, E e) {
        instance.add(e);
        pages.add(EmbPlusPages.getQualityPlusPage());
        return true;
    }
}