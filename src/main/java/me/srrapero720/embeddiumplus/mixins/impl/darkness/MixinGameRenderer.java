package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.embeddiumplus.foundation.darkness.DarknessPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
	@Shadow @Final public Minecraft minecraft;
	@Shadow @Final public LightTexture lightTexture;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void inject$renderLevel(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
		var lightmap = (LightTextureAccessor) lightTexture;

		if (lightmap.isDirty()) {
			minecraft.getProfiler().push("lightTex");
			DarknessPlus.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmap.getFlicker());
			minecraft.getProfiler().pop();
		}
	}
}