package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import me.srrapero720.embeddiumplus.EmbPlusConfig;
import me.srrapero720.embeddiumplus.features.darkness.DarknessPlus;
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
		if (!EmbPlusConfig.trueDarknessEnabled.get()) return;

		if (!EmbPlusConfig.darkNether.get()) return;

		final double factor = DarknessPlus.darkNetherFog();

		DarknessPlus.getDarkenedFogColor(ci, factor);
	}
}