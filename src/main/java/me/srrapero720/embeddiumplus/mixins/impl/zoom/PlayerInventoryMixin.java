package me.srrapero720.embeddiumplus.mixins.impl.zoom;

import me.srrapero720.embeddiumplus.foundation.keybinding.KeyboardInput;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class PlayerInventoryMixin
{
    @Inject(at = {@At("HEAD")},
            method = {"swapPaint(D)V"},
            cancellable = true)
    private void onScrollInHotbar(double scrollAmount, CallbackInfo ci)
    {
        if(KeyboardInput.zoomKey.isDown())
            ci.cancel();
    }
}