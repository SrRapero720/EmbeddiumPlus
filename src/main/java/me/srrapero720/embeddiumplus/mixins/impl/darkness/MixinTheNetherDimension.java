package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import me.srrapero720.embeddiumplus.foundation.darkness.DarknessPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.NetherEffects.class)
public class MixinTheNetherDimension {

	@Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
	private void onAdjustSkyColor(CallbackInfoReturnable<Vec3> ci) {
		if (EmbyConfig.darknessMode.get() == EmbyConfig.DarknessMode.OFF) return;
		if (!EmbyConfig.darknessOnNetherCache) return;

		final double factor = DarknessPlus.darkNetherFogBrightness();
		DarknessPlus.getDarkenedFogColor(ci, factor);
	}
}