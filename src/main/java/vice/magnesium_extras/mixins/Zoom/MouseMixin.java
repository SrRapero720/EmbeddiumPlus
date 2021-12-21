package vice.magnesium_extras.mixins.Zoom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.util.MouseSmoother;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.features.Zoom.ZoomUtils;
import vice.magnesium_extras.keybinds.KeyboardInput;


//This mixin is responsible for the mouse-behavior-changing part of the zoom.
@Mixin(MouseHelper.class)
public class MouseMixin {
	@Final
	@Shadow
	private Minecraft minecraft;
	
	@Final
	@Shadow
	private final MouseSmoother smoothTurnX = new MouseSmoother();
	
	@Final
	@Shadow
	private final MouseSmoother smoothTurnY = new MouseSmoother();

	@Shadow
	private double accumulatedDX;

	@Shadow
	private double accumulatedDY;
	
	@Shadow
	private double accumulatedScroll;
	
	@Unique
	private final MouseSmoother cursorXZoomSmoother = new MouseSmoother();

	@Unique
	private final MouseSmoother cursorYZoomSmoother = new MouseSmoother();

	@Unique
	private double extractedE;
	@Unique
	private double adjustedG;
	
	//This mixin handles the "Reduce Sensitivity" option and extracts the g variable for the cinematic cameras.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;minecraft:Lnet/minecraft/client/Minecraft;", ordinal = 2),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyReduceSensitivity(double g) {
		double modifiedMouseSensitivity = this.minecraft.options.sensitivity;

		if (MagnesiumExtrasConfig.lowerZoomSensitivity.get())
		{
			if (!MagnesiumExtrasConfig.zoomTransition.get().equals(MagnesiumExtrasConfig.ZoomTransitionOptions.OFF.toString())) {
				modifiedMouseSensitivity *= ZoomUtils.zoomFovMultiplier;
			} else if (ZoomUtils.zoomState) {
				modifiedMouseSensitivity /= ZoomUtils.zoomDivisor;
			}
		}

		double appliedMouseSensitivity = modifiedMouseSensitivity * 0.6 + 0.2;
		g = appliedMouseSensitivity * appliedMouseSensitivity * appliedMouseSensitivity * 8.0;
		this.adjustedG = g;
		return g;
	}
	
	//Extracts the e variable for the cinematic cameras.
	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHelper;isMouseGrabbed()Z"),
		method = "turnPlayer",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void obtainCinematicCameraValues(CallbackInfo info, double d, double e) {
		this.extractedE = e;
	}

	//Applies the cinematic camera on the mouse's X.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedDX:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyCinematicModeX(double l) {
		if (!MagnesiumExtrasConfig.cinematicCameraMode.get().equals(MagnesiumExtrasConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCamera) {
					l = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * this.adjustedG, (this.extractedE * this.adjustedG));
					this.cursorXZoomSmoother.reset();
				} else {
					l = this.cursorXZoomSmoother.getNewDeltaValue(this.accumulatedDX * this.adjustedG, (this.extractedE * this.adjustedG));
				}
				if (MagnesiumExtrasConfig.cinematicCameraMode.get().equals(MagnesiumExtrasConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					l *= MagnesiumExtrasConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.cursorXZoomSmoother.reset();
			}
		}
		
		return l;
	}
	
	//Applies the cinematic camera on the mouse's Y.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedDY:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyCinematicModeY(double m) {
		if (!MagnesiumExtrasConfig.cinematicCameraMode.get().equals(MagnesiumExtrasConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCamera) {
					m = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * this.adjustedG, (this.extractedE * this.adjustedG));
					this.cursorYZoomSmoother.reset();
				} else {
					m = this.cursorYZoomSmoother.getNewDeltaValue(this.accumulatedDY * this.adjustedG, (this.extractedE * this.adjustedG));
				}
				if (MagnesiumExtrasConfig.cinematicCameraMode.get().equals(MagnesiumExtrasConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					m *= MagnesiumExtrasConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.cursorYZoomSmoother.reset();
			}
		}
		
		return m;
	}
	
	//Handles zoom scrolling.
	@Inject(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedScroll:D", ordinal = 7),
		method = "onScroll",
		cancellable = true
	)
	private void zoomerOnMouseScroll(CallbackInfo info) {
		if (this.accumulatedScroll != 0.0) {
			if (MagnesiumExtrasConfig.zoomScrolling.get()) {
				if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
					if (!KeyboardInput.zoomKey.isDown())
					{
						return;
					}
				}
				
				if (ZoomUtils.zoomState) {
					if (this.accumulatedScroll > 0.0) {
						ZoomUtils.changeZoomDivisor(true);
					} else if (this.accumulatedScroll < 0.0) {
						ZoomUtils.changeZoomDivisor(false);
					}
					
					info.cancel();
				}
			}
		}
	}

	//Handles the zoom scrolling reset through the middle button.
	@Inject(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;set(Lnet/minecraft/client/util/InputMappings$Input;Z)V"),
			method = "onPress(JIII)V",
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void zoomerOnMouseButton(long window, int button, int action, int mods, CallbackInfo info, boolean bl, int i) {
		if (MagnesiumExtrasConfig.zoomScrolling.get()) {
			if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
				if (!KeyboardInput.zoomKey.isDown()) {
					return;
				}
			}

			if (button == 2 && bl) {
				if (KeyboardInput.zoomKey.isDown()) {
					ZoomUtils.resetZoomDivisor();
					info.cancel();
				}
			}
		}
	}
}