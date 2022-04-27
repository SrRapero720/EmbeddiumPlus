/*******************************************************************************
 * Copyright 2019 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package vice.magnesium_extras.mixins.TotalDarkness;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.features.TotalDarkness.Darkness;

@Mixin(DimensionRenderInfo.Nether.class)
public class MixinTheNetherDimension {

	@Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
	private void onAdjustSkyColor(CallbackInfoReturnable<Vector3d> ci) {
		if (!MagnesiumExtrasConfig.trueDarknessEnabled.get())
			return;

		if (!MagnesiumExtrasConfig.darkNether.get())
			return;

		final double factor = Darkness.darkNetherFog();

		Darkness.getDarkenedFogColor(ci, factor);
	}
}
