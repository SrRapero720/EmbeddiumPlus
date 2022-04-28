package vice.rubidium_extras.mixins.Zoom;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.rubidium_extras.WiZoom;
import vice.rubidium_extras.features.Keybinding.KeyboardInput;

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