package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.srrapero720.embeddiumplus.foundation.embeddium.pages.*;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false, priority = 100/* Prevents other forks of sodium extra stay above emb++*/)
public class EmbOptionsMixin {
    @Shadow @Final private List<OptionPage> pages;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private <E> boolean redirect$qualityPage(List<E> instance, E e, Operation<Boolean> original) {
        original.call(instance, e);
        return pages.add(new QualityPlusPage());
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$dynLightsPage(Screen prevScreen, CallbackInfo ci) {
        pages.add(new TrueDarknessPage());
        pages.add(new EntityCullingPage());
        pages.add(new OthersPage());
    }
}