/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.disabled720.dynamiclights.mixin.lightsource;

import me.disabled720.dynamiclights.DynamicLightSource;
import me.disabled720.dynamiclights.LambDynLights;
import me.disabled720.dynamiclights.api.DynamicLightHandlers;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource {
	@Unique
	protected int lambdynlights$luminance;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void tdv$dynamicLightTick() {
		if (!EmbeddiumPlusConfig.tileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this)) {
			this.lambdynlights$luminance = 0;
			return;
		}

		if (this.isOnFire() || this.isCurrentlyGlowing()) {
			this.lambdynlights$luminance = 15;
		} else {
			int luminance = 0;
			var eyePos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
			boolean submergedInFluid = !this.level().getFluidState(eyePos).isEmpty();
			for (var equipped : this.getAllSlots()) {
				if (!equipped.isEmpty())
					luminance = Math.max(luminance, LambDynLights.getLuminanceFromItemStack(equipped, submergedInFluid));
			}

			this.lambdynlights$luminance = luminance;
		}

		int luminance = DynamicLightHandlers.getLuminanceFrom(this);
		if (luminance > this.lambdynlights$luminance)
			this.lambdynlights$luminance = luminance;
	}

	@Override
	public int tdv$getLuminance() {
		return this.lambdynlights$luminance;
	}
}
