package me.srrapero720.chloride.mixins.impl.jei_rei_emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.config.SidebarType;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.widget.EmiSearchWidget;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EmiScreenManager.class, remap = false)
@Pseudo
public class EmiOverlayMixin {
    @Shadow public static EmiSearchWidget search;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager$SidebarPanel;render(Ldev/emi/emi/runtime/EmiDrawContext;IIF)V"))
    private static void inject$renderStackOverlay(final EmiScreenManager.SidebarPanel instance, final EmiDrawContext ctx, final int i, final int context, final float mouseX, final Operation<Void> original) {
        if (!ChlorideConfig.hideJREMI) {
            original.call(instance, ctx, i, context, mouseX);
        } else {
            if (instance.getType() == SidebarType.INDEX && search.getValue().isEmpty()) {
                final Bounds bounds = instance.getBounds();
                ctx.drawCenteredTextWithShadow(Component.translatable("chloride.jei.message"), bounds.x() + (bounds.width() / 2), bounds.y() + (bounds.height() / 2), 0xFFFFFF);
                return;
            }
            original.call(instance, ctx, i, context, mouseX);
        }
    }
}