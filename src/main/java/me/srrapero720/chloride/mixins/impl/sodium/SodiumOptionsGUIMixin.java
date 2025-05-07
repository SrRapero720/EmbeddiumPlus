package me.srrapero720.chloride.mixins.impl.sodium;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.sodium.pages.*;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static me.srrapero720.chloride.Chloride.LOGGER;

@Mixin(value = SodiumOptionsGUI.class, priority = 0)
public class SodiumOptionsGUIMixin {

    @Shadow @Final private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$constructor(Screen prevScreen, CallbackInfo ci) {
        this.pages.add(new InterfacePage());
        this.pages.add(new WorldPage());
        if (!ChlorideConfig.modpackMode) pages.add(new DarknessPage());
        this.pages.add(new ParticlesPage());
        this.pages.add(new EntitiesPage());
        if (!ChlorideConfig.modpackMode) pages.add(new ZoomPage());
        if (ChlorideConfig.modpackMode) {
            LOGGER.info("Modpack Mode is enabled, skipping chloride True Darkness and Zoom page registration");
        }
    }
}
