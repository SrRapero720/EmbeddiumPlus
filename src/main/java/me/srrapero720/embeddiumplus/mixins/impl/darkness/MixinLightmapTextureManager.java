package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.srrapero720.embeddiumplus.features.true_darkness.LightmapAccess;
import me.srrapero720.embeddiumplus.features.true_darkness.TextureAccess;

@Mixin(LightTexture.class)
public class MixinLightmapTextureManager implements LightmapAccess {
	@Shadow @Final private DynamicTexture lightTexture;
	@Shadow private float blockLightRedFlicker;
	@Shadow private boolean updateLightTexture;

	@Inject(method = "<init>*", at = @At(value = "RETURN"))
	private void afterInit(GameRenderer gameRenderer, Minecraft minecraftClient, CallbackInfo ci) {
		((TextureAccess) lightTexture).darkness_enableUploadHook();
	}

	@Override
	public float darkness_prevFlicker() {
		return blockLightRedFlicker;
	}

	@Override
	public boolean darkness_isDirty() {
		return updateLightTexture;
	}
}