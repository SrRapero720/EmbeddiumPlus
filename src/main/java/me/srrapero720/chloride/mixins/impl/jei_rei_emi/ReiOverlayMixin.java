package me.srrapero720.chloride.mixins.impl.jei_rei_emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.impl.client.REIRuntimeImpl;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import me.shedaniel.rei.impl.client.gui.widget.entrylist.EntryListWidget;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ScreenOverlayImpl.class, priority = 500, remap = false)
@Pseudo
public class ReiOverlayMixin {
    @WrapOperation(method = "renderWidgets", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/client/gui/widgets/Widget;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    private void redirect$rendering(Widget instance, GuiGraphics graphics, int mouseX, int mouseY, float deltaTick, Operation<Void> original) {
        if (instance instanceof EntryListWidget) {
            if (!ChlorideConfig.hideJREMI || !REIRuntimeImpl.getSearchField().getText().isEmpty()) {
                original.call(instance, graphics, mouseX, mouseY, deltaTick);
            }
        } else {
            original.call(instance, graphics, mouseX, mouseY, deltaTick);
        }
    }
}