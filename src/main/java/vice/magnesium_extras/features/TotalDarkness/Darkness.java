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
package vice.magnesium_extras.features.TotalDarkness;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;


@Mod.EventBusSubscriber(modid = "magnesium_extras", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Darkness
{
	@SubscribeEvent
	public static void onConfigChange(ModConfig.ModConfigEvent e) {
		bake();
	}

	public static void bake() {
		MagnesiumExtrasConfig.darkNetherFogEffective = MagnesiumExtrasConfig.darkNether.get() ? MagnesiumExtrasConfig.darkNetherFogConfigured.get() : 1.0;
		MagnesiumExtrasConfig.darkEndFogEffective = MagnesiumExtrasConfig.darkEnd.get() ? MagnesiumExtrasConfig.darkEndFogConfigured.get() : 1.0;
	}

	public static boolean blockLightOnly() {
		return MagnesiumExtrasConfig.blockLightOnly.get();
	}

	public static double darkNetherFog() {
		return MagnesiumExtrasConfig.darkNetherFogEffective;
	}

	public static double darkEndFog() {
		return MagnesiumExtrasConfig.darkEndFogEffective;
	}

	public static void getDarkenedFogColor(CallbackInfoReturnable<Vector3d> ci, double factor)
	{
		if (factor != 1.0) {
			Vector3d result = ci.getReturnValue();
			double MIN = 0.03D;
			result = new Vector3d(Math.max(MIN, result.x * factor), Math.max(MIN, result.y * factor), Math.max(MIN, result.z * factor));
			ci.setReturnValue(result);
		}
	}

	private static boolean isDark(World world) {
		if (!MagnesiumExtrasConfig.trueDarknessEnabled.get())
			return false;

		final RegistryKey<World> dimType = world.dimension();
		if (dimType == World.OVERWORLD) {
			return MagnesiumExtrasConfig.darkOverworld.get();
		} else if (dimType == World.NETHER) {
			return MagnesiumExtrasConfig.darkNether.get();
		} else if (dimType == World.END) {
			return MagnesiumExtrasConfig.darkEnd.get();
		} else if (world.dimensionType().hasSkyLight()) {
			return MagnesiumExtrasConfig.darkDefault.get();
		} else {
			return MagnesiumExtrasConfig.darkSkyless.get();
		}
	}

	private static float skyFactor(World world) {
		if (!MagnesiumExtrasConfig.blockLightOnly.get() && isDark(world)) {
			if (world.dimensionType().hasSkyLight()) {
				final float angle = world.getTimeOfDay(0);
				if (angle > 0.25f && angle < 0.75f) {
					final float oldWeight = Math.max(0, (Math.abs(angle - 0.5f) - 0.2f)) * 20;
					final float moon = MagnesiumExtrasConfig.ignoreMoonPhase.get() ? 0 : world.getMoonBrightness();
					final float moonInterpolated = (float) MathHelper.lerp(moon, MagnesiumExtrasConfig.minimumMoonLevel.get(), MagnesiumExtrasConfig.maximumMoonLevel.get());
					return MathHelper.lerp(oldWeight * oldWeight * oldWeight, moonInterpolated, 1f) ;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	public static boolean enabled = false;
	private static final float[][] LUMINANCE = new float[16][16];

	public static int darken(int c, int blockIndex, int skyIndex) {
		final float lTarget = LUMINANCE[blockIndex][skyIndex];
		final float r = (c & 0xFF) / 255f;
		final float g = ((c >> 8) & 0xFF) / 255f;
		final float b = ((c >> 16) & 0xFF) / 255f;
		final float l = luminance(r, g, b);
		final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

		return f == 1f ? c : 0xFF000000 | Math.round(f * r * 255) | (Math.round(f * g * 255) << 8) | (Math.round(f * b * 255) << 16);
	}

	public static float luminance(float r, float g, float b) {
		return r * 0.2126f + g * 0.7152f + b * 0.0722f;
	}

	public static void updateLuminance(float tickDelta, Minecraft client, GameRenderer worldRenderer, float prevFlicker) {
		final ClientWorld world = client.level;
		if (world != null) {

			if (!isDark(world) || client.player.hasEffect(Effects.NIGHT_VISION) ||
							(client.player.hasEffect(Effects.CONDUIT_POWER) && client.player.getWaterVision() > 0) || world.getSkyFlashTime() > 0) {
				enabled = false;
				return;
			} else {
				enabled = true;
			}

			final float dimSkyFactor = Darkness.skyFactor(world);
			final float ambient = world.getSkyDarken(1.0F);
			final DimensionType dim = world.dimensionType();
			final boolean blockAmbient = !Darkness.isDark(world);

			for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
				float skyFactor = 1f - skyIndex / 15f;
				skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
				skyFactor *= dimSkyFactor;

				float min = Math.max(skyFactor * 0.05f, MagnesiumExtrasConfig.darknessOption.get().value);
				final float rawAmbient = ambient * skyFactor;
				final float minAmbient = rawAmbient * (1 - min) + min;
				final float skyBase = dim.brightness(skyIndex) * minAmbient;

				min = Math.max(0.35f * skyFactor, MagnesiumExtrasConfig.darknessOption.get().value);
				float v = skyBase * (rawAmbient * (1 - min) + min);
				float skyRed = v;
				float skyGreen = v;
				float skyBlue = skyBase;

				if (worldRenderer.getDarkenWorldAmount(tickDelta) > 0.0F) {
					final float skyDarkness = worldRenderer.getDarkenWorldAmount(tickDelta);
					skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
					skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
					skyBlue = skyBlue * (1.0F - skyDarkness) + skyBlue * 0.6F * skyDarkness;
				}

				for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
					float blockFactor = 1f;
					if (!blockAmbient) {
						blockFactor = 1f - blockIndex / 15f;
						blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;
					}

					final float blockBase = blockFactor * dim.brightness(blockIndex) * (prevFlicker * 0.1F + 1.5F);
					min = 0.4f * blockFactor;
					final float blockGreen = blockBase * ((blockBase * (1 - min) + min) * (1 - min) + min);
					final float blockBlue = blockBase * (blockBase * blockBase * (1 - min) + min);

					float red = skyRed + blockBase;
					float green = skyGreen + blockGreen;
					float blue = skyBlue + blockBlue;

					final float f = Math.max(skyFactor, blockFactor);
					min = 0.03f * f;
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					//the end
					if (world.dimension() == World.END) {
						red = skyFactor * 0.22F + blockBase * 0.75f;
						green = skyFactor * 0.28F + blockGreen * 0.75f;
						blue = skyFactor * 0.25F + blockBlue * 0.75f;
					}

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					final float gamma = (float) client.options.gamma * f;
					float invRed = 1.0F - red;
					float invGreen = 1.0F - green;
					float invBlue = 1.0F - blue;
					invRed = 1.0F - invRed * invRed * invRed * invRed;
					invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
					invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
					red = red * (1.0F - gamma) + invRed * gamma;
					green = green * (1.0F - gamma) + invGreen * gamma;
					blue = blue * (1.0F - gamma) + invBlue * gamma;

					min = Math.max(0.03f * f, MagnesiumExtrasConfig.darknessOption.get().value);
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					if (red < 0.0F) {
						red = 0.0F;
					}

					if (green < 0.0F) {
						green = 0.0F;
					}

					if (blue < 0.0F) {
						blue = 0.0F;
					}

					LUMINANCE[blockIndex][skyIndex] = Darkness.luminance(red, green, blue);
				}
			}
		}
	} 
}
