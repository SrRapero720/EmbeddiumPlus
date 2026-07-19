package me.srrapero720.chloride.mixins.impl.jei_rei_emi;

import me.srrapero720.chloride.ChlorideConfig;
import mezz.jei.gui.elements.IconButton;
import mezz.jei.gui.input.GuiTextFieldFilter;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// JEI GUI INTERNALS ARE NOT API AND CHANGE BETWEEN PATCH RELEASES (SEE ISSUE #172, JEI 19.32.0 REMOVED
// screenPropertiesCache AND IngredientGridWithNavigation). ONLY SHADOW FIELDS PRESENT IN EVERY KNOWN
// 1.21.1 BUILD AND KEEP EVERY INJECTOR require = 0 SO A FUTURE REFACTOR DEGRADES TO A NO-OP, NEVER A CRASH.
@Mixin(value = IngredientListOverlay.class, remap = false)
@Pseudo
public class JeiOverlayMixin {
    @Shadow @Final private GuiTextFieldFilter searchField;
    @Shadow @Final private IconButton configButton;

    // JEI <= 19.21.x: contents drawn through IngredientGridWithNavigation
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;draw(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"), cancellable = true, require = 0)
    public void inject$renderOverlayLegacy(final Minecraft minecraft, final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        this.chloride$hideOverlay(minecraft, guiGraphics, mouseX, mouseY, partialTicks, ci);
    }

    // JEI >= 19.32.x: contents refactored behind IIngredientListOverlayContents
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IIngredientListOverlayContents;draw(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"), cancellable = true, require = 0)
    public void inject$renderOverlayModern(final Minecraft minecraft, final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        this.chloride$hideOverlay(minecraft, guiGraphics, mouseX, mouseY, partialTicks, ci);
    }

    @Inject(method = "drawTooltips", at = @At(value = "HEAD"), cancellable = true, require = 0)
    public void inject$renderTooltips(final Minecraft minecraft, final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final CallbackInfo ci) {
        if (!ChlorideConfig.ui.hideJREMI) return;

        if (this.searchField.getValue().isEmpty()) {
            ci.cancel();
        }
    }

    @Unique
    private void chloride$hideOverlay(final Minecraft minecraft, final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        if (!ChlorideConfig.ui.hideJREMI) return;
        if (!this.searchField.getValue().isEmpty()) return;

        if (minecraft.screen != null) {
            this.configButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
            if (!ChlorideConfig.ui.hideJREMIHint) {
                guiGraphics.centeredText(minecraft.font, Component.translatable("chloride.jei.message"), this.searchField.getX() + (this.searchField.getWidth() / 2), guiGraphics.guiHeight() / 2, 0xFFFFFFFF);
            }
        }
        ci.cancel();
    }
}
