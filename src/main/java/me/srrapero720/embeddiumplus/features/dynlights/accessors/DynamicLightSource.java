/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.srrapero720.embeddiumplus.features.dynlights.accessors;

import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a dynamic light source.
 *
 * @author LambdAurora
 * @version 1.3.3
 * @since 1.0.0
 */
public interface DynamicLightSource {
	/**
	 * Returns the dynamic light source X coordinate.
	 *
	 * @return the X coordinate
	 */
	double tdv$getDynamicLightX();

	/**
	 * Returns the dynamic light source Y coordinate.
	 *
	 * @return the Y coordinate
	 */
	double tdv$getDynamicLightY();

	/**
	 * Returns the dynamic light source Z coordinate.
	 *
	 * @return the Z coordinate
	 */
	double tdv$getDynamicLightZ();

	/**
	 * Returns the dynamic light source world.
	 *
	 * @return the world instance
	 */
	Level tdv$getDynamicLightWorld();

	/**
	 * Returns whether the dynamic light is enabled or not.
	 *
	 * @return {@code true} if the dynamic light is enabled, else {@code false}
	 */
	default boolean tdv$isDynamicLightEnabled() {
		return DynLightsPlus.isEnabled() && DynLightsPlus.get().containsLightSource(this);
	}

	/**
	 * Sets whether the dynamic light is enabled or not.
	 * <p>
	 * Note: please do not call this function in your mod or you will break things.
	 *
	 * @param enabled {@code true} if the dynamic light is enabled, else {@code false}
	 */
	@ApiStatus.Internal
	default void tdv$setDynamicLightEnabled(boolean enabled) {
		this.tdv$resetDynamicLight();
		if (enabled)
			DynLightsPlus.get().addLightSource(this);
		else
			DynLightsPlus.get().removeLightSource(this);
	}

	void tdv$resetDynamicLight();

	/**
	 * Returns the luminance of the light source.
	 * The maximum is 15, below 1 values are ignored.
	 *
	 * @return the luminance of the light source
	 */
	int tdv$getLuminance();

	/**
	 * Executed at each tick.
	 */
	void tdv$dynamicLightTick();

	boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer);

	void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer);
}
