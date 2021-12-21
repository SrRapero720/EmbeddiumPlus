package vice.magnesium_extras.mixins.Zoom;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.MathHelper;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.features.Zoom.ZoomUtils;

//This mixin is responsible for managing the fov-changing part of the zoom.
@Mixin(GameRenderer.class)
public class GameRendererMixin {
	//The zoom overlay's texture identifier.
	@Unique
	private static final ResourceLocation ZOOM_OVERLAY = new ResourceLocation("magnesium_extras:textures/misc/zoom_overlay.png");

	@Final
	@Shadow
	private Minecraft minecraft;

	//Handle transitioned zoom FOV multiplier and zoom overlay alphas each tick.
	@Inject(
		at = @At("HEAD"),
		method = "tick()V"
	)
	private void zoomTick(CallbackInfo info) {
		//If zoom transitions are enabled, update the zoom FOV multiplier.
		if (!MagnesiumExtrasConfig.zoomTransition.get().equals(MagnesiumExtrasConfig.ZoomTransitionOptions.OFF.toString())) {
			ZoomUtils.updateZoomFovMultiplier();
		}

		//If the zoom overlay is enabled, update the zoom overlay alpha.
		if (MagnesiumExtrasConfig.zoomOverlay.get()) {
			ZoomUtils.updateZoomOverlayAlpha();
		}
	}
	
	//Handles zooming of both modes (Transitionless and with Smooth Transitions).
	@Inject(
		at = @At("RETURN"),
		method = "getFov",
		cancellable = true
	)
	private void getZoomedFov(ActiveRenderInfo camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
		double fov = info.getReturnValue();

		if (!MagnesiumExtrasConfig.zoomTransition.get().equals(MagnesiumExtrasConfig.ZoomTransitionOptions.OFF.toString())) {
			//Handle the zoom with smooth transitions enabled.
			if (ZoomUtils.zoomFovMultiplier != 1.0F) {
				fov *= MathHelper.lerp(tickDelta, ZoomUtils.lastZoomFovMultiplier, ZoomUtils.zoomFovMultiplier);
				info.setReturnValue(fov);
			}
		} else {
			//Handle the zoom without smooth transitions.
			if (ZoomUtils.zoomState) {
				double zoomedFov = fov / ZoomUtils.zoomDivisor;
				info.setReturnValue(zoomedFov);
			}
		}

		//Regardless of the mode, if the zoom is over, update the terrain in order to stop terrain glitches.
		if (ZoomUtils.lastZoomState) {
			if (changingFov) {
				this.minecraft.levelRenderer.needsUpdate();
			}
		}
	}

	//This applies the zoom overlay itself.
	@Inject(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/GameSettings;hideGui:Z"),
		method = "render(FJZ)V"
	)
	public void injectZoomOverlay(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
		if (MagnesiumExtrasConfig.zoomOverlay.get()) {
			if (this.minecraft.options.hideGui) {
				return;
			}

			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableBlend();
			//If zoom transitions is on, apply the transition to the overlay.
			if (!MagnesiumExtrasConfig.zoomTransition.get().equals(MagnesiumExtrasConfig.ZoomTransitionOptions.OFF.toString())) {
				if (ZoomUtils.zoomFovMultiplier != 0.0F) {
					float transparency = MathHelper.lerp(tickDelta, ZoomUtils.lastZoomOverlayAlpha, ZoomUtils.zoomOverlayAlpha);
					this.renderZoomOverlay(transparency);
				}
			} else {
				//Else, just do a simple toggle on the overlay.
				if (ZoomUtils.zoomState) {
					this.renderZoomOverlay(1.0F);
				}
			}
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
     	 	RenderSystem.enableAlphaTest();
			RenderSystem.clear(256, Minecraft.ON_OSX);
		}
	}

	//This renders the zoom overlay.
	@Unique
	public void renderZoomOverlay(float f) {
		RenderSystem.disableAlphaTest();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, f);

		this.minecraft.getTextureManager().bind(ZOOM_OVERLAY);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

		bufferBuilder.vertex(0.0D, this.minecraft.getWindow().getGuiScaledHeight(), -90.0D).uv(0.0F, 1.0F).endVertex();
		bufferBuilder.vertex(this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), -90.0D).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex((double)this.minecraft.getWindow().getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();

		tessellator.end();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
