package me.srrapero720.embeddium_extras.mixins.SodiumConfig;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class SodiumGUIMixin {
    @Redirect(
            method = "rebuildGUI",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 3),
            remap = false
    )
    private GuiEventListener Inject(SodiumOptionsGUI instance, GuiEventListener guiEventListener) {
        return null;
    }

    @Redirect(
            method = "rebuildGUI",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 4),
            remap = false
    )
    private GuiEventListener InjectTwo(SodiumOptionsGUI instance, GuiEventListener guiEventListener) {
        return null;
    }
}