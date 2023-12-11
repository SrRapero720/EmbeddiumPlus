package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.srrapero720.embeddiumplus.features.true_darkness.Darkness;
import me.srrapero720.embeddiumplus.features.true_darkness.LightmapAccess;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
	@Shadow @Final public Minecraft minecraft;
	@Shadow @Final public LightTexture lightTexture;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void onRenderWorld(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
		if (lightTexture instanceof LightmapAccess lightmap) {
			if (lightmap.darkness_isDirty()) {
				minecraft.getProfiler().push("lightTex");
				Darkness.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmap.darkness_prevFlicker());
				minecraft.getProfiler().pop();
			}
		}
	}
}