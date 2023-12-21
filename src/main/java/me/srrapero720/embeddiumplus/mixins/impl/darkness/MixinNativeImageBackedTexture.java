package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import com.mojang.blaze3d.platform.NativeImage;
import me.srrapero720.embeddiumplus.features.darkness.DarknessPlus;
import me.srrapero720.embeddiumplus.features.darkness.accessors.TextureAccess;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicTexture.class)
public class MixinNativeImageBackedTexture implements TextureAccess {
	@Shadow
	private NativeImage pixels;

	@Unique
	private boolean embPlus$enableHook = false;

	@Inject(method = "upload", at = @At(value = "HEAD"))
	private void onRenderWorld(CallbackInfo ci) {
		if (embPlus$enableHook && DarknessPlus.enabled) {
			final NativeImage img = pixels;
			for (int b = 0; b < 16; b++) {
				for (int s = 0; s < 16; s++) {
					final int color = DarknessPlus.darken(img.getPixelRGBA(b, s), b, s);
					img.setPixelRGBA(b, s, color);
				}
			}
		}
	}

	@Override
	public void embPlus$enableUploadHook() {
		embPlus$enableHook = true;
	}
}