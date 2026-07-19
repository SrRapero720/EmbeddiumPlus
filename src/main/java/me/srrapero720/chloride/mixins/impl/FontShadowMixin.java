package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class FontShadowMixin {
    // EVERY GUI AND IN-WORLD TEXT FUNNELS THROUGH THESE prepareText OVERLOADS; FORCING THE FLAG OFF KILLS ALL SHADOWS
    @ModifyVariable(method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    public boolean modify$dropShadow(final boolean drawShadow) {
        return drawShadow && ChlorideConfig.ui.fontShadows;
    }

    @ModifyVariable(method = "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    public boolean modify$dropShadowSeq(final boolean drawShadow) {
        return drawShadow && ChlorideConfig.ui.fontShadows;
    }
}
