package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;

@Mixin(LanguageSelectScreen.class)
public class QuickLanguageReloadMixin extends OptionsSubScreen {
    public QuickLanguageReloadMixin(final Screen screen, final Options options, final Component component) { super(screen, options, component); }

    @WrapOperation(method = "onDone", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;"))
    public CompletableFuture<Void> redirect$resourcesReload(final Minecraft instance, final Operation<CompletableFuture<Void>> original) {
        if (ChlorideConfig.fastLanguageReload) {
            this.minecraft.getLanguageManager().onResourceManagerReload(this.minecraft.getResourceManager());
            return null;
        } else {
            return original.call(instance);
        }
    }
}