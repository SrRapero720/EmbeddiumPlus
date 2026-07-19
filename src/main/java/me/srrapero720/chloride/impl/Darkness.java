package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT)
public class Darkness {
	public static final double MIN = 0.03D;

    // NETHER/END FOG DIMMING: DimensionSpecialEffects#getBrightnessDependentFogColor IS GONE IN 1.21.11,
    // SO THE SAME DIM IS NOW APPLIED AT THE END OF THE FOG COLOR PIPELINE
    @SubscribeEvent
    public static void onComputeFogColor(final ViewportEvent.ComputeFogColor e) {
        if (ChlorideConfig.darkness.mode == DarkMode.VANILLA) return;

        final ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        final double factor;
        if (level.dimension() == net.minecraft.world.level.Level.NETHER && ChlorideConfig.darkness.onNether) {
            factor = ChlorideConfig.darkness.netherFogBright;
        } else if (level.dimension() == net.minecraft.world.level.Level.END && ChlorideConfig.darkness.onEnd) {
            factor = ChlorideConfig.darkness.endFogBright;
        } else {
            return;
        }
        if (factor == 1.0) return;

        e.setRed((float) Math.max(MIN, e.getRed() * factor));
        e.setGreen((float) Math.max(MIN, e.getGreen() * factor));
        e.setBlue((float) Math.max(MIN, e.getBlue() * factor));
    }

    private static boolean isDark(final net.minecraft.world.level.Level world) {
		if (ChlorideConfig.darkness.mode == DarkMode.VANILLA) return false;

		final ResourceKey<net.minecraft.world.level.Level> dimType = world.dimension();

		if (dimType == net.minecraft.world.level.Level.OVERWORLD) {
			return ChlorideConfig.darkness.onOverworld;
		} else if (dimType == net.minecraft.world.level.Level.NETHER) {
			return ChlorideConfig.darkness.onNether;
		} else if (dimType == net.minecraft.world.level.Level.END) {
			return ChlorideConfig.darkness.onEnd;
		} else if (EntityCulling.isWhitelisted(dimType.identifier(), ChlorideConfig.darkness.dimensionWhitelist)) {
            return true;
        } else if (world.dimensionType().hasSkyLight()) {
			return ChlorideConfig.darkness.byDefault;
		} else {
			return ChlorideConfig.darkness.onNoSkyLight;
		}
	}

	private static float skyFactor(final net.minecraft.world.level.Level world) {
        if (!isDark(world)) return 1;

        if (!world.dimensionType().hasSkyLight()) return 0;

        // SUN_ANGLE IS 0 AT NOON AND 180 AT MIDNIGHT
        final var probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
        final float angle = Mth.positiveModulo(probe.getValue(EnvironmentAttributes.SUN_ANGLE, 0f) / 360f, 1f);
        if (!(angle > 0.25f) || !(angle < 0.75f)) return 1;

		final float oldWeight = Math.max(0, (Math.abs(angle - 0.5f) - 0.2f)) * 20;
        // FULL MOON (INDEX 0) = 1.0 BRIGHTNESS DOWN TO NEW MOON (INDEX 4) = 0.0
		final float moon = ChlorideConfig.darkness.affectedByMoonPhase
                ? Math.abs(probe.getValue(EnvironmentAttributes.MOON_PHASE, 0f).index() - 4) * 0.25f : 0;
		final float moonInterpolated = (float) Mth.lerp(moon, ChlorideConfig.darkness.newMoonBright, ChlorideConfig.darkness.fullMoonBright);
		return Mth.lerp(oldWeight * oldWeight * oldWeight, moonInterpolated, 1f);
    }

	public static boolean enabled = false;
	private static final float[][] LUMINANCE = new float[16][16];

	public static int darken(final int c, final int blockIndex, final int skyIndex) {
		final float lTarget = LUMINANCE[blockIndex][skyIndex];
		final float r = (c & 0xFF) / 255f;
		final float g = ((c >> 8) & 0xFF) / 255f;
		final float b = ((c >> 16) & 0xFF) / 255f;
		final float l = luminance(r, g, b);
		final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

		return f == 1f ? c : 0xFF000000 | Math.round(f * r * 255) | (Math.round(f * g * 255) << 8) | (Math.round(f * b * 255) << 16);
	}

	public static float luminance(final float r, final float g, final float b) {
		return r * 0.2126f + g * 0.7152f + b * 0.0722f;
	}

	public static void updateLuminance(final Minecraft client, final float ambient, final float bossDarkening, final float prevFlicker) {
		final ClientLevel level = client.level;
        if (level == null) return;

        final boolean isDarkOnLevel = Darkness.isDark(level);

		enabled = !(
                !isDarkOnLevel
				|| client.player.hasEffect(MobEffects.NIGHT_VISION)
				|| (client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getWaterVision() > 0)
				|| level.getSkyFlashTime() > 0
        );

        if (!enabled) return;

        final float dimSkyFactor = Darkness.skyFactor(level);
        final DimensionType dim = level.dimensionType();

        for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
            float skyFactor = 1f - skyIndex / 15f;
            skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
            skyFactor *= dimSkyFactor;

            final var value = ChlorideConfig.darkness.mode.value;
            if (value == -1) throw new IllegalStateException("Darkness value can't be negative");

            float min = Math.max(skyFactor * 0.05f, value);
            final float rawAmbient = ambient * skyFactor;
            final float minAmbient = rawAmbient * (1 - min) + min;
            final float skyBase = Lightmap.getBrightness(dim, skyIndex) * minAmbient;

            min = Math.max(0.35f * skyFactor, value);
            final float v = skyBase * (rawAmbient * (1 - min) + min);
            float skyRed = v;
            float skyGreen = v;
            float skyBlue = skyBase;

            if (bossDarkening > 0.0F) {
                final float skyDarkness = bossDarkening;
                skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
                skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
                skyBlue = skyBlue * (1.0F - skyDarkness) + skyBlue * 0.6F * skyDarkness;
            }

            for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
                float blockFactor;

                blockFactor = 1f - blockIndex / 15f;
                blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;

                final float blockBase = blockFactor * Lightmap.getBrightness(dim, blockIndex) * (prevFlicker * 0.1F + 1.5F);
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
                if (level.dimension() == net.minecraft.world.level.Level.END) {
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

                final float gamma = client.options.gamma().get().floatValue() * f;
                float invRed = 1.0F - red;
                float invGreen = 1.0F - green;
                float invBlue = 1.0F - blue;
                invRed = 1.0F - invRed * invRed * invRed * invRed;
                invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
                invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
                red = red * (1.0F - gamma) + invRed * gamma;
                green = green * (1.0F - gamma) + invGreen * gamma;
                blue = blue * (1.0F - gamma) + invBlue * gamma;

                min = Math.max(0.03f * f, ChlorideConfig.darkness.mode.value);
                red = red * (0.99F - min) + min;
                green = green * (0.99F - min) + min;
                blue = blue * (0.99F - min) + min;

                red = Mth.clamp(red, 0.0f, 1.0f);
                green = Mth.clamp(green, 0.0f, 1.0f);
                blue = Mth.clamp(blue, 0.0f, 1.0f);

                LUMINANCE[blockIndex][skyIndex] = Darkness.luminance(red, green, blue);
            }
        }
    }

    public enum DarkMode {
        VANILLA(-1),
        DIM(0.18f),
        DARK(0.12f),
        DARKNESS(0.08f),
        BLACK(0.04f),
        BLACKNESS(0f);

        public final float value;
        DarkMode(final float value) { this.value = value; }
    }
}
