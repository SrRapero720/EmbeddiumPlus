package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.srrapero720.embeddiumplus.mixins.EmbPlusOptions;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class EmbedtGUIMixin {
    @Shadow @Final private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        pages.add(EmbPlusOptions.getDynLightsPage());
    }

    @Redirect(
            method = "rebuildGUI",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                            ordinal = 3)
            ),
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            ),
            remap = false)
    private GuiEventListener redirectDonationStuff(SodiumOptionsGUI instance, GuiEventListener guiEventListener) {
        return null;
    }
}