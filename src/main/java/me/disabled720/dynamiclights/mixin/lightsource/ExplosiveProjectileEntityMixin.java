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
import me.disabled720.dynamiclights.api.DynamicLightHandlers;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHurtingProjectile.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity implements DynamicLightSource {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void tdv$dynamicLightTick() {
		if (!this.tdv$isDynamicLightEnabled())
			this.tdv$setDynamicLightEnabled(true);
	}

	@Override
	public int tdv$getLuminance() {
		if (EmbeddiumPlusConfig.tileEntityLighting.get() && DynamicLightHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
