package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.srrapero720.embeddiumplus.foundation.embeddium.pages.*;
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
public class EmbOptionsMixin {
    @Shadow @Final private List<OptionPage> pages;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private <E> boolean redirect$qualityPage(List instance, E e, Operation<Boolean> original) {
        instance.add(e);
        pages.add(new QualityPlusPage());
        return true;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$dynLightsPage(Screen prevScreen, CallbackInfo ci) {
        pages.add(new TrueDarknessPage());
        pages.add(new EntityCullingPage());
        pages.add(new DynamicLightsPage());
        pages.add(new OthersPage());
    }
}