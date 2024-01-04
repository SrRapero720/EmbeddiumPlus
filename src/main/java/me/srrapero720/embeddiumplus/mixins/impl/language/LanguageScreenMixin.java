package me.srrapero720.embeddiumplus.mixins.impl.language;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(LanguageSelectScreen.class)
public class LanguageScreenMixin extends OptionsSubScreen {
    public LanguageScreenMixin(Screen screen, Options options, Component component) { super(screen, options, component); }

    @Redirect(method = "onDone", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;"))
    public CompletableFuture<Void> redirectResourceManagerReload(Minecraft instance) {
        this.minecraft.getLanguageManager().onResourceManagerReload(this.minecraft.getResourceManager());
        return null;
    }
}